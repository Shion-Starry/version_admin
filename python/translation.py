import argparse
from pandas import DataFrame
import pandas as pd
import os
import hashlib

module_list = {}
xml_head = '<?xml version="1.0" encoding="utf-8"?>\n<resources>\n'
xml_footer = '</resources>'
xml_note = '\n    <!--  %s  -->\n'
xml_string = '    <string name="%s">%s</string>\n'


# 生成MD5
def genearteMD5(str):
    hl = hashlib.md5()
    hl.update(str.encode(encoding='utf-8'))
    return '_' + hl.hexdigest()[:8]


def sort_translation(df: DataFrame):
    """
    给数据表增加临时分组列
    :param df: 数据表
    """
    if 'module' not in df:
        df.insert(0, 'module', '')
    if 'isBody' not in df:
        df.insert(1, 'isBody', '')
    now_module = ''
    for index, row in df.iterrows():
        key = row['Android-key']
        if pd.isna(key): continue

        is_body = True
        if '【' in key and '】' in key:
            is_body = False
            now_module = key
            if now_module not in module_list:
                module_list[now_module] = len(module_list)
        df.loc[index, 'module'] = '%08d' % (module_list[now_module]) + now_module
        df.loc[index, 'isBody'] = is_body
    return df.sort_values(by=['module', 'isBody', 'Android-key'], na_position='first').drop(
        columns=['module', 'isBody'])


def split_translation(excel_input_path, english_output_path, bahasa_output_path,
                      code_output_path, raise_missing=False, strict=True, debug=False):
    if not os.path.exists(excel_input_path):
        print(f'excel input file not exists in :{excel_input_path}')
    if os.path.exists(english_output_path):
        os.remove(english_output_path)
    if os.path.exists(bahasa_output_path):
        os.remove(bahasa_output_path)
    if os.path.exists(code_output_path):
        os.remove(code_output_path)
    if not os.path.exists(os.path.dirname(english_output_path)):
        os.makedirs(os.path.dirname(english_output_path))
    if not os.path.exists(os.path.dirname(bahasa_output_path)):
        os.makedirs(os.path.dirname(bahasa_output_path))
    if not os.path.exists(os.path.dirname(code_output_path)):
        os.makedirs(os.path.dirname(code_output_path))
    df = pd.read_csv(excel_input_path)
    if 'Code' not in df:
        df.insert(0, 'Code', '')
    df = sort_translation(df)
    has_missing = False
    with open(english_output_path, 'w') as english_xml, \
            open(bahasa_output_path, 'w') as bahasa_xml, \
            open(code_output_path, 'w') as code_xml:
        english_xml.write(xml_head)
        bahasa_xml.write(xml_head)
        code_xml.write(xml_head)
        for index, row in df.iterrows():
            bahasa = row['Bahasa']
            english = row['English']
            key = row['Android-key']
            if pd.isna(key): continue

            if '【' in key and '】' in key:
                english_xml.write(xml_note % key)
                bahasa_xml.write(xml_note % key)
                code_xml.write(xml_note % key)
            else:
                # APP暂时不支持英语版本，不校验英语是否缺失
                # if pd.isna(english):
                #     print(f' Missing English Translation for key: {key}')
                #     has_missing = True
                if pd.isna(bahasa):
                    print(f'Missing Bahasa Translation for key: {key}')
                    has_missing = True
                if strict or (not pd.isna(english)):
                    english_xml.write(xml_string % (key, fix_str(english)))
                else:
                    english_xml.write(xml_string % (key, fix_str(bahasa)))
                bahasa_xml.write(xml_string % (key, fix_str(bahasa)))
                code_xml.write(xml_string % (key, genearteMD5(key)))
                df.loc[index, 'Code'] = genearteMD5(key)
                # row['Code'] = genearteMD5(key)
        english_xml.write(xml_footer)
        bahasa_xml.write(xml_footer)
        code_xml.write(xml_footer)
    df.to_csv(excel_input_path, index=False)
    if not debug:
        if os.path.exists(code_output_path):
            os.remove(code_output_path)
        if os.path.exists(os.path.dirname(code_output_path)):
            os.removedirs(os.path.dirname(code_output_path))
    if has_missing and raise_missing:
        raise Exception("Missing Translation!!!!")


def fix_str(i):
    return str(i).replace('\'', '\\\'')


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--debug', '-d', action='store_true', help='debug mode will output code')
    parser.add_argument('--excel', '-e', default='../sdk_string/translation/Translation.csv',
                        type=str,
                        help='Translation excel path')
    parser.add_argument('--english', '-en', default='../sdk_string/src/main/res/values/strings.xml',
                        type=str,
                        help='english string.xml output path')
    parser.add_argument('--bahasa', '-in',
                        default='../sdk_string/src/main/res/values-in/strings.xml', type=str,
                        help='bahasa string.xml output path')
    parser.add_argument('--code', '-c',
                        default='../sdk_string/src/main/res/values-es/strings.xml', type=str,
                        help='code of android key')
    parser.add_argument('--raiseMissing', '-r', action='store_true',
                        help='raise error if missing translation')
    parser.add_argument('--replaceNan', '-p', action='store_false',
                        help='1 = missing translation will be nan')
    args = parser.parse_args()
    excel_path = args.excel
    english_xml_path = args.english
    bahasa_xml_path = args.bahasa
    a = args.replaceNan
    print("args:", args)
    split_translation(excel_path, english_xml_path, bahasa_xml_path, args.code,
                      args.raiseMissing, args.replaceNan, args.debug)
