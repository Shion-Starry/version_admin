package app.desty.chat_admin.common.config

import app.desty.chat_admin.common.BuildConfig
import app.desty.chat_admin.common.constants.DestyConstants
import com.blankj.utilcode.util.LanguageUtils
import com.drake.serialize.serialize.annotation.SerializeConfig
import com.drake.serialize.serialize.serialLazy
import java.util.Locale

@SerializeConfig(mmapID = "app_config", cryptKey = "~P)*ulzNT9E@4JlJIuPB")
object AppConfig {

    var deviceId: String by serialLazy("")
    var keyTimeOut: Int by serialLazy(app.desty.chat_admin.common.constants.DestyConstants.httpTimeout)

    var currentLanguage: Language by serialLazy(Language.English)

    fun init() {
        if (BuildConfig.desty_debug) {
            changeLanguage(currentLanguage)
        } else {
            changeLanguage(Language.Bahasa)
        }
    }


    fun changeLanguage(language: Language): Locale {
        val locale = when (language) {
            Language.English -> Locale.ENGLISH
            Language.Chinese -> Locale.CHINESE
            Language.Code    -> Locale("es")
            else             -> Locale("id")
        }
        this.currentLanguage = language
        if (!LanguageUtils.isAppliedLanguage(locale)) {
            LanguageUtils.applyLanguage(locale, false)
        }
        return locale
    }


}

enum class Language(val apiType: String) {
    Bahasa("id-ID"),
    English("en-US"),
    Chinese("id-ID"),
    Code("id-ID")
}