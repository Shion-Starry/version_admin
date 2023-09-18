package app.desty.chat_admin.common.api

object Api {
    const val upgrade = "/chat/seller/appVersion/getLastest"
    const val pullMessages = "/chat/search/push/pullMessages"
    const val getBuyerInfoByGroupIds = "/chat/search/push/getBuyerInfoByGroupList"
    const val getBatchSellerAccountInfo = "/chat/seller/accountSeller/getBatchAccountInfos"
    const val getStickerList = "/chat/seller/sticker/list"
    const val getShop = "/chat/seller/buyerFriend/getShop"
    const val getUserInfo = "/chat/seller/accountSeller/get"
    const val guide = "/chat/seller/accountSeller/guide"
    const val readGroupMessage = "/chat/dim/message/readMsgEvent"
    const val gptGeneration = "/chat/task/gpt/generate"

    const val bindPush = "/platform/push/push/device/info/binding"
    const val unbindPush = "/platform/push/push/device/info/unbinding"
}