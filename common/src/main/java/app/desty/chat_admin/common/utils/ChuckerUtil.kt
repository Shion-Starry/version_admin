package app.desty.chat_admin.common.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import app.desty.chat_admin.common.BuildConfig
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient

object ChuckerUtil {
    fun init(context: Context, okHttpBuilder: OkHttpClient.Builder) {
        if (BuildConfig.desty_debug) {
            //此处使用反射调用，来解耦测试包和生产包
            try {
                val clz = Class.forName("com.chuckerteam.chucker.api.ChuckerCollector")
                val constructor1 =
                    clz.getDeclaredConstructor(Context::class.java, Boolean::class.java)
                val collector = constructor1.newInstance(context, false)
                val builderClz =
                    Class.forName("com.chuckerteam.chucker.api.ChuckerInterceptor\$Builder")
                val builderConstructor = builderClz.getDeclaredConstructor(Context::class.java)
                val builder = builderConstructor.newInstance(context)
                builderClz.getDeclaredMethod("collector", clz)
                    .invoke(builder, collector)
                builderClz.getDeclaredMethod("maxContentLength", Long::class.java)
                    .invoke(builder, 250000L)
                builderClz.getDeclaredMethod("alwaysReadResponseBody", Boolean::class.java)
                    .invoke(builder, false)
                builderClz.getDeclaredMethod("createShortcut",Boolean::class.java)
                    .invoke(builder,false)
                val interceptor = builderClz.getDeclaredMethod("build").invoke(builder)
//                com.chuckerteam.chucker.api.ChuckerCollector(context).showNotification = false
//            val interceptor1 = com.chuckerteam.chucker.api.ChuckerInterceptor.Builder(context)
//                .collector(com.chuckerteam.chucker.api.ChuckerCollector(context))
//                .maxContentLength(250000L)
//                .alwaysReadResponseBody(false)
//                .build()
                if (interceptor is Interceptor) {
                    okHttpBuilder.addInterceptor(interceptor)
                }
            } catch (e: Exception) {
                if (BuildConfig.desty_debug) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun openChucker(context: Context) {
        // 不在许可列表，不初始chucker
        if (!checkAllowList(context)) return
        val clz = Class.forName("com.chuckerteam.chucker.api.Chucker")
        val obj = clz.getDeclaredField("INSTANCE")
        val result = clz.getDeclaredMethod("getLaunchIntent", Context::class.java).invoke(obj, context)
        if (result is Intent) {
            context.startActivity(result)
        }
    }


    //安全配置，校验是否为本地许可的设备
    private fun checkAllowList(context: Context): Boolean {
        val json = Json {
            ignoreUnknownKeys = true // JSON和数据模型字段可以不匹配
            coerceInputValues = true // 如果JSON字段是Null则使用默认值
        }
        val deviceId = DeviceUtil.getDeviceId()
        val jsonStr = app.desty.chat_admin.common.utils.FileUtil.assetsFile2Str(context, "ChuckerList.json")
        Log.i(javaClass.name, "checkAllowList:$jsonStr")
        val allowList = json.decodeFromString<List<String>>(jsonStr)
//        return allowList.contains(deviceId)
        //暂时全部许可
        return true
    }
}