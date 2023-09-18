package app.desty.chat_admin.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.core.LogisticsCenter;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.imuxuan.floatingview.FloatingView;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import app.desty.chat_admin.common.bean.HomePageData;
import app.desty.chat_admin.common.bean.HomeRouteBean;
import app.desty.chat_admin.common.constants.RouteConstants;
import app.desty.chat_admin.common.constants.RouteConstants;


public class ActivityLifecycleManager implements Application.ActivityLifecycleCallbacks {

    public static String TAG = "ActivityManager";
    private Stack<Activity> activityStack;
    private int activityStartCount;
    protected static ActivityLifecycleManager mInstances;


    public static ActivityLifecycleManager getInstance() {
        if (mInstances == null) {
            mInstances = new ActivityLifecycleManager();
        }
        return mInstances;
    }

    /**
     * 获取在状态在onStart和onStop之间的Activity数量
     *
     * @return
     */
    public int getActiveActivityCount() {
        return activityStartCount;
    }

    /**
     * 添加Activity到堆栈
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (activityStack == null || activityStack.isEmpty()) {
            return;
        }
        activityStack.add(activity);
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activityStack == null || activityStack.isEmpty()) {
            return;
        }
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public int getActivityCount() {
        if (activityStack == null || activityStack.isEmpty()) {
            return 0;
        }
        return activityStack.size();
    }


    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity getTopActivity() {
        if (activityStack == null || activityStack.isEmpty()) {
            return null;
        }

        return activityStack.lastElement();
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        try {
            if (null == activityStack) {
                return;
            }
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                if (null != activityStack.get(i)) {
                    try {
                        activityStack.get(i).finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            activityStack.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restartApp() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Context context = getTopActivity();
            ActivityLifecycleManager.getInstance().finishAllActivity();
            Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(LaunchIntent);

            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> mList = mActivityManager.getRunningAppProcesses();
            Log.i("Restart", Arrays.toString(mList.toArray()));
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : mList) {
                if (runningAppProcessInfo.pid != android.os.Process.myPid()) {
                    android.os.Process.killProcess(runningAppProcessInfo.pid);
                }
            }
            android.os.Process.killProcess(android.os.Process.myPid());
        }, 1000);
    }

    /**
     * 结束栈底之上及指定Activity之外的其他Activity
     *
     * @param cls
     */
    public void finishAbovePath(Class<?> cls) {
        try {
            if (null == activityStack) {
                return;
            }
            for (int i = activityStack.size() - 1; i >= 1; i--) {
                Activity topActivity = activityStack.get(i);
                if (null != topActivity && !topActivity.getClass().equals(cls)) {
                    try {
                        activityStack.remove(i);
                        topActivity.finish();
                        topActivity = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 结束栈底之上及指定Activity之外的其他Activity
     *
     * @param path
     */
    public void finishAbovePath(String path) {
        Class tClass = routerToClass(path);
        if (tClass != null) finishAbovePath(tClass);

    }

    /**
     * 跳转到指定页面，若堆栈中存在对应页面就关闭该页面之上的页面，不存在就新打卡页面
     *
     * @param path           页面路径
     * @param activityAction 对应页面存在，操作接口
     * @param routeAction    对应页面不存在，ARoute操作
     */
    public void routeToPath(String path, ActivityAction activityAction, RouteAction routeAction) {
        if (!finishAbovePath(path, activityAction)) {
            routeAction.onRoute(ARouter.getInstance().build(path)).navigation();
        }
    }

    public void routeToHome(@Nullable HomeRouteBean homeRouteBean) {
        routeToPath(
                RouteConstants.Home.homaPage,
                activity -> {
                    if (homeRouteBean != null) {
                        try {
                            Class<?> builderClz = Class.forName("app.desty.chat_admin.home.HomeActivity");
                            if (builderClz.isInstance(activity)) {
                                builderClz.getDeclaredMethod(
                                                "newHomeRoute",
                                                HomeRouteBean.class
                                        )
                                        .invoke(activity, homeRouteBean);
                            }
                        } catch (Exception ignore) {
                            ignore.printStackTrace();
                        }
                    }
                },
                postCard -> {
                    postCard.withParcelable(HomePageData.homeRouteBean, homeRouteBean);
                    return postCard;
                });
    }

    /**
     * 关闭指定页面之上的页面
     *
     * @param path           指定页面路径
     * @param activityAction 目标Activity操作
     */
    public boolean finishAbovePath(String path, ActivityAction activityAction) {
        AppCompatActivity activity = (AppCompatActivity) findActivity(path);
        if (activity == null) return false;
        if (activityAction != null) {
            activity.runOnUiThread(() -> activityAction.onAction(activity));
        }
        finishAbovePath(path);
        return true;
    }

    /**
     * 结束指定类名的Activity
     *
     * @param cls
     */
    public void finishActivity(Class<?> cls) {
        if (activityStack == null || activityStack.isEmpty()) {
            return;
        }
        for (Activity activity : activityStack) {
            if (null != activity && activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束指定router路径的activity
     *
     * @param path
     * @return
     */
    public void finishActivity(String path) {
        Class tClass = routerToClass(path);
        if (tClass != null) finishActivity(tClass);
    }

    /**
     * 按照指定类名找到activity
     *
     * @param cls
     * @return
     */
    public AppCompatActivity findActivity(Class<?> cls) {
        if (activityStack == null || activityStack.isEmpty()) {
            return null;
        }
        Activity targetActivity = null;
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                targetActivity = activity;
                break;
            }
        }
        return (AppCompatActivity) targetActivity;
    }

    /**
     * 按照指定类名找到activity
     *
     * @param cls
     * @return
     */
    public void findActivity(Class<?> cls, ActivityAction activityAction) {
        if (activityStack == null || activityStack.isEmpty()) {
            return;
        }
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                if (activityAction != null && activity instanceof AppCompatActivity) {
                    activityAction.onAction((AppCompatActivity) activity);
                }
                break;
            }
        }
    }

    /**
     * 按照指定router路径找到activity
     *
     * @param path
     * @return
     */
    public AppCompatActivity findActivity(String path) {
        Class tClass = routerToClass(path);
        if (tClass == null) return null;
        return findActivity(tClass);
    }

    private Class routerToClass(String path) {
        try {
            Postcard postcard = ARouter.getInstance().build(path);
            LogisticsCenter.completion(postcard);
            return postcard.getDestination();
        } catch (Exception ignore) {
            return null;
        }
    }

    /**
     * 退出应用程序
     *
     * @param context
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            if (context != null) {
                android.app.ActivityManager activityMgr = (android.app.ActivityManager) context
                        .getSystemService(Context.ACTIVITY_SERVICE);
                activityMgr.killBackgroundProcesses(context.getPackageName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activityStack.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        activityStartCount++;
//        if (activityStartCount == 1) {
//            // nothing
//        }
        try {
            FloatingView.get().attach(activity);
        } catch (Exception ignore) {

        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        activityStartCount--;
//        if (activityStartCount == 0) {
//            Map<String, Object> paramsMap = new HashMap();
//            //自定义参数
//            paramsMap.put(TrackConstants.PARAM_KEY.STATUS, 2);
//            MobclickAgent.onEventObject(getTopActivity(), TrackConstants.EVENT_ID.APP_STATUS, paramsMap);
//        }
        try {
            FloatingView.get().detach(activity);
        } catch (Exception ignore) {

        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activityStack != null && !activityStack.isEmpty()) {
            activityStack.remove(activity);
        }
    }

    public interface ActivityAction {
        void onAction(@NonNull AppCompatActivity activity);
    }

    public interface RouteAction {
        Postcard onRoute(@NonNull Postcard postcard);
    }

}

