package app.desty.chat_admin.common.base;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.gyf.immersionbar.ImmersionBar;

import java.lang.reflect.ParameterizedType;

import app.desty.chat_admin.common.R;
import app.desty.chat_admin.common.bean.ToolbarConfig;
import app.desty.chat_admin.common.config.UserConfig;
import app.desty.chat_admin.common.utils.LoginUtil;
import app.desty.chat_admin.common.utils.MyToast;
import app.desty.chat_admin.common.widget.LoadingDialog;
import app.desty.chat_admin.common.bean.ToolbarConfig;
import app.desty.chat_admin.common.config.UserConfig;
import app.desty.chat_admin.common.utils.LoginUtil;
import app.desty.chat_admin.common.utils.MyToast;
import app.desty.chat_admin.common.widget.LoadingDialog;


public abstract class BaseVmActivity<Q extends BaseVM> extends AppCompatActivity implements ViewModelStoreOwner {
    private ViewDataBinding mBinding;

    protected Context  mContext;
    protected Activity mActivity;
    protected Q        mState;

    private Dialog loadingDialog;

    private ViewModelProvider mActivityProvider;
    private ViewModelProvider mApplicationProvider;


    protected abstract void initViewModel();

    protected abstract DataBindingConfig getDataBindingConfig();

    protected abstract void init(@Nullable Bundle savedInstanceState);

    protected void beforeSetContent(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 警惕使用。非必要情况下，尽可能不在子类中拿到 binding 实例乃至获取 view 实例。使用即埋下隐患。
     *
     * @return binding
     */
    protected ViewDataBinding getBinding() {
        return mBinding;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);//注解注入传参
        super.onCreate(savedInstanceState);
        beforeSetContent(savedInstanceState);
        this.mContext  = this;
        this.mActivity = this;

        mState = getActivityScopeViewModel((Class<Q>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        int statusBarHeight = SizeUtils.px2dp(BarUtils.getStatusBarHeight());
        mState.getStatusBarHeight().setValue(statusBarHeight);
        mState.getStatusBarHeightPx().setValue(BarUtils.getStatusBarHeight());
        mState.getNavigationBarHeight().setValue(SizeUtils.px2dp(BarUtils.getNavBarHeight()));
        initViewModel();
        ToolbarConfig titleBean = getToolbarConfig();
        if (titleBean != null) {
            titleBean.setStatusBarHeight(statusBarHeight);
        }
        mState.getToolbarConfig().setValue(titleBean);
        DataBindingConfig dataBindingConfig = getDataBindingConfig();

        ViewDataBinding binding = DataBindingUtil.setContentView(this, dataBindingConfig.getLayout());
        binding.setLifecycleOwner(this);
        binding.setVariable(dataBindingConfig.getVmVariableId(), dataBindingConfig.getStateViewModel());
        SparseArray bindingParams = dataBindingConfig.getBindingParams();
        for (int i = 0, length = bindingParams.size(); i < length; i++) {
            binding.setVariable(bindingParams.keyAt(i), bindingParams.valueAt(i));
        }
        mBinding = binding;
        setStatusBar();
        initBaseObservable(mState);
        init(savedInstanceState);
        Integer autoShowKeyboardViewId = getAutoShowKeyboardViewId();
        if (autoShowKeyboardViewId != null) {
            KeyboardUtils.showSoftInput(findViewById(autoShowKeyboardViewId));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBinding != null) {
            mBinding.unbind();
            mBinding = null;
        }
    }

    /**
     * 获取自动弹出键盘的目标View Id
     */
    protected Integer getAutoShowKeyboardViewId() {
        return null;
    }

    // 设置顶部状态栏，若不需要用，重载为空方法
    protected void setStatusBar() {
        //默认白色状态栏
        ImmersionBar.with(this)
                    .statusBarColor(android.R.color.transparent)
                    .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                    .navigationBarColor(R.color.grey_50)
                    .navigationBarDarkIcon(true)
                    .init();
    }

    protected ToolbarConfig getToolbarConfig() {
        return null;
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
//        mState.handleErrorByDialog.observe(this, dialog -> {
//            if (dialog != null) {
//                dialog.create(BaseVmActivity.this);
//            }
//        });
        mState.getShowDialogFragment().observe(this, dialogFragment -> {
            if (dialogFragment != null) {
                dialogFragment.show(getSupportFragmentManager(), StringUtils.format("dialog_%d", System.currentTimeMillis()));
            }
        });
        mState.getFinishActivity().observe(this, this::finishActivityByVM);
    }

    protected void finishActivityByVM(boolean finish) {
        if (finish) {
            if (ObjectUtils.getOrDefault(mState.getSetResult().getValue(), false)) {
                setResult(RESULT_OK, mState.getIntent());
            }
            finish();
        }
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

        if (!this.isFinishing()) {
            if (loadingDialog == null) {
                loadingDialog =  new LoadingDialog(this);
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
        if (!this.isFinishing()) {
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
        }
    }

    /**
     * des:隐藏软键盘
     */
    public void hideInputForce() {
        if (this == null || getCurrentFocus() == null) {
            return;
        }
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(getCurrentFocus()
                                                 .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        getCurrentFocus().clearFocus();
    }

    /**
     * 打开键盘
     **/
    public void showInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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


    protected <T extends ViewModel> T getActivityScopeViewModel(@NonNull Class<T> modelClass) {
        if (mActivityProvider == null) {
            mActivityProvider = VMProvider.of(this);
        }
        return mActivityProvider.get(modelClass);
    }

    protected <T extends ViewModel> T getApplicationScopeViewModel(@NonNull Class<T> modelClass) {
        if (mApplicationProvider == null) {
            mApplicationProvider = VMProvider.ofApp(this);
        }
        return mApplicationProvider.get(modelClass);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果堆栈信息中有首页，则校验token是否为空
        if (!UserConfig.INSTANCE.isLoginStatus()) {//不为登录态
            if (!UserConfig.INSTANCE.getToken().isEmpty()) {//但是Token未清理
                //跨进程退出时会出现这个case
                LoginUtil.INSTANCE.logout(false);
            }
        }
    }
}
