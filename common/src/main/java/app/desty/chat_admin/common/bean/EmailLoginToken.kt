package app.desty.chat_admin.common.bean

import kotlinx.serialization.Serializable

@Serializable
data class EmailLoginToken(
    val token: String = ""
                          )
