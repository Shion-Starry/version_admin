package app.desty.chat_admin.common.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.PathUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import app.desty.chat_admin.common.aop.annotation.UseMainThread;
import app.desty.chat_admin.common.aop.annotation.UseMainThread;

public class FileUtil {

    /**
     * 发送广播，扫描文件夹加入相册
     *
     * @param context
     */
    public static void scanMediaDir(Context context, Uri uri) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            scanIntent.setData(uri);
            context.sendBroadcast(scanIntent);
        }
    }

    /**
     * 将缓存的文件拷贝至Download文件夹
     *
     * @param context         上下文
     * @param cacheFile       待拷贝的文件
     * @param relativeDirPath 相对路径，要以Download开始
     * @return 是否成功
     */
    @UseMainThread(false)
    public static boolean copyCacheToDownload(Context context, File cacheFile,
                                              String relativeDirPath) {
        FileInputStream inputStream = null;
        OutputStream downloadOutputStream = null;
        try {
            inputStream          = new FileInputStream(cacheFile);
            downloadOutputStream = getDownloadOutputStream(context, relativeDirPath, cacheFile.getName());
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                downloadOutputStream.write(buffer, 0, bytesRead);
                downloadOutputStream.flush();
            }
        } catch (IOException ignored) {
            return false;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (downloadOutputStream != null) {
                    downloadOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    @UseMainThread(false)
    public static boolean moveCacheToDownload(Context context, File cacheFile,
                                              String relativeDirPath) {
        FileInputStream inputStream = null;
        OutputStream downloadOutputStream = null;
        try {
            inputStream          = new FileInputStream(cacheFile);
            downloadOutputStream = getDownloadOutputStream(context, relativeDirPath, cacheFile.getName());
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                downloadOutputStream.write(buffer, 0, bytesRead);
                downloadOutputStream.flush();
            }
        } catch (IOException ignored) {
            return false;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (downloadOutputStream != null) {
                    downloadOutputStream.close();
                }
                FileUtils.delete(cacheFile);//删掉缓存文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    public static OutputStream getDownloadOutputStream(Context context,
                                                       String relativeDirPath,
                                                       String fileName) throws FileNotFoundException {
        if (!relativeDirPath.endsWith("/")) {
            relativeDirPath = relativeDirPath + "/";
        }
        OutputStream outputStream = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // 将要新建的文件的文件索引插入到 external.db 数据库中
            // 需要插入到 external.db 数据库 files 表中, 这里就需要设置一些描述信息
            ContentValues contentValues = new ContentValues();
            // 设置插入 external.db 数据库中的 files 数据表的各个字段的值
            // 设置存储路径 , files 数据表中的对应 relative_path 字段在 MediaStore 中以常量形式定义
            contentValues.put(MediaStore.Downloads.RELATIVE_PATH, relativeDirPath);
            // 设置文件名称
            contentValues.put(MediaStore.Downloads.DISPLAY_NAME, fileName);

            Uri outUri = insertDownloadUnique(context.getApplicationContext().getContentResolver(),
                                              MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL),
                                              contentValues);
            outputStream = context.getApplicationContext().getContentResolver().openOutputStream(outUri);
        } else {
            String dirName = PathUtils.join(Environment.getExternalStorageDirectory().getAbsolutePath(), relativeDirPath);
            File newFile = new File(dirName);
            if (!newFile.exists()) {
                newFile.mkdirs();
            }
            File file1 = new File(newFile, fileName);
            file1.deleteOnExit();
            outputStream = new FileOutputStream(file1);
        }
        return outputStream;
    }

    public static Uri getDownloadUri(Context context, String relativeDirPath, String displayName) {
        if (!relativeDirPath.endsWith("/")) {
            relativeDirPath = relativeDirPath + "/";
        }
        Uri result = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            String selection = MediaStore.Downloads.DISPLAY_NAME + " = ? and " +
                    MediaStore.Downloads.RELATIVE_PATH + " = ?";
            String[] selectionArgs = new String[]{displayName, relativeDirPath};

            Cursor c = context.getApplicationContext().getContentResolver()
                              .query(MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL), null, selection, selectionArgs, null);

            if (c != null && c.moveToFirst()) {
                long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
                result = ContentUris.withAppendedId(MediaStore.Downloads.EXTERNAL_CONTENT_URI, id);
                c.close();
            }
        } else {
            result = Uri.fromFile(
                    new File(Environment.getExternalStorageDirectory(),
                             PathUtils.join(relativeDirPath, displayName)));
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static Uri insertDownloadUnique(ContentResolver cr,
                                            Uri uri, ContentValues cv) {
        String selection = MediaStore.Downloads.DISPLAY_NAME + " = ? and " +
                MediaStore.Downloads.RELATIVE_PATH + " = ?";
        String[] selectionArgs = new String[]{
                (String) cv.get(MediaStore.Downloads.DISPLAY_NAME),
                (String) cv.get(MediaStore.Downloads.RELATIVE_PATH)};

        Cursor c = cr.query(uri, null, selection, selectionArgs, null);

        Uri result;
        if (c != null && c.moveToFirst()) {
            long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
            result = ContentUris.withAppendedId(MediaStore.Downloads.EXTERNAL_CONTENT_URI, id);
            cr.update(result, cv, selection, selectionArgs);
            c.close();
        } else {
            result = cr.insert(uri, cv);
        }
        return result;
    }

    public static Pair<OutputStream, Uri> getImageOutputStream(Context context,
                                                               String relativeDirPath,
                                                               String fileName) throws FileNotFoundException {
        if (!relativeDirPath.endsWith("/")) {
            relativeDirPath = relativeDirPath + "/";
        }
        Pair<OutputStream, Uri> result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // 将要新建的文件的文件索引插入到 external.db 数据库中
            // 需要插入到 external.db 数据库 files 表中, 这里就需要设置一些描述信息
            ContentValues contentValues = new ContentValues();
            // 设置插入 external.db 数据库中的 files 数据表的各个字段的值
            // 设置存储路径 , files 数据表中的对应 relative_path 字段在 MediaStore 中以常量形式定义
            contentValues.put(MediaStore.Images.ImageColumns.RELATIVE_PATH, relativeDirPath);
            // 设置文件名称
            contentValues.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, fileName);

            Uri outUri = insertImageUnique(context.getApplicationContext().getContentResolver(),
                                           MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL),
                                           contentValues);
            OutputStream outputStream = context.getApplicationContext().getContentResolver().openOutputStream(outUri);
            result = new Pair<>(outputStream, outUri);
        } else {
            String dirName = PathUtils.join(Environment.getExternalStorageDirectory().getAbsolutePath(), relativeDirPath);
            File newFile = new File(dirName);
            if (!newFile.exists()) {
                newFile.mkdirs();
            }
            File file1 = new File(newFile, fileName);
            file1.deleteOnExit();
            FileOutputStream outputStream = new FileOutputStream(file1);
            result = new Pair<>(outputStream, Uri.fromFile(file1));
        }
        return result;
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static Uri insertImageUnique(ContentResolver cr,
                                         Uri uri, ContentValues cv) {
        String selection = MediaStore.Images.ImageColumns.DISPLAY_NAME + " = ? and " +
                MediaStore.Images.ImageColumns.RELATIVE_PATH + " = ?";
        String[] selectionArgs = new String[]{
                (String) cv.get(MediaStore.Images.ImageColumns.DISPLAY_NAME),
                (String) cv.get(MediaStore.Images.ImageColumns.RELATIVE_PATH)};

        Cursor c = cr.query(uri, null, selection, selectionArgs, null);

        Uri result;
        if (c != null && c.moveToFirst()) {
            long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
            result = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            cr.update(result, cv, selection, selectionArgs);
            c.close();
        } else {
            result = cr.insert(uri, cv);
        }
        return result;
    }

    public static boolean checkFileSize(Context context, Uri fileUri, long sizeLimit) {
        try (InputStream inputStream = context.getContentResolver().openInputStream(fileUri)) {
            long size = inputStream.available();
            Log.i("FileUtil", fileUri.toString() + ":" + size);
            return size <= sizeLimit;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * assetsFile读取为string
     *
     * @param context  上下文
     * @param fileName assets 文件名
     * @return 文本
     */
    public static String assetsFile2Str(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        AssetManager assets = context.getAssets();
        try (InputStreamReader inputStreamReader = new InputStreamReader(assets.open(fileName))) {
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
