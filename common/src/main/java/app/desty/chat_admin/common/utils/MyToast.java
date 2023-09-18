package app.desty.chat_admin.common.utils;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;

import androidx.annotation.StringRes;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;

import app.desty.chat_admin.common.R;
import app.desty.sdk.logcat.Logcat;

public class MyToast {
    /**
     * 弹出Toast，线程安全
     *
     * @param text
     */
    public static void showToast(String text, Object... objects) {
        originalShowToast(null, StringUtils.format(text, objects));
    }

    public static void showToast(@StringRes int textResId, Object... objects) {
        originalShowToast(null, StringUtils.getString(textResId, objects));
    }

    public static void showToastWithCode(String code, String text, Object... objects) {
        originalShowToast(code, StringUtils.format(text, objects));
    }

    public static void showToastWithCode(String code, @StringRes int textResId, Object... objects) {
        originalShowToast(code, StringUtils.getString(textResId, objects));
    }


    private static void originalShowToast(String code, String text) {
        if (text.isEmpty()) return;
        String finalText = text;
        if (!TextUtils.isEmpty(code)) {
            finalText = StringUtils.format("%s[%s]", text, code);
        }
        Logcat.i("ShowToast:" + finalText);
        ToastUtils.getDefaultMaker()
                  .setGravity(Gravity.CENTER, 0, 0)
                  .setBgResource(R.drawable.bg_black_toast)
                  .setTextSize(14)
                  .setTextColor(Color.WHITE)
                  .setTypeface(FontUtil.getFontTypefaceByName("font/regular_font.ttf"))
                  .setDurationIsLong(false)
                  .show(finalText);
    }

    public static void showToastLong(String text, Object... objects) {
        ToastUtils.getDefaultMaker()
                  .setGravity(Gravity.CENTER, 0, 0)
                  .setBgResource(R.drawable.bg_black_toast)
                  .setTextSize(14)
                  .setTextColor(Color.WHITE)
                  .setTypeface(FontUtil.getFontTypefaceByName("font/regular_font.ttf"))
                  .setDurationIsLong(true)
                  .show(StringUtils.format(text, objects));
    }

    public static void showToastLong(@StringRes int textResId, Object... objects) {
        ToastUtils.getDefaultMaker()
                  .setGravity(Gravity.CENTER, 0, 0)
                  .setBgResource(R.drawable.bg_black_toast)
                  .setTextSize(14)
                  .setTextColor(Color.WHITE)
                  .setTypeface(FontUtil.getFontTypefaceByName("font/regular_font.ttf"))
                  .setDurationIsLong(true)
                  .show(StringUtils.getString(textResId, objects));
    }

}
