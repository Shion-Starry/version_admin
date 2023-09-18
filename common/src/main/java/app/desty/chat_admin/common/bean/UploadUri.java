package app.desty.chat_admin.common.bean;

import android.net.Uri;

public class UploadUri {
    private Uri uri;


    public UploadUri(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
