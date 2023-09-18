package app.desty.chat_admin.product.bean

import android.os.Parcelable
import app.desty.chat_admin.common.R
import com.blankj.utilcode.util.StringUtils
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

@Serializable
data class PageProductList(
    val total: Int = 0,
    val size: Int = 0,
    val current: Int = 1,
    val records: List<ProductBean> = emptyList()
                          )


@Serializable
@Parcelize
data class ProductBean(
    val id: Int = 0,
    val externalSpuId: String = "",
    val imageUrl: String = "",
    val maxPrice: Double = 0.0,
    val minPrice: Double = 0.0,
    val productName: String = "",
    val productUrl: String = "",
    val skuCode: String = "",
    val status: Int = 0,
    val stock: Int = 0
                      ) : Parcelable {
    fun getShowPrice(): String {
        val min = minPrice.toBigDecimal()
        val max = maxPrice.toBigDecimal()
        val numberFormat = NumberFormat.getInstance(Locale("id")) as DecimalFormat
        return if (min.compareTo(max) == 0) {
            StringUtils.getString(R.string.product_price, numberFormat.format(min))
        } else {
            StringUtils.getString(
                R.string.product_price_range,
                numberFormat.format(min),
                numberFormat.format(max)
                                 )
        }
    }
}