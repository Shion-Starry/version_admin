package app.desty.chat_admin.common.utils

object NumberUtil {

    fun setInputRangeRules(string: String, min: Int, max: Int): String {
        val content = StringBuilder(string)
        if (content.isNotEmpty()) {

            if (content.startsWith("-")) {
                content.replace(0, 1, "")
                return content.toString()
            } else if (content.length > 1 && content.startsWith("0")) {
                content.replace(0 , 1 , "")
                return content.toString()
            } else if (content.length > 2 && content.startsWith("-0")) {
                content.replace(1, 2, "")
                return content.toString()
            }

            val num: Int = try {
                content.toString().toInt()
            } catch (e: NumberFormatException) {
                return ""
            }

            if (max > min) {
                if (num > max) {
                    MyToast.showToast("You've reached the maximum limit")
                    return max.toString()
                } else if (num < min) {
                    MyToast.showToast("You've reached the minimum limit")
                }
            }

        }

        return ""
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