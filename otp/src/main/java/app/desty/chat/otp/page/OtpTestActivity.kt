package app.desty.chat.otp.page

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import app.desty.chat.otp.OtpUtils
import app.desty.chat_admin.otp.R

class OtpTestActivity : AppCompatActivity() {
    lateinit var etOtp: EditText
    lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_test)
        etOtp = findViewById(R.id.et_otp)
        tvResult = findViewById(R.id.tv_result)
        findViewById<View>(R.id.b_verify).setOnClickListener {
            tvResult.text = if (OtpUtils.verify(etOtp.text.toString()))
                "success" else "failed"
        }
    }
}