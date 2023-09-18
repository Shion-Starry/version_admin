#关键字	描述
#keep	保留类和类中的成员，防止它们被混淆或移除。
#keepnames	保留类和类中的成员，防止它们被混淆，但当成员没有被引用时会被移除。
#keepclassmembers	只保留类中的成员，防止它们被混淆或移除。
#keepclassmembernames	只保留类中的成员，防止它们被混淆，但当成员没有被引用时会被移除。
#keepclasseswithmembers	保留类和类中的成员，防止它们被混淆或移除，前提是指名的类中的成员必须存在，如果不存在则还是会混淆。
#keepclasseswithmembernames	保留类和类中的成员，防止它们被混淆，但当成员没有被引用时会被移除，前提是指名的类中的成员必须存在，如果不存在则还是会混淆。

#以上有name的表示需要被引用到才会保留，否则就算声明了如果没有被引用到，也还是会被移除的
#-----------------基本配置参数--------------------
-dontoptimize
-verbose
#-android
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-printmapping mapping.txt
#------------end-------------------------------


#-------------google服务---------------
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.google.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService
-dontnote com.google.vending.licensing.ILicensingService
-dontnote com.google.android.vending.licensing.ILicensingService
#----------- end -----------------

#-------------有些第三方库用了andorid sdk中apache作废的类---------------
-keep class org.apache.http.**{*;}

#--------------默认的需要保留的类和成员 -----------------
-keepattributes *Annotation* #注解
-keepattributes SourceFile,LineNumberTable # 行号
-keepattributes Signature # 泛型
-keepattributes EncloseClass #反射
-keep class * extends java.lang.annotation.Annotation{*;}
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.

-keepclasseswithmembernames class * {
    native <methods>;
}
#属性方法会用到
-keepclassmembers class * extends android.view.View{
    public void set*(***);
    public *** get();
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#xml中的onClick指向的方法
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}
-keep enum app.desty.** {*;}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keepclassmembers class **.R$* {
    public static <fields>;
}
# webview js调用方法
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
    public void *(android.webkit.WebView, jav.lang.String);
}

# Keep注解保留的关键
-keep class android.support.annotation.Keep
-keep @android.support.annotation.Keep class * {*;}
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
    @android.support.annotation.Keep <fields>;
    @android.support.annotation.Keep <init>(...);
}
#----------------end---------------------------------

#-------------------support库下的不需要提醒-------------------
-dontnote android.support.**
-dontwarn android.support.**
# 保留support下的所有类及其内部类
-keep class android.support.** {*;}

# 保留继承的
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcaseReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.Application
-keep public class * extends andoird.view.View

-keep class com.onesignal.JobIntentService$* {*;}
