package app.desty.chat_admin.common.bean

import kotlinx.serialization.Serializable

@Serializable
class BasePage<T>(
    val total: Int = 0,
    val size: Int = 0,
    val current: Int = 1,
    val pages: Int = 1,
    val records: List<T> = emptyList()
                 ) {


}