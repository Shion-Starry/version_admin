package app.desty.chat_admin.splash

import android.app.Activity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import app.desty.chat_admin.R
import app.desty.chat_admin.common.constants.RouteConstants
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter

@Route(path = RouteConstants.Main.splash)
class RouteActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {//默认白色状态栏
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowInsetsControllerCompat(window, window.decorView)
            .hide(WindowInsetsCompat.Type.statusBars())
        setContentView(R.layout.activity_route)
//        splashScreen.setKeepOnScreenCondition { true }

        goToNextActivity()

    }

    private fun goToNextActivity() {
        ARouter.getInstance().build(RouteConstants.Main.welcome).navigation()
        finish()
    }
}