package app.desty.chat_admin.common.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.BubbleHorizontalAttachPopupView;

import app.desty.chat_admin.common.R;


/**
 * Description: 自定义Attach弹窗，水平方向的带气泡的弹窗
 * Create by lxj, at 2019/3/13
 */
public class CustomHorizontalBubbleAttachPopup extends BubbleHorizontalAttachPopupView {
    private String content;

    public CustomHorizontalBubbleAttachPopup(@NonNull Context context, String str) {
        super(context);
        this.content = str;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_attach_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        getPopupImplView().setBackgroundResource(0);
        ((TextView) findViewById(R.id.tv_zan)).setText(content);

    }

}
