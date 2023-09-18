import argparse
import base64
import fnmatch
import hashlib
import hmac
import json
import os
import shutil
import time
from retrying import retry
import qrcode
import requests
from minio import Minio
from datetime import timedelta
import zipfile

baseUrl = os.environ.get('baseUrl', 'http://192.168.8.244:9000')
defaultUrl = 'https://open.larksuite.com/open-apis/bot/v2/hook/8934c22b-d93b-477a-85b3-1a983b7ba923'
defaultKey = 'eKEswYHvoD4vua0GUutmy'
splitKey = ','
webhookUrl = os.environ.get('webhookUrl', defaultUrl)
webhookKey = os.environ.get('webhookKey', defaultKey)
minioBucket = 'desty-chat-android'
minioClient = Minio('n-minio.desty.chat',
                    access_key='forminio',
                    secret_key='o2yOJB^idA*Wemw',
                    secure=True)
qrcodeColor = os.environ.get('qrcodeColor', '#6B7280')


def gen_sign(timestamp, secret):
    # Stitch timestamp and secret
    string_to_sign = '{}\n{}'.format(timestamp, secret)
    hmac_code = hmac.new(string_to_sign.encode("utf-8"), digestmod=hashlib.sha256).digest()

    # Base64 processing the results
    sign = base64.b64encode(hmac_code).decode('utf-8')

    return sign


def replace_author(log: str):
    return log.replace('linxiaoke', 'xiaoke.lin').replace('535117267', 'xiaoke.lin').replace(
        '834645976', 'zeyu.li')


def change_log():
    change_list = os.environ.get('SCM_CHANGELOG', '无').split('</n>')  # 获取scm change log
    print('change_list:', change_list)
    tag_change_list = []
    for change in change_list:
        if change == '':
            continue
        tag_change_list.append(replace_author(change.strip()))
    return tag_change_list


def get_env(apk_path: str):
    app_config = {}
    if os.path.exists(apk_path):
        with open(os.path.join(apk_path, 'AppConfig.json'), 'r') as f:
            app_config = json.load(f)
    app_config['UpdateDest'] = change_log()
    print('读取环境变量：>>>start<<<')
    print(app_config)
    for k, v in app_config.items():
        print(k + ':' + str(v))

    print('读取环境变量：>>>end<<<')
    return app_config


def find_match_file(dir_path, wildcard):
    match_file_list = []
    if os.path.exists(dir_path):
        for file_name in os.listdir(dir_path):
            if fnmatch.fnmatch(file_name, wildcard):
                match_file_list.append(os.path.join(dir_path, file_name))
    return match_file_list


def zipDir(dirpath, outFullName):
    """
    压缩指定文件夹
    :param dirpath: 目标文件夹路径
    :param outFullName: 压缩文件保存路径+xxxx.zip
    :return: 无
    """
    zip = zipfile.ZipFile(outFullName, "w", zipfile.ZIP_DEFLATED)
    for path, dirnames, filenames in os.walk(dirpath):
        # 去掉目标跟路径，只对目标文件夹下边的文件及文件夹进行压缩
        fpath = path.replace(dirpath, '')

        for filename in filenames:
            zip.write(os.path.join(path, filename), os.path.join(fpath, filename))
    zip.close()


@retry(stop_max_attempt_number=7, wait_incrementing_start=1000, wait_incrementing_increment=500)
def upload_apk_to_minio(apk_path: str, param):
    # bucket不存在的话创建一个新的
    if not minioClient.bucket_exists(minioBucket):
        minioClient.make_bucket(minioBucket)
    build_type = "Testing" if param['DESTY_DEBUG'] == 'true' else "Production"
    version = param['VersionName']
    dir_name = time.strftime(f'{build_type}/{version}/build_%Y-%m-%d %H:%M:%S',
                             time.localtime(int(param['Time']) / 1000))

    # 处理mapping
    mapping_files = param['MappingDirName']
    mapping_dir = os.path.join(apk_path, mapping_files)

    mapping_dir_zip = f'{mapping_dir}.zip'
    zipDir(mapping_dir, mapping_dir_zip)
    mapping_object_name = f'{dir_name}/mapping.zip'
    minioClient.fput_object(minioBucket, mapping_object_name,
                            os.path.join(mapping_dir, mapping_dir_zip))
    # for mapping_file in os.listdir(mapping_dir):
    #     print(f'正在处理：{mapping_file}')
    #     mapping_object_name = f'{dir_name}/mapping/{mapping_file}'
    #     minioClient.fput_object(minioBucket, mapping_object_name,
    #                             os.path.join(mapping_dir, mapping_file))

    # 处理apk
    apk_file_dict = {}
    for apk_file_name in os.listdir(apk_path):
        if not (apk_file_name.endswith('.apk') or apk_file_name.endswith('.aab')):
            continue
        print(f'正在处理：{apk_file_name}')
        apk_file_path = os.path.join(apk_path, apk_file_name)
        object_name = f'{dir_name}/{apk_file_name}'
        minioClient.fput_object(minioBucket, object_name, apk_file_path)
        apk_url = minioClient.presigned_get_object(minioBucket, object_name,
                                                   expires=timedelta(days=5))
        apk_file_dict[apk_file_name] = apk_url
    return apk_file_dict


def upload_apk_file(apk_path: str, param,
                    tomcat_share_root,
                    apk_share_dir='/Users/linxiaoke/share',
                    mapping_share_dir='/Users/linxiaoke/mappingFile'):
    mapping_files = param['MappingDirName']
    share_dir = os.path.join(apk_share_dir, param['VersionName'])
    if not os.path.exists(share_dir):
        os.makedirs(share_dir)
    if not os.path.exists(mapping_share_dir):
        os.makedirs(mapping_share_dir)
    shutil.copytree(os.path.join(apk_path, mapping_files),
                    os.path.join(mapping_share_dir, mapping_files))  # 拷贝mapping
    apk_file_list = []
    for apk_file_name in os.listdir(apk_path):
        if not apk_file_name.endswith('.apk'): continue
        print(f'正在处理：{apk_file_name}')
        apk_file_path = os.path.join(share_dir, apk_file_name)
        shutil.copy(os.path.join(apk_path, apk_file_name), apk_file_path)
        apk_url = apk_file_path.replace(tomcat_share_root, '%s/share' % baseUrl)
        apk_file_list.append((apk_file_name, apk_url))
    return apk_file_list


def get_token():
    url = "https://open.larksuite.com/open-apis/auth/v3/tenant_access_token/internal"
    payload = {'app_id': 'cli_9f6ea942fa28d009', 'app_secret': 'Vhj63OlPKMfSUniwpQskUhnNx3ZOQIex'}
    headers = {
        'Content-Type': 'application/json; charset=utf-8'
    }

    response = requests.request("POST", url, headers=headers, json=payload)

    return response.json()['tenant_access_token']


@retry(stop_max_attempt_number=7, wait_incrementing_start=1000, wait_incrementing_increment=500)
# 重试7次，起始等待1秒逐次增加0.5秒
def upload_image(token, image_path):
    with open(image_path, 'rb') as f:
        image = f.read()
    resp = requests.post(
        url='https://open.larksuite.com/open-apis/image/v4/put/',
        headers={'Authorization': f"Bearer {token}"},
        files={
            "image": image
        },
        data={
            "image_type": "message"
        },
        stream=True)
    resp.raise_for_status()
    content = resp.json()
    print(content)
    if content.get("code") == 0:
        return content
    else:
        raise Exception("Call Api Error, errorCode is %s" % content["code"])


def url_to_img(url):
    image_name = 'img_%d' % time.time()
    # 二维码的内容,扫描后得到
    img_file_name = '%s.png' % image_name
    qr = qrcode.QRCode(error_correction=qrcode.ERROR_CORRECT_L, border=1, box_size=5)
    qr.add_data(url)
    qr.make_image(back_color="#ffffff", fill_color=qrcodeColor) \
        .save(img_file_name)  # 保存二维码到指定路径

    # 上传lark图床
    content = upload_image(get_token(), img_file_name)
    # 移除本地文件
    os.remove(img_file_name)
    return content['data']['image_key']


@retry(stop_max_attempt_number=7, wait_incrementing_start=1000, wait_incrementing_increment=500)
# 重试7次，起始等待1秒逐次增加0.5秒
def notify_lark_rich(param, download_url):
    job_name = os.getenv('JOB_NAME', '')
    json_dic = {'msg_type': 'post', 'content': {}}
    json_dic['content']['post'] = {}
    content = [
        [{'tag': 'text', 'text': "Job: " + job_name}],
        [{'tag': 'text', 'text': "VersionName: " + param['VersionName']}],
        [{'tag': 'text', 'text': "VersionCode: " + param['VersionCode']}],
        [{'tag': 'text', 'text': "GitBranch: " + os.environ.get('branch', 'testing')}],
        [{'tag': 'text',
          'text': "Environment: " + (
              "Testing" if param['DESTY_DEBUG'] == 'true' else "Production")}],
        [{'tag': 'text',
          'text': "PackagingTime: " + time.strftime('%Y-%m-%d %H:%M:%S',
                                                    time.localtime(int(param['Time']) / 1000))}],
        [{'tag': 'text', 'text': "DownloadUrl: "}]
    ]
    content_cn = [
        [{'tag': 'text', 'text': "任务名: " + job_name}],
        [{'tag': 'text', 'text': "版本名: " + param['VersionName']}],
        [{'tag': 'text', 'text': "版本代号: " + param['VersionCode']}],
        [{'tag': 'text', 'text': "Git分支: " + os.environ.get('branch', 'testing')}],
        [{'tag': 'text',
          'text': "包类型: " + (
              "测试包" if param['DESTY_DEBUG'] == 'true' else "生产包")}],
        [{'tag': 'text',
          'text': "打包时间: " + time.strftime('%Y-%m-%d %H:%M:%S',
                                           time.localtime(int(param['Time']) / 1000))}],
        [{'tag': 'text', 'text': "下载链接: "}]
    ]
    download_url_list = []
    image_key_list = []

    for file_name, url in download_url.items():
        if file_name.endswith('.apk'):
            download_url_list.append([{'tag': 'a', 'text': file_name, 'href': url}])
            image_key_list.append([{'tag': 'img', 'image_key': url_to_img(url)}])
        if file_name.endswith('.aab'):
            download_url_list.append([{'tag': 'a', 'text': file_name, 'href': url}])
    content.extend(download_url_list)
    content_cn.extend(download_url_list)
    content.extend(image_key_list)
    content_cn.extend(image_key_list)
    content.append([{'tag': 'text', 'text': "ChangeLog: "}])
    content_cn.append([{'tag': 'text', 'text': "变更日志: "}])

    for i, dest in enumerate(param['UpdateDest']):
        content.append([{'tag': 'text', 'text': f'\t\t{i + 1}.{dest}'}])
        content_cn.append([{'tag': 'text', 'text': f'\t\t{i + 1}.{dest}'}])
    json_dic['content']['post']['en_us'] = {
        'title': '%s-%s' % (param['AppName'], param['DeviceType']),
        'content': content
    }
    json_dic['content']['post']['zh_cn'] = {
        'title': '%s-%s' % (param['AppName'], param['DeviceType']),
        'content': content_cn
    }

    timestamp = int(time.time())
    for tmpUrl, tmpKey in zip(webhookUrl.split(splitKey), webhookKey.split(splitKey)):
        sign = gen_sign(timestamp, tmpKey)
        json_dic['timestamp'] = timestamp
        json_dic['sign'] = sign
        resp = requests.post(tmpUrl, json=json_dic)
        print(resp.text)
        resp.raise_for_status()


if __name__ == '__main_1_':
    apk_path = '/Users/linxiaoke/git/menu-android/release_app'
    j = notify_lark_rich(get_env(apk_path), "http://baidu.com")
    print(j)

if __name__ == '__main__2':
    apk_path = "/Users/linxk/git/menu-android/release_app"
    param = get_env(apk_path)
    print(upload_apk_to_minio(apk_path, param))

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--apk_path', '-o', default='/Users/linxk/git/menu-android/release_app',
                        type=str,
                        help='apk output path')
    parser.add_argument('--apk_share_dir', '-s', default='/Users/linxiaoke/share', type=str,
                        help='apk share path')
    parser.add_argument('--mapping_share_dir', '-m', default='/Users/linxiaoke/mappingFile',
                        type=str,
                        help='mapping share path')
    parser.add_argument('--tomcat_share_root', '-t', default='/Users/jenkins/share_file',
                        type=str, help='tomcat share root path')
    args = parser.parse_args()
    apk_path = args.apk_path
    apk_share_dir = args.apk_share_dir
    mapping_share_dir = args.mapping_share_dir
    param = get_env(apk_path)
    # download_url = upload_apk_file(apk_path, param,
    #                                args.tomcat_share_root,
    #                                apk_share_dir,
    #                                mapping_share_dir)[0][1]
    download_url = upload_apk_to_minio(apk_path, param)
    notify_lark_rich(param, download_url)
