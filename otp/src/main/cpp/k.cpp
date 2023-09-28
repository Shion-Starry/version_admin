//
// Created by 林孝可 on 2021/11/9.
//

#include "k.h"

/**
 * 从图片数据中获取密钥
 * @param data 图片数据
 * @param channel 数据所在通道，如图片格式为RGBA且数据在B通道那就传入2
 * @return 密钥字符串
 */
std::string b2s(const char *data, int channel) {
    std::string str;
    int key_index = key_offset;
    for (int i = 0; i < key_length; i++) {
        char tmp = 0;
        for (int i1 = 7; i1 >= 0; i1--) {
            char blue = data[key_index + channel];
            if ((blue & 1) > 0) {//奇数
                tmp = (char) (tmp | (1 << i1));
            } else {
                tmp = (char) (tmp & ~(1 << i1));
            }
            key_index += bit_offset;
        }
        str += tmp;
    }

    return str;
}