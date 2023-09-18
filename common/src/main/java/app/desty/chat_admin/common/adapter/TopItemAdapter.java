package app.desty.chat_admin.common.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SizeUtils;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import app.desty.chat_admin.common.R;
import app.desty.chat_admin.common.databinding.TabItemLiveOrderBinding;
import app.desty.chat_admin.common.widget.TopTabItem;


/**
 * @author xiaoke.lin
 * @date 2022/3/4
 */
public class TopItemAdapter extends CommonNavigatorAdapter {

    private int itemMinWidth = -1;
    final int[] stringRes;
    final TabItemLiveOrderBinding[] topTabItems;

    public TopItemAdapter(int... titleRes) {
        stringRes = titleRes;
        topTabItems = new TabItemLiveOrderBinding[titleRes.length];
    }

    public TopItemAdapter setItemMinWidth(int itemMinWidth) {
        this.itemMinWidth = itemMinWidth;
        return this;
    }

    @Override
    public int getCount() {
        return stringRes.length;
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        TopTabItem tabItem = new TopTabItem(context, itemMinWidth);
        TabItemLiveOrderBinding inflate = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.tab_item_live_order, null, false);
        tabItem.setContentView(inflate.getRoot());
        tabItem.setTextNormalColor(ColorUtils.getColor(R.color.grey_400))
                .setTextSelectedColor(ColorUtils.getColor(R.color.grey_700))
                .setCountBackSelectedColor(ColorUtils.getColor(R.color.grey_700))
                .setCountBackNormalColor(ColorUtils.getColor(R.color.grey_400))
                .setTitleTextRes(stringRes[index]);
        tabItem.setOnClickListener(v -> {
            if (clickPosition != null) {
                clickPosition.onClick(v, index);
            }
        });
        topTabItems[index] = inflate;
        return tabItem;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);

        indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
        indicator.setXOffset(0);
        indicator.setLineHeight(SizeUtils.dp2px(4));
        indicator.setRoundRadius(SizeUtils.dp2px(2));
        indicator.setStartInterpolator(new AccelerateInterpolator());
        indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
        indicator.setColors(ColorUtils.getColor(R.color.chat_600));
        return indicator;
    }

    public TabItemLiveOrderBinding getTopTabItem(int index) {
        if (index < 0 || index >= topTabItems.length) return null;
        return topTabItems[index];
    }

    public void setCounts(String... counts) {
        for (int i = 0; i < getCount(); i++) {
            if (i >= topTabItems.length) break;
            if (i >= counts.length) break;
            setCountView(topTabItems[i].tabItemCount, counts[i]);
        }
    }

    public void setCountByIndex(int index, String count) {
        if (index >= topTabItems.length) return;
        setCountView(topTabItems[index].tabItemCount, count);
    }

    public void setCountView(TextView countView, String count) {
        if (countView == null) return;
        countView.setVisibility(TextUtils.isEmpty(count)
                ? View.GONE
                : View.VISIBLE);
        countView.setText(count);
    }


    private ClickPosition clickPosition;

    public ClickPosition getClickPosition() {
        return clickPosition;
    }

    public TopItemAdapter setClickPosition(
            ClickPosition clickPosition) {
        this.clickPosition = clickPosition;
        return this;
    }

    public interface ClickPosition {
        void onClick(View view, int index);
    }


}
