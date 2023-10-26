package app.desty.chat_admin.common.utils

import androidx.databinding.BaseObservable
import androidx.databinding.BindingAdapter
import com.drake.brv.PageRefreshLayout
import com.drake.statelayout.StateLayout
import com.drake.statelayout.Status

class StateLayoutKtx {


    companion object {
        private fun StateLayout.setLayoutState(layoutState: LayoutState) {
            when (layoutState.state) {
                MyStatus.LOADING -> showLoading(layoutState.tag)
                MyStatus.EMPTY -> showEmpty(layoutState.tag)
                MyStatus.CONTENT -> showContent(layoutState.tag)
                MyStatus.ERROR -> showError(layoutState.tag)
                MyStatus.REFRESHING -> showContent(layoutState.tag)
            }
        }

        private fun PageRefreshLayout.setLayoutState(layoutState: LayoutState) {
            when (layoutState.state) {
                MyStatus.LOADING -> showLoading(layoutState.tag, layoutState.isRefresh)
                MyStatus.EMPTY -> showEmpty(layoutState.tag)
                MyStatus.CONTENT -> showContent(layoutState.hasMore, layoutState.tag)
                MyStatus.ERROR -> showError(layoutState.tag)
                MyStatus.REFRESHING -> autoRefresh()
            }
        }

        @BindingAdapter(value = ["layoutState"])
        @JvmStatic
        fun setState(view: StateLayout, layoutState: LayoutState) {
            view.setLayoutState(layoutState)
        }

        @BindingAdapter(value = ["onRefresh"])
        @JvmStatic
        fun setOnRefresh(view: StateLayout, onRefresh: StateLayout.(Any?) -> Unit) {
            view.onRefresh(onRefresh)
        }

        @BindingAdapter(value = ["layoutState"])
        @JvmStatic
        fun setState(view: PageRefreshLayout, layoutState: LayoutState) {
            view.setLayoutState(layoutState)
        }

        @BindingAdapter(value = ["onRefresh"])
        @JvmStatic
        fun setOnRefresh(view: PageRefreshLayout, onRefresh: PageRefreshLayout.(Any?) -> Unit) {
            view.onRefresh(onRefresh)
        }

    }
}

enum class MyStatus {
    LOADING, EMPTY, ERROR, CONTENT, REFRESHING
}

data class LayoutState(
    var state: MyStatus = MyStatus.CONTENT,
    var tag: Any? = null,
    var hasMore: Boolean = true,
    var isRefresh: Boolean = true,
    var defaultBlock: ((Status) -> Any?)? = null
) : BaseObservable() {
    fun showingContent(): Boolean = (state == MyStatus.CONTENT) || (state == MyStatus.REFRESHING)
    fun showLoading(tag: Any? = null, isRefresh: Boolean = true) {
        state = MyStatus.LOADING
        this.tag = tag ?: defaultBlock?.invoke(Status.LOADING)
        this.isRefresh = isRefresh
        notifyChange()
    }

    fun showEmpty(tag: Any? = null) {
        state = MyStatus.EMPTY
        this.tag = tag ?: defaultBlock?.invoke(Status.EMPTY)
        notifyChange()
    }

    fun showContent(tag: Any? = null, hasMore: Boolean = true) {
        state = MyStatus.CONTENT
        this.tag = tag ?: defaultBlock?.invoke(Status.CONTENT)
        this.hasMore = hasMore
        notifyChange()
    }

    fun showError(tag: Any? = null) {
        state = MyStatus.ERROR
        this.tag = tag ?: defaultBlock?.invoke(Status.ERROR)
        notifyChange()
    }

    fun showRefreshing(tag: Any? = null) {
        state = MyStatus.REFRESHING
        this.tag = tag ?: defaultBlock?.invoke(Status.CONTENT)
        notifyChange()
    }

}

