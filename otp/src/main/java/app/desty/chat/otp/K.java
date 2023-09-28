package app.desty.chat.otp;

public class K {

    // Used to load the 'c' library on application startup.
    static {
        System.loadLibrary("otp");
    }

    /**
     * 获取图片中的密钥字符串
     *
     * @param data 图片数据
     * @return 密钥字符串
     */
    public static native String b2s(byte[] data, int channel);

}