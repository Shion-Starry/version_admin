package app.desty.chat_admin.common.base;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import app.desty.chat_admin.common.R;


public abstract class BaseBottomDialogFragment extends BaseDialogFragment {

    protected final int WRAP  = ViewGroup.LayoutParams.WRAP_CONTENT;
    protected final int MATCH = ViewGroup.LayoutParams.MATCH_PARENT;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.ActionSheetDialogStyle);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            //设置 dialog 的宽高
            getDialog().getWindow().setLayout(getWidth(), getHeight());
        }
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        if (getDialog() != null) {
            //去除标题栏
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

            Window window = getDialog().getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.BOTTOM; //底部
            int h = getHeight();
            int w = getWidth();
            if (h == WRAP) {
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            } else if (h == MATCH) {
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            } else {
                lp.height = h;
            }
            if (w == WRAP) {
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            } else if (w == MATCH) {
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            } else {
                lp.width = w;
            }
            window.setAttributes(lp);
        }
        return onMyCreateView(inflater, container, savedInstanceState);
    }

    public abstract int getHeight();

    public abstract int getWidth();

    public abstract View onMyCreateView(@NonNull LayoutInflater inflater,
                                        @Nullable ViewGroup container,
                                        @Nullable Bundle savedInstanceState);
}
