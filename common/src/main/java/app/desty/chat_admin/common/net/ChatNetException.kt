package app.desty.chat_admin.common.net

object ChatNetException {

    fun getExceptionByCode(code: Int, msg: String): Throwable =
        when (code) {
            50001 -> ImTokenNotExistsException(msg)

            else  -> BaseNetThrowable(code, msg)

        }
}


open class BaseNetThrowable(val code: Int, val msg: String) : Throwable(msg) {
}

class ForceUpgradeException(msg: String = "Force upgrade!",val body:String?) : BaseNetThrowable(401, msg)
class TokenException(msg: String = "Token expired!") : BaseNetThrowable(401, msg)
class ImTokenNotExistsException(msg: String = "Token expired!") : BaseNetThrowable(50001, msg)