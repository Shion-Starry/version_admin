package app.desty.chat_admin.common.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SizeUtils;

import java.lang.reflect.ParameterizedType;

import app.desty.chat_admin.common.utils.MyToast;
import app.desty.chat_admin.common.widget.LoadingDialog;
import app.desty.chat_admin.common.utils.MyToast;
import app.desty.chat_admin.common.widget.LoadingDialog;


public abstract class BaseVMFragment<Q extends BaseVM> extends Fragment {
    protected AppCompatActivity mActivity;
    private ViewDataBinding mBinding;
    protected Q mState;

    private Dialog loadingDialog;

    private ViewModelProvider mFragmentProvider;
    private ViewModelProvider mParentFragmentProvider;
    private ViewModelProvider mActivityProvider;
    private ViewModelProvider mApplicationProvider;


    protected abstract void initViewModel();

    protected abstract DataBindingConfig getDataBindingConfig();

    protected abstract void init(Bundle savedInstanceState);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);//注解注入传参
        if (mState == null) {
            mState = getFragmentScopeViewModel((Class<Q>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        }
        initViewModel();
        initBaseObservable(mState);
        init(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
        if (mState == null) {
            mState = getFragmentScopeViewModel((Class<Q>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        }
        mState.getStatusBarHeight().setValue(SizeUtils.px2dp(BarUtils.getStatusBarHeight()));
        mState.getStatusBarHeightPx().setValue(BarUtils.getStatusBarHeight());

//        mState.navigationBarHeight.setValue(StatusBarUtil.getNavigationBarHeight(mActivity));
    }


    /**
     * tip: 警惕使用。非必要情况下，尽可能不在子类中拿到 binding 实例乃至获取 view 实例。使用即埋下隐患。
     * 目前的方案是在 debug 模式下，对获取实例的情况给予提示。
     *
     * @return binding
     */
    protected ViewDataBinding getBinding() {
        return mBinding;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        DataBindingConfig dataBindingConfig = getDataBindingConfig();

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, dataBindingConfig.getLayout(), container, false);
        binding.setLifecycleOwner(this);
        binding.setVariable(dataBindingConfig.getVmVariableId(), dataBindingConfig.getStateViewModel());
        SparseArray bindingParams = dataBindingConfig.getBindingParams();
        for (int i = 0, length = bindingParams.size(); i < length; i++) {
            binding.setVariable(bindingParams.keyAt(i), bindingParams.valueAt(i));
        }
        mBinding = binding;
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.unbind();
        mBinding = null;
    }

    private void initBaseObservable(Q mState) {
        mState.getShowHttpLoadingDialog().observe(this, isShow -> {
            if (isShow == null) return;
            if (isShow) {
                showLoadingDialog(mState.getHttpLoadingDialog().getMsg());
            } else {
                dismissLoadingDialogs();
            }
        });
    }

    /**
     * 显示Toast提示
     *
     * @param text
     */
    public void showToast(String text) {
        MyToast.showToast(text);
    }


    /**
     * 显示loading 对话框
     * 默认文字  正在加载中...
     */
    public Dialog showLoadingDialog(String title) {
        if (!mActivity.isFinishing()) {
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(getContext());
                loadingDialog.setCanceledOnTouchOutside(false);
                loadingDialog.setCancelable(false);
            }
            loadingDialog.show();
            return loadingDialog;
        }
        return null;
    }


    /**
     * dismiss loadingdialog
     */
    public void dismissLoadingDialogs() {
        if (!mActivity.isFinishing()) {
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
        }
    }

    /**
     * des:隐藏软键盘
     */
    public void hideInputForce() {
        if (this == null || mActivity.getCurrentFocus() == null) {
            return;
        }
        ((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(mActivity.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        mActivity.getCurrentFocus().clearFocus();
    }

    /**
     * 打开键盘
     **/
    public void showInput(View view) {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            view.requestFocus();
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * 获取context
     */
    public Context getContext() {
        return mActivity;
    }


    protected <T extends ViewModel> T getFragmentScopeViewModel(@NonNull Class<T> modelClass) {
        if (mFragmentProvider == null) {
            mFragmentProvider = VMProvider.of(this);
        }
        return mFragmentProvider.get(modelClass);
    }

    protected <T extends ViewModel> T getParentFragmentScopeViewModel(
            @NonNull Class<T> modelClass) {
        if (mParentFragmentProvider == null) {
            mParentFragmentProvider = VMProvider.of(getParentFragment());
        }
        return mParentFragmentProvider.get(modelClass);
    }

    protected <T extends ViewModel> T getActivityScopeViewModel(@NonNull Class<T> modelClass) {
        if (mActivityProvider == null) {
            mActivityProvider = VMProvider.of(mActivity);
        }
        return mActivityProvider.get(modelClass);
    }

    protected <T extends ViewModel> T getApplicationScopeViewModel(@NonNull Class<T> modelClass) {
        if (mApplicationProvider == null) {
            mApplicationProvider = VMProvider.ofApp(this);
        }
        return mApplicationProvider.get(modelClass);
    }
}
