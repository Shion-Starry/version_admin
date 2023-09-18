package app.desty.chat_admin.common.widget.base

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.AttributeSet
import android.widget.ImageView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.databinding.BindingAdapter
import app.desty.chat_admin.common.widget.DestyImageViewerPopupView
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.Transition
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.util.ImageDownloadTarget
import com.lxj.xpopup.util.SmartGlideImageLoader
import java.io.File
import kotlin.math.max

open class BaseImageView : ImageFilterView {

    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
                                                                )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
               ) : super(context!!, attrs, defStyleAttr)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        loadUrlImage()
    }

    private val MAX_SIZE_DEFAULT = 2000
    var urlKey: String? = null
    var url: String? = null
    var imageMessage: String? = null
    var requestOptions: RequestOptions? = null
    var centerCrop: Boolean = false
    var maxSize = MAX_SIZE_DEFAULT

    private fun loadUrlImage() {
        url?.apply {
            if (width == 0 || height == 0) return
            if (centerCrop &&
                (contains("static.desty.app")
                        || contains("static.desty.page")
                        || contains("desty-upload-indonesia.oss-ap-southeast-5.aliyuncs.com"))
            ) {
                val builder = Uri.parse(url).buildUpon()
                builder.appendQueryParameter(
                    "x-oss-process",
                    StringUtils.format(
                        "image/resize,m_fill,w_%d,h_%d",
                        width,
                        height
                                      )
                                            )
                val url1 = builder.build().toString()
                loadImage(this@BaseImageView, requestOptions, url1)
                return
            }
            loadImage(this@BaseImageView, requestOptions, url)
            return
        }
        imageMessage?.apply {
            var g = Glide.with(context)
                .downloadOnly()
                .load(imageMessage)
            requestOptions?.apply {
                g = g.apply(this)
            }
            g.into(object : ImageDownloadTarget() {
                override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                    val filePath = resource.path
                    val options = BitmapFactory.Options()
                    options.inJustDecodeBounds = true
                    BitmapFactory.decodeFile(filePath, options)

                    val bmpWidth = options.outWidth
                    val bmpHeight = options.outHeight

                    var gifOptions = requestOptions ?: (RequestOptions()
                        .priority(Priority.HIGH)
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                    val maxSide = max(bmpHeight, bmpWidth)
                    gifOptions = if (maxSide > maxSize) {
                        val scale = maxSide / maxSize
                        gifOptions.override(bmpWidth / scale, bmpHeight / scale)
                    } else {
                        gifOptions.override(SIZE_ORIGINAL, SIZE_ORIGINAL)
                    }
                    Glide.with(context)
                        .load(resource)
                        //监听器是RequestListener<GifDrawable>类型的，其中控制了加载进度条——loadingProgress的隐藏
//                            .listener(getGifRequestListener(loadingProgress))
                        .apply(gifOptions)
                        //imageView即 PhotoView对象
                        .into(this@BaseImageView)

                }
            })
        }
    }

    private fun loadImage(view: ImageView, requestOptions: RequestOptions?, url: String?) {
        if (!isValidContextForGlide(view.context)) return
        var load = Glide.with(view).load(url).timeout(6000)
        if (requestOptions != null) {
            load = load.apply(requestOptions)
        }
        load.into(view)
    }

    /**
     * 校验activity是否已经结束
     */
    private fun isValidContextForGlide(context: Context?): Boolean {
        if (context == null) {
            return false
        }
        if (context is Activity) {
            val activity = context
            if (activity.isDestroyed || activity.isFinishing) {
                return false
            }
        }
        return true
    }

    companion object {

        @BindingAdapter(
            value = ["urlKey", "url", "originalUrl", "option", "centerCrop", "maxSize"],
            requireAll = false
                       )
        @JvmStatic
        fun setUrlImage(
            baseImageView: BaseImageView,
            urlKey: String?,
            url: String?,
            originalUrl: String?,
            requestOptions: RequestOptions?,
            centerCrop: Boolean?,
            maxSize: Int?,
                       ) {
            if (urlKey != null) {
                if (baseImageView.urlKey != urlKey) baseImageView.setImageDrawable(null)
            } else {
                baseImageView.setImageDrawable(null)
            }
            baseImageView.urlKey = urlKey
            baseImageView.url = url?.trim()
            baseImageView.imageMessage = originalUrl?.trim()
            baseImageView.requestOptions = requestOptions
            baseImageView.centerCrop = centerCrop == true
            baseImageView.maxSize =
                if (maxSize == null) baseImageView.MAX_SIZE_DEFAULT else SizeUtils.dp2px(maxSize.toFloat())
            baseImageView.loadUrlImage()
        }

        @BindingAdapter(value = ["clickToView"])
        @JvmStatic
        fun setClickToView(
            baseImageView: BaseImageView,
            clickToView: Boolean?,
                          ) {
            if (clickToView == true) {
                baseImageView.setOnClickListener {
                    XPopup.Builder(baseImageView.context)
                        .asCustom(
                            DestyImageViewerPopupView(baseImageView.context)
                                .setSingleSrcView(
                                    baseImageView,
                                    baseImageView.imageMessage ?: baseImageView.url
                                                 )
                                .setXPopupImageLoader(SmartGlideImageLoader())
                                 )
                        .show()
                }
            } else {
                baseImageView.setOnClickListener(null)
            }
        }
    }

}