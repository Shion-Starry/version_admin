package app.desty.chat_admin.home.page

import android.os.Bundle
import app.desty.chat_admin.common.base.BaseVM
import app.desty.chat_admin.common.base.BaseVMFragment
import app.desty.chat_admin.common.base.DataBindingConfig
import app.desty.chat_admin.common.constants.RouteConstants
import app.desty.chat_admin.home.BR
import app.desty.chat_admin.home.R
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = RouteConstants.Home.homeState)
class HomeStateFragment : BaseVMFragment<BaseVM>() {
    override fun initViewModel() {

    }

    override fun getDataBindingConfig(): DataBindingConfig =
        DataBindingConfig(R.layout.fragment_home_state, BR.mState, mState)

    override fun init(savedInstanceState: Bundle?) {

    }


}