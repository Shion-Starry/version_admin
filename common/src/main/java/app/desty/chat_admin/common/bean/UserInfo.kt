package app.desty.chat_admin.common.bean

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val account: String = "",
    val guideNum: Int = 0,
    val isAdmin: Int = 0,
    val isInit: Int = 0,
    val mainAccount: String = "",
    val masterUserId: String = "",
    val mobile: String = "",
    val registerType: Int = 0,
    val whatsappSettingGuide: Int = 0,
    val whatsappTour: Int = 0,
    val imAccount: String = "",
    val nickname: String = "",
    val email: String = "",
    val image: String = "",
    var tourGuide: Int? = null,
                   ) {
    fun getShowName(): String {
        if (nickname.isNotEmpty()) return nickname
        if (mobile.isNotEmpty()) return mobile
        if (email.isNotEmpty()) return email
        return ""
    }
}
//account?: string;
//createTime?: string;
//email?: string;
//image?: string;
//mobile?: string;
//nickname?: string;
//platformShopMapDtoList?: Shop[];
//shopNum?: number;
//unionId?: string;
//isAdmin?: number;
//isInit?: number;
//mainAccount?: string;
//masterUserId?: string;
//platformList?: any;
//registerType?: number;
//imAccount: string;
//guideNum?: number;
//whatsappTour?: number;
//whatsappSettingGuide?: number;