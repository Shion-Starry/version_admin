package app.desty.chat_admin.common.binding_adapter;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

public class ProxyDrawable extends StateListDrawable {

    private Drawable originDrawable;

    @Override
    public void addState(int[] stateSet, Drawable drawable) {
        if (stateSet != null && stateSet.length == 1 && stateSet[0] == 0) {
            originDrawable = drawable;
        }
        super.addState(stateSet, drawable);
    }

    public Drawable getOriginDrawable() {
        return originDrawable;
    }
}