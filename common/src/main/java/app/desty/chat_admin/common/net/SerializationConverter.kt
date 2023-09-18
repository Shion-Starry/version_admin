package app.desty.chat_admin.common.net

import com.drake.net.NetConfig
import com.drake.net.convert.NetConverter
import com.drake.net.exception.ConvertException
import com.drake.net.exception.RequestParamsException
import com.drake.net.exception.ServerResponseException
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type

class SerializationConverter(
    val success: Int = 200,
    val code: String = "code",
    val message: String = "message",
                            ) : NetConverter {

    companion object {
        val jsonDecoder = Json {
            ignoreUnknownKeys = true // 数据类可以不用声明Json的所有字段
            coerceInputValues = true // 如果Json字段是Null则使用数据类字段默认值
        }
    }

    override fun <R> onConvert(succeed: Type, response: Response): R? {
        val code = response.code
        when {
            code in 200..299 -> { // 请求成功
                val bodyString = response.body?.string() ?: return null
                return try {
                    val json = JSONObject(bodyString) // 获取JSON中后端定义的错误码和错误信息
                    when (val srvCode = json.getInt(this.code)) {
                        success -> json.getString("data").parseBody<R>(succeed)
                        401     -> throw ForceUpgradeException(body = json.getString("data"))
                        else    -> {
                            val errorMessage = json.optString(
                                message,
                                NetConfig.app.getString(com.drake.net.R.string.no_error_message)
                                                             )
                            throw ChatNetException.getExceptionByCode(
                                code = srvCode,
                                msg = errorMessage
                                                                     )
                        }
                    }
                } catch (e: JSONException) { // 固定格式JSON分析失败直接解析JSON
                    bodyString.parseBody<R>(succeed)
                }
            }

            code == 401      -> {
                throw TokenException()
            }

            code in 402..499 -> throw RequestParamsException(
                response,
                code.toString()
                                                            ) // 请求参数错误
            code >= 500      -> throw ServerResponseException(
                response,
                code.toString()
                                                             ) // 服务器异常错误
            else             -> throw ConvertException(response)
        }
    }

    fun <R> String.parseBody(succeed: Type): R? {
        return when {
            succeed === String::class.java -> {
                this as R
            }

            else                           -> jsonDecoder.decodeFromString(
                Json.serializersModule.serializer(
                    succeed
                                                 ),
                this
                                                                          ) as R
        }
    }
}