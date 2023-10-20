package app.desty.chat_admin.common.config

import android.view.View
import app.desty.chat_admin.common.enum_bean.HomePageType

interface ToolbarClickListener {
    companion object {
        const val RIGHT_OPERATE = 1
        const val BACK = 2
    }

    fun clickFragToolbar(view: View, homePageType: HomePageType, buttonType: Int)

}