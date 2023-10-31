package app.desty.chat_admin.cloud.pages

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import app.desty.chat_admin.cloud.BR
import app.desty.chat_admin.cloud.R
import app.desty.chat_admin.common.base.BaseVMFragment
import app.desty.chat_admin.common.base.DataBindingConfig
import app.desty.chat_admin.common.config.ToolbarClickListener
import app.desty.chat_admin.common.constants.RouteConstants
import app.desty.chat_admin.common.enum_bean.HomePageType
import app.desty.chat_admin.common.utils.MyToast
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.core.result.navigation

@Route(path = RouteConstants.Cloud.main)
class CloudMainFragment : BaseVMFragment<CloudMainViewModel>(), ToolbarClickListener {
    override fun initViewModel() {

    }

    override fun getDataBindingConfig(): DataBindingConfig =
        DataBindingConfig(R.layout.fragment_cloud_main, BR.mState, mState)

    override fun init(savedInstanceState: Bundle?) {

    }

    private val uploadCloudResult = ActivityResultCallback<ActivityResult> {
        if (it.resultCode == Activity.RESULT_OK) {
            MyToast.showToast("The draft has been saved :)")
        }
    }

    override fun clickFragToolbar(view: View, homePageType: HomePageType, buttonType: Int) {
        if (homePageType != HomePageType.Cloud) {
            return
        }

        if (buttonType == ToolbarClickListener.RIGHT_OPERATE) {
            ARouter.getInstance()
                .build(RouteConstants.Cloud.upload)
                .navigation(this@CloudMainFragment, uploadCloudResult)
        }

    }

}