package app.desty.chat_admin.common.utils


import android.media.MediaMetadataRetriever
import android.text.TextUtils
import java.io.File


/**
 * author：luck
 * project：PictureSelector
 * package：com.luck.picture.lib.config
 * email：893855882@qq.com
 * data：2017/5/24
 */
object PictureMimeType {
    fun ofAll(): Int {
        return PictureConfig.TYPE_ALL
    }

    fun ofImage(): Int {
        return PictureConfig.TYPE_IMAGE
    }

    fun ofVideo(): Int {
        return PictureConfig.TYPE_VIDEO
    }

    fun ofAudio(): Int {
        return PictureConfig.TYPE_AUDIO
    }

    fun isPictureType(pictureType: String?): Int {
        when (pictureType) {
            "image/png", "image/PNG", "image/jpeg", "image/JPEG", "image/webp", "image/WEBP", "image/gif", "image/GIF", "imagex-ms-bmp"                                             -> return PictureConfig.TYPE_IMAGE
            "video/3gp", "video/3gpp", "video/3gpp2", "video/avi", "video/mp4", "video/quicktime", "video/x-msvideo", "video/x-matroska", "video/mpeg", "video/webm", "video/mp2ts" -> return PictureConfig.TYPE_VIDEO
            "audio/mpeg", "audio/x-ms-wma", "audio/x-wav", "audio/amr", "audio/wav", "audio/aac", "audio/mp4", "audio/quicktime", "audio/3gpp"                                      -> return PictureConfig.TYPE_AUDIO
        }
        return PictureConfig.TYPE_IMAGE
    }

    /**
     * 是否是gif
     *
     * @param pictureType
     * @return
     */
    fun isGif(pictureType: String?): Boolean {
        when (pictureType) {
            "image/gif", "image/GIF" -> return true
        }
        return false
    }

    /**
     * 是否是gif
     *
     * @param path
     * @return
     */
    fun isImageGif(path: String): Boolean {
        if (!TextUtils.isEmpty(path)) {
            val lastIndex = path.lastIndexOf(".")
            val pictureType = path.substring(lastIndex, path.length)
            return (pictureType.startsWith(".gif")
                    || pictureType.startsWith(".GIF"))
        }
        return false
    }

    /**
     * 是否是视频
     *
     * @param pictureType
     * @return
     */
    fun isVideo(pictureType: String?): Boolean {
        when (pictureType) {
            "video/3gp", "video/3gpp", "video/3gpp2", "video/avi", "video/mp4", "video/quicktime", "video/x-msvideo", "video/x-matroska", "video/mpeg", "video/webm", "video/mp2ts" -> return true
        }
        return false
    }

    /**
     * 是否是网络图片
     *
     * @param path
     * @return
     */
    fun isHttp(path: String): Boolean {
        if (!TextUtils.isEmpty(path)) {
            if (path.startsWith("http")
                || path.startsWith("https")
            ) {
                return true
            }
        }
        return false
    }

    /**
     * 判断文件类型是图片还是视频
     *
     * @param file
     * @return
     */
    fun fileToType(file: File?): String {
        if (file != null) {
            val name = file.name
            if (name.endsWith(".mp4") || name.endsWith(".avi")
                || name.endsWith(".3gpp") || name.endsWith(".3gp") || name.startsWith(".mov")
            ) {
                return "video/mp4"
            } else if (name.endsWith(".PNG") || name.endsWith(".png") || name.endsWith(".jpeg")
                || name.endsWith(".gif") || name.endsWith(".GIF") || name.endsWith(".jpg")
                || name.endsWith(".webp") || name.endsWith(".WEBP") || name.endsWith(".JPEG")
            ) {
                return "image/jpeg"
            } else if (name.endsWith(".mp3") || name.endsWith(".amr")
                || name.endsWith(".aac") || name.endsWith(".war")
                || name.endsWith(".flac")
            ) {
                return "audio/mpeg"
            }
        }
        return "image/jpeg"
    }

    /**
     * is type Equal
     *
     * @param p1
     * @param p2
     * @return
     */
    fun mimeToEqual(p1: String?, p2: String?): Boolean {
        return isPictureType(p1) == isPictureType(p2)
    }

    fun createImageType(path: String?): String {
        try {
            if (!TextUtils.isEmpty(path)) {
                val file = File(path)
                val fileName = file.name
                val last = fileName.lastIndexOf(".") + 1
                val temp = fileName.substring(last, fileName.length)
                return "image/$temp"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return "image/jpeg"
        }
        return "image/jpeg"
    }

    fun createVideoType(path: String?): String {
        try {
            if (!TextUtils.isEmpty(path)) {
                val file = File(path)
                val fileName = file.name
                val last = fileName.lastIndexOf(".") + 1
                val temp = fileName.substring(last, fileName.length)
                return "video/$temp"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return "video/mp4"
        }
        return "video/mp4"
    }

    /**
     * Picture or video
     *
     * @return
     */
    fun pictureToVideo(pictureType: String): Int {
        if (!TextUtils.isEmpty(pictureType)) {
            if (pictureType.startsWith("video")) {
                return PictureConfig.TYPE_VIDEO
            } else if (pictureType.startsWith("audio")) {
                return PictureConfig.TYPE_AUDIO
            }
        }
        return PictureConfig.TYPE_IMAGE
    }

    /**
     * get Local video duration
     *
     * @return
     */
    fun getLocalVideoDuration(videoPath: String?): Int {
        val duration: Int
        duration = try {
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(videoPath)
            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!.toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }
        return duration
    }
}