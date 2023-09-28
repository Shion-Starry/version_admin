package app.desty.chat_admin.home.adapter

import app.desty.chat_admin.common.enum_bean.HomePageType
import app.desty.chat_admin.home.R
import app.desty.chat_admin.home.databinding.ItemNavigationModuleBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

class HomeNaviAdapter : BaseQuickAdapter<HomePageType, BaseDataBindingHolder<ItemNavigationModuleBinding>>
    (R.layout.item_navigation_module) {
    override fun convert(
        holder: BaseDataBindingHolder<ItemNavigationModuleBinding>,
        item: HomePageType
    ) {
        val dataBinding = holder.dataBinding
        dataBinding?.apply {
            data = item
            executePendingBindings()
        }
    }
}