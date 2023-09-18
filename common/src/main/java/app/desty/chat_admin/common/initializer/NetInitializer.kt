package app.desty.chat_admin.common.initializer

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.startup.Initializer
import app.desty.chat_admin.common.BuildConfig
import app.desty.chat_admin.common.config.EnvConfig
import app.desty.chat_admin.common.net.ChatNetErrorHandler
import app.desty.chat_admin.common.net.GlobalHeaderInterceptor
import app.desty.chat_admin.common.net.SerializationConverter
import app.desty.chat_admin.common.utils.ChuckerUtil
import app.desty.chat_admin.common.widget.LoadingDialog
import com.blankj.utilcode.util.StringUtils
import com.drake.net.NetConfig
import com.drake.net.cookie.PersistentCookieJar
import com.drake.net.okhttp.setConverter
import com.drake.net.okhttp.setDebug
import com.drake.net.okhttp.setDialogFactory
import com.drake.net.okhttp.setErrorHandler
import com.drake.net.okhttp.setRequestInterceptor
import com.drake.net.okhttp.trustSSLCertificate
import okhttp3.Cache
import java.util.concurrent.TimeUnit

class NetInitializer : Initializer<String> {
    override fun create(context: Context): String {
        Log.i("Initializer", "NetInitializer:create")
        initNet(context)
        return "NetInitializer"
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(ConfigInitializer::class.java)

    /**
     * 初始化网络库
     */
    private fun initNet(context: Context) {
        NetConfig.initialize(EnvConfig.getBaseUrl(), context) {

            if (BuildConfig.desty_debug) {
                trustSSLCertificate()
            }
            // 超时设置
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)

            // 本框架支持Http缓存协议和强制缓存模式
            cache(
                Cache(
                    context.cacheDir,
                    1024 * 1024 * 128
                     )
                 ) // 缓存设置, 当超过maxSize最大值会根据最近最少使用算法清除缓存来限制缓存大小

            // LogCat是否输出异常日志, 异常日志可以快速定位网络请求错误
            setDebug(BuildConfig.DEBUG)

            // AndroidStudio OkHttp Profiler 插件输出网络日志
//            addInterceptor(LogRecordInterceptor(BuildConfig.DEBUG))

            // 添加持久化Cookie管理
            cookieJar(PersistentCookieJar(context))

            // 通知栏监听网络日志
            ChuckerUtil.init(context, this)

            val userAgent = StringUtils.format(
                "DestyMenuSeller/%s (%s;Android %s)",
                BuildConfig.appVersionName, Build.BRAND, Build.VERSION.RELEASE
                                              )
            // 添加请求拦截器, 可配置全局/动态参数
            setRequestInterceptor(
                GlobalHeaderInterceptor(
                    userAgent,
                    BuildConfig.appVersionName,
                    BuildConfig.appVersionCode
                                       )
                                 )

            // 数据转换器
            setConverter(SerializationConverter())

            setErrorHandler(ChatNetErrorHandler)
            setDialogFactory { // 全局加载对话框
                LoadingDialog(it).apply {
                    setCanceledOnTouchOutside(false)
                    setCancelable(false)
                }
            }
        }
    }

}