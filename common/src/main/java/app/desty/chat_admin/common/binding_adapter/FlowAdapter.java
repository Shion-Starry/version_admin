package app.desty.chat_admin.common.binding_adapter;

import android.view.View;

public interface FlowAdapter {
    String viewTag = "FlowAdapter";

    <T> View convertView(View parent, T object, int position, int total);
}
