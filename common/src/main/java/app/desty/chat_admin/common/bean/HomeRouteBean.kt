package app.desty.chat_admin.common.bean

import android.os.Bundle
import android.os.Parcelable
import app.desty.chat_admin.common.enum_bean.GlobalDialogEnum
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeRouteBean(
    val routePath: String?=null,
    val bundle: Bundle?=null,
    val dialogEnum: GlobalDialogEnum?=null,
                        ):Parcelable
