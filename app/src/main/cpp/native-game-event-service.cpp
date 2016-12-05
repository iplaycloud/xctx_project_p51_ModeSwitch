//
// Created by Administrator on 2016/6/8.
//

#include <jni.h>
#include <stdio.h>
#include <android/log.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#define  LOG_TAG    "TestGameEventService"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

static const char *classPathName = "com/xctx/iplay/modeswitch/NativeUartLib";

int fd;

/*
 * shield_mode掌机模式：关闭event服务
 * 并通知game sdk
 * */
static jint start_service(JNIEnv *env, jobject thiz) {

    jint ret = 0;

    LOGI("准备开启GameEventService\n");

    system("game_client startService");

    LOGI("开启GameEventService完成\n");

    return ret;
}

/*
 * shield_mode掌机模式：开启event服务
 *
 * */
static jint stop_service(JNIEnv *env, jobject thiz) {

    jint ret = 0;

    LOGI("准备停止GameEventService\n");

    system("game_client stopService");

    LOGI("停止GameEventService完成\n");

    return ret;
}

/**
 *第一个变量name是Java中函数的名字。
 *第二个变量signature，用字符串是描述了函数的参数和返回值
 *第三个变量fnPtr是函数指针，指向C函数。
 */
static JNINativeMethod methods[] = {{"startService",  "()I", (void *) start_service},
                                    {"stopService",  "()I", (void *) stop_service},
                                    };

/*
 * Register several native methods for one class.
 */
static int registerNativeMethods(JNIEnv *env, const char *className,
                                 JNINativeMethod *gMethods, int numMethods) {
    jclass clazz;

    clazz = env->FindClass(className);
    if (clazz == NULL) {
        //ALOGE("Native registration unable to find class '%s'", className);
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        //ALOGE("RegisterNatives failed for '%s'", className);
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

/*
 * Register native methods for all classes we know about.
 *
 * returns JNI_TRUE on success.
 */
static int registerNatives(JNIEnv *env) {
    if (!registerNativeMethods(env, classPathName, methods,
                               sizeof(methods) / sizeof(methods[0]))) {
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

/*
 * This is called by the VM when the shared library is first loaded.
 */
typedef union {
    JNIEnv *env;
    void *venv;
} UnionJNIEnvToVoid;

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    UnionJNIEnvToVoid uenv;
    uenv.venv = NULL;
    jint result = -1;
    JNIEnv *env = NULL;

    // ALOGI("JNI_OnLoad");

    if (vm->GetEnv(&uenv.venv, JNI_VERSION_1_4) != JNI_OK) {
        //   ALOGE("ERROR: GetEnv failed");
        goto bail;
    }
    env = uenv.env;

    if (registerNatives(env) != JNI_TRUE) {
        // ALOGE("ERROR: registerNatives failed");
        goto bail;
    }

    result = JNI_VERSION_1_4;

    bail:
    return result;
}
