package app.desty.chat_admin.common.bean

import androidx.lifecycle.MutableLiveData

data class Versions (
    val majorInput: MutableLiveData<String> = MutableLiveData("0"),
    val subInput: MutableLiveData<String> = MutableLiveData("0"),
    val fixInput: MutableLiveData<String> = MutableLiveData("0"),
    val buildInput: MutableLiveData<String> = MutableLiveData("0")
) {
    fun setVersions(versionGroup: VersionGroup) {
        majorInput.value = versionGroup.major.toString()
        subInput.value = versionGroup.sub.toString()
        fixInput.value = versionGroup.fix.toString()
        buildInput.value = versionGroup.build.toString()
    }
}