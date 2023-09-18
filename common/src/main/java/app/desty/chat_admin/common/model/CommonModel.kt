package app.desty.chat_admin.common.model

import androidx.lifecycle.ViewModel
import app.desty.chat_admin.common.widget.crop.util.Log
import app.desty.sdk.logcat.Logcat
import com.drake.net.request.BodyRequest
import com.drake.net.request.MediaConst
import com.drake.net.scope.AndroidScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject


class CommonModel : ViewModel() {
    companion object {
        const val info = "ss"

        fun buildInfoBody(): RequestBody =
            "ss".toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())

        inline fun <reified T> baseJson(data: T?) {
//            val base = mapOf<String, Any?>(
//                "sign" to "",
//                "timestamp" to System.currentTimeMillis(),
//                "data" to Json.encodeToJsonElement(data)
//                                          )
//    baseJson(JSONObject(base))

            Logcat.i(Json.encodeToString(data))
            Logcat.i(Json.encodeToString(BasicData(data = data)))
        }

    }

}

@Serializable
abstract class MeBasicData<T>(
    var code: String = "",
    var msg: String = "",
    var data: T? = null
                             )

@Serializable
data class Me(
    val name: String,
             ) : MeBasicData<Me>()

@Serializable
data class BasicData<T>(
    var sign: String = "",
    var timestamp: Long = System.currentTimeMillis(),
    val data: T? = null
                       )

@Serializable
data class SubData(
    var title: String = "",
    var name: String = ""
                  )

fun BodyRequest.baseJson(baseJsonString: String) {
    this.body = baseJsonString.toRequestBody(MediaConst.JSON)
}

fun BodyRequest.baseJson(baseJson: JSONObject) = baseJson(baseJson.toString())

fun BodyRequest.baseJson(vararg params: Pair<String, Any?>) = baseJson(params.toMap())

inline fun <reified T> BodyRequest.baseJson(data: T?) {
    baseJson(Json.encodeToString(BasicData(data = data)))
}

fun BodyRequest.baseJson(dataMap: Map<String, Any?>?) {
    val base = mapOf(
        "sign" to "",
        "timestamp" to System.currentTimeMillis(),
        "data" to dataMap
                    )
    baseJson(JSONObject(base))
}


fun main() {
    AndroidScope(dispatcher = Dispatchers.IO).launch {

    }
//    print(Json.encodeToString(BasicData("1", "sd", SubData("tit", "name"))))
}