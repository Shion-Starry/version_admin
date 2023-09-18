package app.desty.chat_admin.common.utils;

import android.graphics.Typeface;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import app.desty.sdk.logcat.Logcat;


public class FontUtil {
    private static final Map<String, Typeface> fontMap = new HashMap<>();

    private static Typeface getFontTypefaceFromAssert(String fontName) {
        try {
            synchronized (fontMap) {
                if (!fontMap.containsKey(fontName)) {
                    Typeface typeFace = Typeface.createFromAsset(ActivityLifecycleManager.getInstance().getTopActivity().getAssets(), fontName);
                    fontMap.put(fontName, typeFace);
                }
                return fontMap.get(fontName);
            }
        } catch (Exception e) {
            Logcat.e(e);
            return null;
        }
    }

    public static Typeface getFontTypefaceByName(String fontName) {
        return getFontTypefaceFromAssert(fontName);
    }

    public static void setFount(TextView view, Boolean isBold) {
        if (view != null) {
            try {
                var fontName = (isBold != null && isBold)
                               ? "font/bold_font.ttf"
                               : "font/regular_font.ttf";
                Typeface typeface = FontUtil.getFontTypefaceByName(fontName);
                view.setTypeface(typeface);
            } catch (Exception ignore) {
            }
        }
    }
}
