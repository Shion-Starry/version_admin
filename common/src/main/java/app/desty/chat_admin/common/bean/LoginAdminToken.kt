package app.desty.chat_admin.common.bean

import kotlinx.serialization.Serializable

@Serializable
data class LoginAdminToken(
    val token: String = "",
    val expirationTime: Int = 86400
                          )
