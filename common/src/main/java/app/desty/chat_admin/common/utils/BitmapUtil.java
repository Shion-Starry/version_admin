package app.desty.chat_admin.common.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;

import com.blankj.utilcode.util.FileUtils;

import java.io.FileNotFoundException;
import java.io.OutputStream;


public class BitmapUtil {
    /**
     * 保存文件，文件名为当前日期
     */
    public static void saveBitmap(Bitmap bitmap, String tableNumber, Activity activity) {
        try {
            try {
                Pair<OutputStream, Uri> output = getOutputStream(activity, tableNumber);
                OutputStream outputStream = output.first;
                Uri outputUri = output.second;
                //这个100表示压缩比,100说明不压缩,90说明压缩到原来的90%
                //注意:这是对于占用存储空间而言,不是说占用内存的大小
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
                FileUtil.scanMediaDir(activity, outputUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap createBitmap(View v, int width, int height) {
        //测量使得view指定大小
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        v.measure(measuredWidth, measuredHeight);
        //调用layout方法布局后，可以得到view的尺寸大小
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        c.drawColor(Color.WHITE);
        v.draw(c);
        return bitmap;
    }


    public static Pair<OutputStream, Uri> getOutputStream(Activity activity,
                                                          String tableNumber) throws FileNotFoundException {
        tableNumber = FileUtils.filterFileName(tableNumber);
//        String storeName = FileUtils.filterFileName(ObjectUtils.getOrDefault(UserConfig.getStoreInfo().getName(), "desty"));
        String relativePath = "Pictures/desty/chat" ;//相对路径
        //图片的名称
        String fileName;
        if (TextUtils.isEmpty(tableNumber)) {
            fileName = System.currentTimeMillis() + ".png";
        } else {
            fileName = tableNumber + ".png";
        }
        return FileUtil.getImageOutputStream(activity, relativePath, fileName);
    }
}
