package app.desty.chat_admin.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import app.desty.chat_admin.R
import app.desty.chat_admin.common.config.UserConfig
import app.desty.chat_admin.common.constants.DestyConstants
import app.desty.chat_admin.common.constants.RouteConstants
import app.desty.chat_admin.common.utils.ActivityLifecycleManager
import app.desty.sdk.logcat.Logcat
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ThrowableUtils

@Route(path = RouteConstants.Main.splash)
class RouteActivity : Activity() {

    private val msgGetVerInfo = 1
    private val msgGoNextPage = 2
    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                msgGoNextPage -> {
                    if (TextUtils.isEmpty(UserConfig.token)) {
                        ARouter.getInstance()
                            .build(RouteConstants.Main.welcome)
                            .navigation()
                        finish()
                    } else {
                        executeIntent(intent)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {//默认白色状态栏
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowInsetsControllerCompat(window, window.decorView)
            .hide(WindowInsetsCompat.Type.statusBars())
        setContentView(R.layout.activity_route)
//        splashScreen.setKeepOnScreenCondition { true }
        val activity: AppCompatActivity? =
            ActivityLifecycleManager.getInstance().findActivity(RouteConstants.Home.homePage)
        if (activity != null) {
            handler.sendEmptyMessageDelayed(msgGoNextPage, 100)
        } else {
            handler.sendEmptyMessageDelayed(msgGoNextPage, 100)
        }
    }

    private fun executeIntent(intent: Intent) {
        Logcat.i().msg("Splash data:")
            .msg("action:${intent.action}")
            .msg("data:${intent.data?.toString() ?: ""}")
            .out()
        try {
            when (intent.action) {
                DestyConstants.notificationAction -> guideToMain()
                else                                            -> throw Exception("Unknown Action")
            }
        } catch (e: Exception) {
            Logcat.e(ThrowableUtils.getFullStackTrace(e))
            guideToMain()
        }
        finish()
    }

    private fun guideToMain() {
        ARouter.getInstance()
            .build(RouteConstants.Home.homePage)
            .navigation()
    }

}