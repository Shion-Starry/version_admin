package app.desty.chat_admin.common.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.BubbleAttachPopupView;

import app.desty.chat_admin.common.R;

/**
 * Description: 自定义气泡Attach弹窗
 * Create by lxj, at 2019/3/13
 */
public class CustomBubbleAttachPopup extends BubbleAttachPopupView {
    private String content;

    public CustomBubbleAttachPopup(@NonNull Context context, String str) {
        super(context);
        this.content = str;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_bubble_attach_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        final TextView tv = findViewById(R.id.tv);
        tv.setText(content);

    }

}
