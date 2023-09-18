package app.desty.chat_admin.common.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author xiaoke.lin
 * @date 2022/3/4
 */
@Parcelize
data class TopItemBean(
    var titleTextRes: Int,//文字资源
    var count:Int,//数量
    var isSelected: Boolean,//是否被选中
                      ) : Parcelable