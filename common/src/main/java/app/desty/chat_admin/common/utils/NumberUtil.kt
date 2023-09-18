package app.desty.chat_admin.common.utils

object NumberUtil {

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