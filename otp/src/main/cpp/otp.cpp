#include <jni.h>
#include <string>
#include "k.h"

extern "C" JNIEXPORT jstring JNICALL
Java_app_desty_chat_otp_K_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL Java_app_desty_chat_otp_K_b2s(
        JNIEnv *env,
        jclass clazz/* this */,
        jbyteArray data, jint channel) {

    char *chars = NULL;
    jbyte *bytes;
    bytes = env->GetByteArrayElements(data, JNI_FALSE);
    int chars_len = env->GetArrayLength(data);
    chars = new char[chars_len + 1];
    memset(chars, 0, chars_len + 1);
    memcpy(chars, bytes, chars_len);
    chars[chars_len] = 0;
    env->ReleaseByteArrayElements(data, bytes, 0);
    return env->NewStringUTF(b2s(chars, channel).c_str());
}