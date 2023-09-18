package app.desty.chat_admin.common.utils

object EnumUtil {
    fun <T:Enum<*>> max(a: T?, b: T?): T? {
        return if (a == null) b
        else {
            if (b == null) a
            else {
                if (a.ordinal > b.ordinal) a
                else b
            }
        }
    }
    fun <T:Enum<*>> min(a: T?, b: T?): T? {
        return if (a == null) b
        else {
            if (b == null) a
            else {
                if (a.ordinal < b.ordinal) a
                else b
            }
        }
    }
}