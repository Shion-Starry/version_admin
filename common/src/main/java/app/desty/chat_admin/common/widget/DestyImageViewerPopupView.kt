package app.desty.chat_admin.common.widget

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import app.desty.chat_admin.common.R
import app.desty.chat_admin.common.utils.MyToast
import app.desty.chat_admin.common.utils.MyToast.showToast
import com.lxj.xpopup.core.ImageViewerPopupView
import com.lxj.xpopup.interfaces.XPopupImageLoader
import com.lxj.xpopup.util.PermissionConstants
import com.lxj.xpopup.util.XPermission
import com.lxj.xpopup.util.XPopupUtils
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.Executors

class DestyImageViewerPopupView(context: Context) : ImageViewerPopupView(context) {
    init {
        setBgColor(Color.BLACK)
        isShowSaveButton(false)
    }

    override fun getImplLayoutId(): Int {
        return R.layout.desty_image_viewer
    }

    override fun initPopupContent() {
        super.initPopupContent()
        customView.findViewById<View>(R.id.iv_save).setOnClickListener {
            XPermission.create(context, PermissionConstants.STORAGE)
                .callback(object : XPermission.SimpleCallback {
                    override fun onGranted() {
                        saveBmpToAlbum(context, imageLoader, urls[realPosition])
                    }

                    override fun onDenied() {
                    }

                })
                .request()
        }
    }

    fun saveBmpToAlbum(context: Context, imageLoader: XPopupImageLoader, uri: Any?) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute(Runnable {
            val source = imageLoader.getImageFile(context, uri!!)
            if (source == null) {
                app.desty.chat_admin.common.utils.MyToast.showToast(R.string.save_iamge_failed)
                return@Runnable
            }
            try {
                val dir = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    context.packageName
                              )
                if (!dir.exists()) dir.mkdirs()
                val destFile = File(
                    dir,
                    System.currentTimeMillis().toString() + "." + XPopupUtils.getImageType(source)
                                   )
                if (Build.VERSION.SDK_INT < 29) {
                    if (destFile.exists()) destFile.delete()
                    destFile.createNewFile()
                    FileOutputStream(destFile).use { out ->
                        writeFileFromIS(out, FileInputStream(source))
                    }
                    val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                    intent.data = Uri.parse("file://" + destFile.absolutePath)
                    context.sendBroadcast(intent)
                } else {
                    //android10以上，增加了新字段，自己insert，因为RELATIVE_PATH，DATE_EXPIRES，IS_PENDING是29新增字段
                    val contentValues = ContentValues()
                    contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, destFile.name)
                    contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/*")
                    val contentUri =
                        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        } else {
                            MediaStore.Images.Media.INTERNAL_CONTENT_URI
                        }
                    contentValues.put(
                        MediaStore.Images.Media.RELATIVE_PATH,
                        Environment.DIRECTORY_DCIM + "/" + context.packageName
                                     )
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 1)
                    val uri = context.contentResolver.insert(contentUri, contentValues)
                    if (uri == null) {
                        app.desty.chat_admin.common.utils.MyToast.showToast(R.string.save_iamge_failed)
                        return@Runnable
                    }
                    val resolver = context.contentResolver
                    resolver.openOutputStream(uri).use { out ->
                        writeFileFromIS(
                            out,
                            FileInputStream(
                                source
                                           )
                                       )
                    }
                    // Everything went well above, publish it!
                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    //                            contentValues.putNull(MediaStore.MediaColumns.DATE_EXPIRES);
                    resolver.update(uri, contentValues, null, null)
                }
                app.desty.chat_admin.common.utils.MyToast.showToast(R.string.save_image_success)
            } catch (e: Exception) {
                e.printStackTrace()
                app.desty.chat_admin.common.utils.MyToast.showToast(R.string.save_iamge_failed)
            }
        })
    }

    private fun writeFileFromIS(fos: OutputStream?, `is`: InputStream): Boolean {
        var os: OutputStream? = null
        return try {
            os = BufferedOutputStream(fos)
            val data = ByteArray(8192)
            var len: Int
            while (`is`.read(data, 0, 8192).also { len = it } != -1) {
                os.write(data, 0, len)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}