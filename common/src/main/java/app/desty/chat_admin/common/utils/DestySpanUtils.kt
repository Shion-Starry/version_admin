package app.desty.chat_admin.common.utils

import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.annotation.ColorInt

/**
 * @author xiaoke.lin
 * @date 2022/3/11
 */
class DestySpanUtils {
    //拼接彩色span
    class Builder {
        val builder = SpannableStringBuilder()
        fun append(string: String) {
            val textSpanned = SpannableString(string)
            builder.append(textSpanned)
        }

        fun append(string: String, @ColorInt textColor: Int) {
            append(string, textColor, 0f)
        }

        fun append(string: String, @ColorInt textColor: Int, relativeSize: Float) {
            val textSpanned = SpannableString(string)
            if (relativeSize != 0f) {
                textSpanned.setSpan(
                    RelativeSizeSpan(relativeSize),
                    0, string.lastIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE
                                   )
            }
            textSpanned.setSpan(
                ForegroundColorSpan(textColor),
                0, string.lastIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE
                               )
            builder.append(textSpanned)
        }

        fun build(): SpannableStringBuilder {
            return builder
        }
    }

    companion object {

        /**
         * 根据匹配的字符串把命中文字转为所需颜色
         */
        @JvmStatic
        fun getLightSpan(
            original: String,
            vararg target: String?,
            @ColorInt textColor: Int
                        ): CharSequence {
            val originalLower = original.lowercase()
            val textSpanned = SpannableStringBuilder(original)
            for (s in target) {
                if (s == null) continue
                val stringLower = s.trim().lowercase()
                if (TextUtils.isEmpty(stringLower)) continue
                if (!originalLower.contains(stringLower)) continue
                match(textSpanned, originalLower, 0, stringLower) {
                    arrayOf(ForegroundColorSpan(textColor))
                }
            }
            return textSpanned
        }


        /**
         * 根据匹配的字符串把命中文字转为所需颜色
         */
        @JvmStatic
        fun getBoldSpan(
            original: String,
            vararg target: String?
                       ): CharSequence {
            val originalLower = original.lowercase()
            val textSpanned = SpannableStringBuilder(original)
            for (s in target) {
                if (s == null) continue
                val stringLower = s.trim().lowercase()
                if (TextUtils.isEmpty(stringLower)) continue
                if (!originalLower.contains(stringLower)) continue
                match(textSpanned, originalLower, 0, stringLower) {
                    arrayOf(StyleSpan(android.graphics.Typeface.BOLD))
                }
            }
            return textSpanned
        }


        @JvmStatic
        fun getCustomSpan(
            original: String,
            vararg target: String,
            spanType: () -> Any,
                         ): CharSequence = getCustomSpans(original, *target) {
            arrayOf(spanType())
        }


        @JvmStatic
        fun getCustomSpans(
            original: CharSequence,
            vararg target: String,
            spanType: () -> Array<Any>,
                         ): CharSequence {
            val originalLower = original.toString().lowercase()
            val textSpanned = SpannableStringBuilder(original)

            for (s in target) {
                val stringLower = s.trim().lowercase()
                if (TextUtils.isEmpty(stringLower)) continue
                if (!originalLower.contains(stringLower)) continue
                match(textSpanned, originalLower, 0, stringLower, spanType)
            }
            return textSpanned
        }

        private fun match(
            original: SpannableStringBuilder,
            originalStr: String,
            index: Int,
            target: String,
            spanTypes: () -> Array<Any>
                         ): SpannableStringBuilder {
            return if (!originalStr.substring(index).contains(target)) {
                original
            } else {
                val startIndex = originalStr.substring(index).indexOf(target)
                val endIndex = startIndex + target.length
                spanTypes().forEach {
                    original.setSpan(
                        it,
                        index + startIndex,
                        index + endIndex,
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE
                                    )
                }
                match(original, originalStr, index + endIndex, target, spanTypes)
            }
        }
    }
}