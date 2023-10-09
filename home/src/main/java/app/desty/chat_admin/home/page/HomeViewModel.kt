package app.desty.chat_admin.home.page

import androidx.lifecycle.MutableLiveData
import app.desty.chat_admin.common.base.BaseVM
import app.desty.chat_admin.common.enum_bean.HomePageType

class HomeViewModel : BaseVM() {
    val pageType = MutableLiveData(HomePageType.Home)
    val isHome = MutableLiveData(true)
}