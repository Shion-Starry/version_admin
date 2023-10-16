package app.desty.chat_admin.upload.page.upload_new

import android.view.View.OnClickListener
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import app.desty.chat_admin.common.base.BaseVM
import app.desty.chat_admin.common.enum_bean.DialogInputType
import app.desty.chat_admin.common.utils.MyToast

class UploadNewViewModel : BaseVM() {
    val featureTextList = mutableListOf<MutableLiveData<String>>()
    val onclickListenerList = mutableListOf<OnClickListener>()
    val canUpload = MediatorLiveData(false)
    val dialogType = MutableLiveData<DialogInputType>()
    val inputText1 = MutableLiveData("")
    val inputText2 = MutableLiveData("0")
    val inputText3 = MutableLiveData("0")
    val inputText4 = MutableLiveData("0")

    init {
        for (enumType in DialogInputType.values()) {
            if (enumType == DialogInputType.Channel) {
                featureTextList.add(MutableLiveData("android"))
            } else {
                featureTextList.add(MutableLiveData(""))
            }
        }

        for (enumType in DialogInputType.values()) {
            when (enumType) {
                DialogInputType.LatestVersion -> {
                    onclickListenerList.add(
                        OnClickListener{
                            MyToast.showToast("This is the LatestVersion")
                        }
                    )
                }
                DialogInputType.CompatVersion -> {
                    onclickListenerList.add(
                        OnClickListener {
                            MyToast.showToast("This is the CompatVersion")
                        }
                    )
                }
                else -> {
                    onclickListenerList.add(
                        OnClickListener {  }
                    )
                }
            }
        }

        for (editorText in featureTextList) {
            canUpload.addSource(editorText) {updateUploadEnabled()}
        }
    }

    private fun updateUploadEnabled() {
        for (editorText in featureTextList) {
            if (editorText.value.isNullOrEmpty()) {
                canUpload.value = false
                return
            }
        }
        canUpload.value = true
    }

}