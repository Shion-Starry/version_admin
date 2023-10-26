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
                Status.LOADING -> showLoading(layoutState.tag)
                Status.EMPTY -> showEmpty(layoutState.tag)
                Status.CONTENT -> showContent(layoutState.tag)
                Status.ERROR -> showError(layoutState.tag)
            }
        }

        private fun PageRefreshLayout.setLayoutState(layoutState: LayoutState) {
            when (layoutState.state) {
                Status.LOADING -> {
                    if (layoutState.isAutoRefresh) {
                        autoRefresh()
                    } else {
                        showLoading(layoutState.tag, layoutState.isRefresh)
                    }
                }
                Status.EMPTY -> showEmpty(layoutState.tag)
                Status.CONTENT -> showContent(layoutState.hasMore, layoutState.tag)
                Status.ERROR -> showError(layoutState.tag)
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

data class LayoutState(
    var state: Status = Status.CONTENT,
    var tag: Any? = null,
    var hasMore: Boolean = true,
    var isRefresh: Boolean = true,
    var isAutoRefresh: Boolean = true,
    var defaultBlock: ((Status) -> Any?)? = null
) : BaseObservable() {
    fun showingContent(): Boolean = (state == Status.CONTENT) || (state == Status.LOADING && isAutoRefresh)
    fun showLoading(tag: Any? = null, isRefresh: Boolean = true, isAutoRefresh: Boolean = true) {
        state = Status.LOADING
        this.tag = tag ?: defaultBlock?.invoke(Status.LOADING)
        this.isRefresh = isRefresh
        this.isAutoRefresh = isAutoRefresh
        notifyChange()
    }

    fun showEmpty(tag: Any? = null) {
        state = Status.EMPTY
        this.tag = tag ?: defaultBlock?.invoke(Status.EMPTY)
        notifyChange()
    }

    fun showContent(tag: Any? = null, hasMore: Boolean = true) {
        state = Status.CONTENT
        this.tag = tag ?: defaultBlock?.invoke(Status.CONTENT)
        this.hasMore = hasMore
        notifyChange()
    }

    fun showError(tag: Any? = null) {
        state = Status.ERROR
        this.tag = tag ?: defaultBlock?.invoke(Status.ERROR)
        notifyChange()
    }
}

