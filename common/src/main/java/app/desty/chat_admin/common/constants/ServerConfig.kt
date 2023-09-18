package app.desty.chat_admin.common.constants

import app.desty.chat_admin.common.bean.Config
import app.desty.chat_admin.common.config.AppConfig
import app.desty.chat_admin.common.enum_bean.ServerConfigNameEnum

object ServerConfig {

    var destyWhatsAppNumber = app.desty.chat_admin.common.constants.DestyConstants.destyWhatsAppNumber
    var destyWhatsAppMessage = app.desty.chat_admin.common.constants.DestyConstants.destyWhatsAppMessage
    var pullMessagePageSize = app.desty.chat_admin.common.constants.DestyConstants.pullMessagePageSize

    fun initConfig(configs: List<Config>?) {
        configs?.forEach {
            when (it.name) {
                ServerConfigNameEnum.DESTY_WHATS_APP_NUMBER  -> destyWhatsAppNumber =
                    it.value ?: app.desty.chat_admin.common.constants.DestyConstants.destyWhatsAppNumber

                ServerConfigNameEnum.DESTY_WHATS_APP_MESSAGE -> destyWhatsAppMessage =
                    it.value ?: destyWhatsAppMessage

                ServerConfigNameEnum.HTTP_TIME_OUT           -> {
                    AppConfig.keyTimeOut =
                        it.value?.toIntOrNull() ?: app.desty.chat_admin.common.constants.DestyConstants.httpTimeout
                }

                else                                         -> {}
            }
        }
    }

    override fun toString(): String {
        return "ServerConfig:" +
                "\n\tdestyWhatsAppNumber:$destyWhatsAppNumber" +
                "\n\tdestyWhatsAppMessage:$destyWhatsAppMessage"
    }
}