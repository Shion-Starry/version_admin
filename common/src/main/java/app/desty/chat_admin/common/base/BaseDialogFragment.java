package app.desty.chat_admin.common.base;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import app.desty.chat_admin.common.R;

public class BaseDialogFragment extends DialogFragment {
    protected AppCompatActivity mActivity;
    private FragmentDialogDismissCallback fragmentDialogDismissCallback;
    private boolean isShowing = false;
    /**
     * 获取context
     */
    @Override
    public Context getContext() {
        return mActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, R.style.dialog_style);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        super.show(manager, tag);
        isShowing = true;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        isShowing = false;
        if (fragmentDialogDismissCallback != null) {
            fragmentDialogDismissCallback.onDismiss();
        }
    }

    public boolean isShowing() {
        return isShowing;
    }

    public interface FragmentDialogDismissCallback{
        void onDismiss();
    }

    public void setFragmentDialogDismissCallback(FragmentDialogDismissCallback fragmentDialogDismissCallback) {
        this.fragmentDialogDismissCallback = fragmentDialogDismissCallback;
    }
}
