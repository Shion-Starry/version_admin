package app.desty.chat_admin.common.bean

import kotlinx.serialization.Serializable

@Serializable
data class StoreSyncProgress(
    val completedOrderJobNum: Int = 0,
    val completedProductJobNum: Int = 0,
    val failOrderJobNum: Int = 0,
    val failProductJobNum: Int = 0,
    val progressPercentage: Int = 0,
    val totalOrderJobNum: Int = 0,
    val totalProductJobNum: Int = 0,
    val undoOrderJobNum: Int = 0,
    val undoProductJobNum: Int = 0
                            ) {
    fun getShowPercentage(): Int = if (totalOrderJobNum + totalProductJobNum == 0) {
        100
    } else {
        progressPercentage
    }
}