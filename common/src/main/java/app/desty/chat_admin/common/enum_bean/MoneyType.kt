package app.desty.chat_admin.common.enum_bean

/**
 * @author xiaoke.lin
 * @date 2022/3/7
 */
enum class MoneyType(
    var unit: String, var currency: String
                    ) {
    Rupiah("Rp", "IDR"),//印尼卢比
    ;
}