package app.desty.chat_admin.common.utils

import android.text.Editable

object NumberUtil {

    fun setInputRangeRules(editable: Editable, min: Int, max: Int) {
        val content = editable.toString()

        if (content.isNotEmpty()) {
            if (content.startsWith("-")) {
                return
            } else if (content.length > 1 && content.startsWith("0")) {
                editable.replace(0, 1, "")
                return
            } else if (content.length > 2 && content.startsWith("-0")) {
                editable.replace(1, 2, "")
                return
            }

            val num: Int = try {
                content.toInt()
            } catch (e: NumberFormatException) {
                return
            }

            if (max > min) {
                if (num > max) {
                    MyToast.showToast("You've reached the maximum limit.")
                    editable.clear()
                    editable.append(max.toString())
                } else if (num < min) {
                    MyToast.showToast("You've reached the minimum limit.")
                    editable.clear()
                    editable.append(min.toString())
                }
            }
        }
    }

    fun numbersOf1(a: Int): Int {
        var n = a
        var count = 0
        while (n != 0) {
            ++count
            n = n - 1 and n
        }
        return count
    }

}