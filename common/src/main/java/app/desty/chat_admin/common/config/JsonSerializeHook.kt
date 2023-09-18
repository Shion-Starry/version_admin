package app.desty.chat_admin.common.config

import com.blankj.utilcode.util.KsonUtils
import com.drake.serialize.serialize.SerializeHook
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

/**
 * 本地序列化存储的数据结构为Json
 */
class JsonSerializeHook : SerializeHook {

    override fun <T> serialize(name: String, type: Class<T>, data: Any): ByteArray {
        return KsonUtils.executeKson {
            encodeToString(Json.serializersModule.serializer(type), data).toByteArray()
        }
    }

    override fun <T> deserialize(name: String, type: Class<T>, bytes: ByteArray): Any {
        return KsonUtils.executeKson {
            decodeFromString(Json.serializersModule.serializer(type), bytes.decodeToString())
        }
    }
}