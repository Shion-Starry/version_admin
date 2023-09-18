package app.desty.chat_admin.common.widget

import app.desty.chat_admin.common.R
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

object GlideRequestOptions {

    val buyerAvatarOptions: RequestOptions = RequestOptions()
        .placeholder(R.drawable.ic_default_avatar)
        .error(R.drawable.ic_default_avatar)
        .diskCacheStrategy(DiskCacheStrategy.ALL)

    val sellerAvatarOptions: RequestOptions = RequestOptions()
        .placeholder(R.drawable.ic_default_avatar)
        .error(R.drawable.ic_default_avatar)
        .diskCacheStrategy(DiskCacheStrategy.ALL)

    fun getBuyerOrSellerAvatarOptions(isBuyer: Boolean) =
        if (isBuyer) buyerAvatarOptions else sellerAvatarOptions

    val stickerGroupOptions: RequestOptions = RequestOptions()
        .placeholder(R.drawable.ic_sticker_placeholder)
        .error(R.drawable.ic_sticker_placeholder)
        .diskCacheStrategy(DiskCacheStrategy.ALL)

    val stickerOptions: RequestOptions = RequestOptions()
        .placeholder(R.drawable.ic_sticker_placeholder)
        .error(R.drawable.ic_sticker_placeholder)
        .diskCacheStrategy(DiskCacheStrategy.ALL)

    val productOptions: RequestOptions = RequestOptions()
        .placeholder(R.drawable.ic_product_placeholder)
        .error(R.drawable.ic_product_placeholder)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
}