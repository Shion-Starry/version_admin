package app.desty.chat_admin.common.utils;

import android.view.View;

public abstract class QuickOnClick implements View.OnClickListener {
    public long CLICK_INTERVAL = 500;
    private long time = 0;

    public abstract void click(View v);

    public void setWaitTime(Long waitTime) {
        if (waitTime != null && waitTime >= 0) {
            CLICK_INTERVAL = waitTime;
        }
    }

    @Override
    public void onClick(View v) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - time > CLICK_INTERVAL) {
            click(v);
            time = nowTime;
        }
    }
}
