#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_example_jinquan_pan_mnist_1ensorflow_1androiddemo_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "TensoFlow MNIST测试Demo:JNI";
    return env->NewStringUTF(hello.c_str());
}
