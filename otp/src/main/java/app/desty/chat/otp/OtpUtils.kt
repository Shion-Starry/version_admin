package app.desty.chat.otp

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import dev.turingcomplete.kotlinonetimepassword.GoogleAuthenticator
import org.apache.commons.codec.binary.Base32
import java.nio.ByteBuffer

object OtpUtils {
    private lateinit var secretKey: String

    /**
     * 验证one time password
     * @return true：验证通过
     */
    fun verify(otp: String): Boolean {
        otp.toIntOrNull()?.run {
            return generateOtp() == otp
        }
        return false
    }

    fun init(context: Context, bitmapRes: Int, channel: Int, defaultKey: String) {
        secretKey = getSecret(context, bitmapRes, channel, defaultKey)
    }


    private fun getSecret(
        context: Context,
        bitmapRes: Int,
        channel: Int,
        defaultKey: String,
                         ): String {
        val options = BitmapFactory.Options()
        options.inScaled = false
        val bitmap =
            BitmapFactory.decodeResource(context.resources, bitmapRes, options)
                ?: return defaultKey
        val bytes = bitmap.byteCount
        val buffer = ByteBuffer.allocate(bytes)
        bitmap.copyPixelsToBuffer(buffer) //Move the byte data to the buffer
        val data = buffer.array() //Get the bytes array of the bitmap

        return K.b2s(data, channel) ?: defaultKey
    }

    /**
     * 根据内置密钥生成otp
     */
    private fun generateOtp(): String {
        // Warning: the length of the plain text may be limited, see next chapter
        val plainTextSecret = secretKey.toByteArray(Charsets.UTF_8)

// This is the encoded one to use in most of the generators (Base32 is from the Apache commons codec library)
        val base32EncodedSecret = Base32().encodeToString(plainTextSecret)
//        println("Base32 encoded secret to be used in the Google Authenticator app: $base32EncodedSecret")
        val googleAuthenticator = GoogleAuthenticator(base32EncodedSecret.toByteArray())
        return googleAuthenticator.generate() // Will use System.currentTimeMillis()
    }
}
