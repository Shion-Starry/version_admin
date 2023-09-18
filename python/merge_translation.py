import argparse
import pandas as pd
from pandas import DataFrame
import os
import hashlib

pd.set_option('display.max_colwidth', 20)
pd.set_option('display.max_columns', 20)
language_list = ['English', 'Bahasa']
module_list = {}


# 生成MD5
def genearteMD5(str):
    hl = hashlib.md5()
    hl.update(str.encode(encoding='utf-8'))
    return '_' + hl.hexdigest()[:8]


def add_module_col(df: DataFrame):
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

        isBody = True
        if '【' in key and '】' in key:
            isBody = False
            now_module = key
            if now_module not in module_list:
                module_list[now_module] = len(module_list)
        df.loc[index, 'module'] = '%08d' % (module_list[now_module]) + now_module
        df.loc[index, 'isBody'] = isBody


def merge_translation(base_file_path, translation_file_path, version):
    if not os.path.exists(base_file_path):
        print('base file not exists')
        exit(0)
    if not os.path.exists(translation_file_path):
        print('cloud file not exists')
        exit(0)
    if os.path.exists(base_file_path) and os.path.exists(translation_file_path):
        print(base_file_path)
        print(translation_file_path)
        base_df = pd.read_csv(base_file_path)
        translation_df = pd.read_csv(translation_file_path)
        add_module_col(base_df)
        add_module_col(translation_df)
        # print(module_list)
        translation_df = translation_df.set_index('Android-key')
        base_df = base_df.set_index('Android-key')
        diff_df = DataFrame(columns=base_df.columns)

        for index, row in base_df.iterrows():
            if pd.isna(index):
                continue
            if index in translation_df.index:
                for language in language_list:
                    base_df.loc[index, language] = translation_df.loc[index, language]
            else:
                i = len(diff_df)
                diff_df.loc[i] = row
                diff_df.loc[i, 'Android-key'] = index
                diff_df.loc[i, 'Version'] = version
        # 分模块合并
        translation_df = translation_df.reset_index(drop=False)
        translation_df = pd.concat([translation_df, diff_df]) \
            .sort_values(by=['module', 'Version'], na_position='first')
        translation_df = translation_df.reset_index(drop=False)
        # print(translation_df)

        for module in set(translation_df['module'].tolist()):
            m_df = translation_df[translation_df['module'] == module]
            v = m_df.tail(1)['Version']
            i = m_df.head(1).index
            # print(f'{i.name} module {module} -> Version {v.values}')
            translation_df.loc[m_df.head(1).index.values, 'Version'] = v.values
        # print(translation_df)
        # 遍历生成code
        for index, row in translation_df.iterrows():
            key = row['Android-key']
            if pd.isna(key) or ('【' in key and '】' in key): continue
            translation_df.loc[index, 'Code'] = genearteMD5(key)
        # 输出合并新增key之后的云文档
        translation_df = translation_df.sort_values(by=['module', 'isBody', 'Android-key'],
                                                    na_position='first')
        translation_df[['Code', 'Version', 'Android-key', 'English', 'Bahasa']].to_csv(
            os.path.join(os.path.dirname(translation_file_path), "NewCloudTranslation.csv"),
            index=False)
        base_df = base_df.sort_values(by=['module', 'isBody', 'Android-key'], na_position='first')
        base_df.reset_index(drop=False)[['Code', 'Android-key', 'English', 'Bahasa']].to_csv(
            base_file_path, index=False)
        print('Done')


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--base_file', '-b',
                        default='../sdk_string/translation/Translation.csv',
                        help='base file of translation')
    parser.add_argument('--cloud_file', '-c',
                        default='../sdk_string/translation/CloudTranslation.csv',
                        help='cloud file of translation')
    parser.add_argument('--version', '-v', help='current version')
    args = parser.parse_args()
    merge_translation(args.base_file, args.cloud_file, args.version)
