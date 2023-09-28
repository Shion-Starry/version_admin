package app.desty.chat_admin.common.utils

import android.content.Intent
import app.desty.chat_admin.common.config.UserConfig
import app.desty.chat_admin.common.constants.RouteConstants
import com.alibaba.android.arouter.launcher.ARouter

object LoginUtil {

    fun login() {
    }

    fun logout(manual: Boolean = false) {
        clearLoginInfo(manual)
        ARouter.getInstance()
            .build(app.desty.chat_admin.common.constants.RouteConstants.Main.welcome)
            .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            .navigation()
    }

    fun clearLoginInfo(manual: Boolean = false) {
        //清空持久化的用户信息
        UserConfig.clearAll()
        //解绑push服务
        PushingBindUtil.autoBindOrUnbind(null)
        //清空通知
        NotificationUtil.cancelAll()
    }


    /**
     * 登录后的路由跳转
     */
    fun routeToHomePage() {
        ARouter.getInstance().build(RouteConstants.Home.homePage)
            .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            .navigation()
    }

    /*
    fun routeToNextPageAfterLogin(userInfo: UserInfo?=null): Boolean {
        val tmpUserInfo = userInfo ?: UserConfig.userInfo
        if (tmpUserInfo?.isAdmin != 1) {
            //去首页
            ARouter.getInstance().build(app.desty.chat_admin.common.constants.RouteConstants.Home.homePage)
                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                .navigation()
            return true
        }
        when (tmpUserInfo?.tourGuide) {
            null                       -> return false
            TourGuide.Init.apiType     -> {
                //去填写信息
                ARouter.getInstance().build(app.desty.chat_admin.common.constants.RouteConstants.Login.completeUserInfo)
                    .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    .navigation()
            }

            TourGuide.UserInfo.apiType -> {
                //去绑定店铺
                ARouter.getInstance().build(app.desty.chat_admin.common.constants.RouteConstants.Setting.bindStore)
                    .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    .navigation()
            }

            else                       -> {
                //去首页
                ARouter.getInstance().build(app.desty.chat_admin.common.constants.RouteConstants.Home.homePage)
                    .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    .navigation()
            }
        }
        return true
    }
     */
}