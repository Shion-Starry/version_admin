//
// Created by 林孝可 on 2021/11/9.
//

#ifndef SHANDONG_SCHOOL_K_H
#define SHANDONG_SCHOOL_K_H

#include <string>

static int key_length = 16;//密钥长度
static int key_offset = 2217 * 4;//起始偏移量
static int bit_offset = 23 * 4;//间隔

std::string b2s(const char *data, int channel);


#endif //SHANDONG_SCHOOL_K_H
