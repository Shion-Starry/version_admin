package app.desty.chat_admin.common.utils

import androidx.databinding.BaseObservable
import androidx.databinding.BindingAdapter
import com.drake.statelayout.StateLayout
import com.drake.statelayout.Status

class StateLayoutKtx {


    companion object {
        private fun StateLayout.setLayoutState(layoutState: LayoutState) {
            when (layoutState.state) {
                Status.LOADING -> showLoading(layoutState.tag)
                Status.EMPTY   -> showEmpty(layoutState.tag)
                Status.CONTENT -> showContent(layoutState.tag)
                Status.ERROR   -> showError(layoutState.tag)
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
    }
}

data class LayoutState(
    var state: Status = Status.CONTENT,
    var tag: Any? = null,
    var defaultBlock: ((Status) -> Any?)? = null,
                      ) : BaseObservable() {
    fun showLoading(tag: Any? = null) {
        state = Status.LOADING
        this.tag = tag ?: defaultBlock?.invoke(Status.LOADING)
        notifyChange()
    }

    fun showEmpty(tag: Any? = null) {
        state = Status.EMPTY
        this.tag = tag ?: defaultBlock?.invoke(Status.EMPTY)
        notifyChange()
    }

    fun showContent(tag: Any? = null) {
        state = Status.CONTENT
        this.tag = tag ?: defaultBlock?.invoke(Status.CONTENT)
        notifyChange()
    }

    fun showError(tag: Any? = null) {
        state = Status.ERROR
        this.tag = tag ?: defaultBlock?.invoke(Status.ERROR)
        notifyChange()
    }
}

