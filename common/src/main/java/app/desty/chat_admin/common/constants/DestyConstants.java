package app.desty.chat_admin.common.constants;

/**
 * @author xiaoke.lin
 * @date 2022/3/10
 */
public interface DestyConstants {
    String[] highLight = new String[]{"*"};

    /*  **** 时间单位  ****  */
    long SECOND = 1000;
    long MINUTE = 60 * SECOND;
    long HOUR   = 60 * MINUTE;
    long DAY    = 24 * HOUR;

    long sendImageMaxLength = 10 * 1024 * 1024;

    //会话最长存活时间
    long sessionTTL      = 35L * DAY;
    //tiktok 过期时间
    long tiktokSessionExpireTime = 3L * DAY;
    //通知存活时间
    long notificationTTL = 48 * HOUR;
    //通知的intent action
    String notificationAction = "app.desty.chat_admin.NOTIFY";

    int pullMessagePageSize = 1000;//消息拉取页码大小
    int maxShowUnreadCount  = 99;//最大显示的未读的数量，若设置为99超过该值显示99+

    /*  **** 时间格式  ****  */
    String timePattern               = "dd MMM yyyy HH:mm:ss";
    String timePatternWithoutSec     = "dd MMM yyyy HH:mm";
    String timePatternWithoutTime    = "dd MMM yyyy";
    String timePattern_HHmm          = "HH:mm";
    String timePattern_HHmmss        = "HH:mm:ss";
    String timePatternDate           = "dd MMM";
    String timePatternDDmm           = "dd MMM HH:mm:ss";
    String timePatternDDmmWithoutSec = "dd MMM HH:mm";

    String serverTimePattern = "yyyy-MM-dd";//与服务端交互的时间格式，不对用户展示


    String destyPhone           = "08995533789";//联系desty电话
    String destyWhatsAppNumber  = "628995533789";//desty的WA号码
    String destyWhatsAppMessage = "Hallo, saya butuh bantuan :)";//通过WA联系desty默认信息

    int maxModifierOptionPrice = 9999000;//最大配料价格

    /* ***** Delay ***** */
    long checkExistDelay = 500;
    long searchDelay     = 500;

    /* ****** Http ****** */
    int httpTimeout = 20 * 1000;//默认网络超时时间

    interface PageSize {
        int smallPageSize  = 5;
        int normalPageSize = 10;
        int largePageSize  = 20;
    }
}
