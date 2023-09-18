package app.desty.chat_admin

import androidx.multidex.MultiDexApplication
import androidx.startup.AppInitializer
import androidx.work.Configuration
import app.desty.chat_admin.common.initializer.ARouterInitializer
import app.desty.chat_admin.common.initializer.ConfigInitializer
import app.desty.chat_admin.common.initializer.LogInitializer
import app.desty.chat_admin.common.initializer.NetInitializer
import app.desty.chat_admin.common.utils.ActivityLifecycleManager
import com.blankj.utilcode.util.ProcessUtils
import com.core.result.ActivityResultApi
import com.google.firebase.FirebaseApp

class ChatApplication : MultiDexApplication(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
        ActivityResultApi.init(this)
        // 注册生命周期管理器
        registerActivityLifecycleCallbacks(ActivityLifecycleManager.getInstance())
        if (!ProcessUtils.isMainProcess()) {//非主进程手动调用初始化
            AppInitializer.getInstance(this).apply {
                initializeComponent(ConfigInitializer::class.java)
                initializeComponent(ARouterInitializer::class.java)
                initializeComponent(LogInitializer::class.java)
                initializeComponent(NetInitializer::class.java)
            }
            FirebaseApp.initializeApp(this)
        }
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()


}