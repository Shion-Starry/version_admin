package app.desty.chat_admin.common.widget;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author xiaoke.lin
 * @date 2022/3/5
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private final int left;
    private final int top;
    private final int right;
    private final int bottom;


    public SpacesItemDecoration(int space) {
        top    = 0;
        bottom = space;
        left   = 0;
        right  = 0;
    }

    public SpacesItemDecoration(int verticalSpace, int horizontalSpace) {
        top    = 0;
        bottom = verticalSpace;
        left   = horizontalSpace;
        right  = horizontalSpace;
    }

    public SpacesItemDecoration(int left, int top, int right, int bottom) {
        this.left   = left;
        this.top    = top;
        this.right  = right;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
//        outRect.left   = left;
//        outRect.right  = right;
//        outRect.bottom = bottom;
        outRect.set(left, top, right, bottom);
//        // Add top margin only for the first item to avoid double space between items
//        if (parent.getChildAdapterPosition(view) == 0)
//            outRect.top = 0;
    }
}
