package app.desty.chat_admin.common.binding_adapter;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.drake.statelayout.StateLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.desty.chat_admin.common.constants.DestyConstants;
import app.desty.chat_admin.common.utils.AutoCapUtil;
import app.desty.chat_admin.common.utils.DestySpanUtils;
import app.desty.chat_admin.common.utils.QuickOnClick;
import app.desty.chat_admin.common.widget.SpacesItemDecoration;
import app.desty.sdk.logcat.Logcat;


public class CommonBindingAdapter {

    @BindingAdapter(value = {"url"})
    public static void setWebViewUrl(WebView webView, String url) {
        if (webView != null) {
            webView.loadUrl(url);
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
        }

    }

    /****************** View ******************/

    @BindingAdapter(value = {"layout_constraintWidth_percent"})
    public static void setWidthPercent(View view, float percent) {
        if (view == null || view.getParent() == null) return;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ConstraintLayout.LayoutParams) {
            ConstraintLayout.LayoutParams cLayoutParams = (ConstraintLayout.LayoutParams) layoutParams;
            cLayoutParams.matchConstraintPercentWidth = percent;
            view.setLayoutParams(cLayoutParams);
        }
    }

    @BindingAdapter(value = {"layout_constraintHorizontal_bias",
                             "layout_constraintVertical_bias"}, requireAll = false)
    public static void setBias(View view, float horizontalBias, float verticalBias) {

        if (view == null || view.getParent() == null) return;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ConstraintLayout.LayoutParams) {
            ConstraintLayout.LayoutParams cLayoutParams = (ConstraintLayout.LayoutParams) layoutParams;
            cLayoutParams.horizontalBias = horizontalBias;
            cLayoutParams.verticalBias   = verticalBias;
            view.setLayoutParams(cLayoutParams);
        }
    }

    @BindingAdapter(value = {"android:onClick", "waitTime"}, requireAll = false)
    public static void quickClick(View view, View.OnClickListener onClickListener, Long waitTime) {
        if (view == null) return;
        QuickOnClick quickClick = new QuickOnClick() {
            @Override
            public void click(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
            }
        };
        if (waitTime != null) {
            quickClick.setWaitTime(waitTime);
        }
        view.setOnClickListener(quickClick);
    }

    @BindingAdapter(value = {"customerAnimations"})
    public static void setCustomerAnimations(ViewGroup view,Boolean customAnimations){
        if(!customAnimations)return;
        LayoutTransition layoutTransition = view.getLayoutTransition();
        if(layoutTransition==null){
            layoutTransition = new LayoutTransition();
        }


        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 500f, 0f);
//        animator.setDuration(2000);
        layoutTransition.setAnimator(LayoutTransition.APPEARING, animator);


//        ObjectAnimator disappearAnimator = ObjectAnimator.ofFloat(view,"alpha",1f,0f);
        ObjectAnimator disappearAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0f, 500f);
//        disappearAnimator.setDuration(2000);
        layoutTransition.setAnimator(LayoutTransition.DISAPPEARING, disappearAnimator);

        layoutTransition.setAnimator(LayoutTransition.CHANGING, null);
        layoutTransition.setAnimator(LayoutTransition.CHANGE_APPEARING, null);
        layoutTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, null);
        layoutTransition.setDuration(200);
        view.setLayoutTransition(layoutTransition);

    }

    @BindingAdapter(value = {"visible"}, requireAll = false)
    public static void visible(View view, Boolean visible) {
        if (visible != null)
            view.setVisibility(visible
                               ? View.VISIBLE
                               : View.GONE);
    }


    //是否显示
//    @BindingAdapter(value = {"visible", "access", "android:enable",
//                             "accessType"}, requireAll = false)
//    public static void visible(View view, Boolean visible, Boolean access, Boolean enable,
//                               int accessType) {
//        switch (accessType) {
//            //有权限显示
//            case AccessControlUtil
//                    .ACCESS_VISIBLE:
//                if (access != null && visible != null) {
//                    view.setVisibility(access && visible
//                                       ? View.VISIBLE
//                                       : View.GONE);
//                } else if (access != null) {
//                    view.setVisibility(
//                            access
//                            ? View.VISIBLE
//                            : View.GONE
//                    );
//                }
//
//                break;
//            //有权限可用
//            case AccessControlUtil.ACCESS_ENABLE:
//                if (access != null && enable != null) {
//                    view.setEnabled(enable && access);
//                } else if (access != null) {
//                    view.setEnabled(access);
//                }
//                if (visible != null) {
//                    view.setVisibility(visible
//                                       ? View.VISIBLE
//                                       : View.GONE);
//                }
//
//                break;
//            default:
//                if (enable != null)
//                    view.setEnabled(enable);
//                if (visible != null)
//                    view.setVisibility(visible
//                                       ? View.VISIBLE
//                                       : View.GONE);
//                break;
//        }
//
//    }


    @BindingAdapter("android:layout_width")
    public static void setLayoutWidth(View view, float width) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = (int) width;
        view.setLayoutParams(params);
    }

    @BindingAdapter("android:layout_height")
    public static void setLayoutHeight(View view, float height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = (int) height;
        view.setLayoutParams(params);
    }

    @BindingAdapter(value = {"marginLeft", "marginTop", "marginRight",
                             "marginBottom", "marginIsDp"}, requireAll = false)
    public static void setMargin(View view, int left, int top, int right, int bottom,
                                 Boolean isDp) {
        if (isDp == null || isDp) {
            left   = SizeUtils.dp2px(left);
            top    = SizeUtils.dp2px(top);
            right  = SizeUtils.dp2px(right);
            bottom = SizeUtils.dp2px(bottom);
        }
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.setMargins(left, top, right, bottom);
    }

    @BindingAdapter(value = {"paddingLeft", "paddingTop", "paddingRight",
                             "paddingBottom", "paddingIsDp"}, requireAll = false)
    public static void setPadding(View view, int left, int top, int right, int bottom,
                                  Boolean isDp) {
        if (isDp == null || isDp) {
            left   = SizeUtils.dp2px(left);
            top    = SizeUtils.dp2px(top);
            right  = SizeUtils.dp2px(right);
            bottom = SizeUtils.dp2px(bottom);
        }
        view.setPadding(left, top, right, bottom);
    }

    //增加控件点击范围
    @BindingAdapter(value = {"area"}, requireAll = false)
    public static void toSetArea(View view, Integer b) {
        if (b == null || view == null) return;
        Rect delegateArea = new Rect();
        view.getHitRect(delegateArea);
        int bInPx = SizeUtils.dp2px(b);
        delegateArea.top -= bInPx;           //上方增加范围
        delegateArea.bottom += bInPx;        //下方增加范围
        delegateArea.left -= bInPx;          //左边增加
        delegateArea.right += bInPx;           //右边

        TouchDelegate expandedArea = new TouchDelegate(delegateArea, view);
        if (view.getParent() instanceof View) {
            ((View) view.getParent())
                    .setTouchDelegate(expandedArea);
        }
    }

    //获取焦点回调
    @BindingAdapter(value = {"onFocus"}, requireAll = false)
    public static void bindFocus(final View view, View.OnFocusChangeListener listener) {
        if (view != null) {
            view.setOnFocusChangeListener(listener);
        }
    }

    @BindingAdapter(value = {"backgroundAlpha"})
    public static void setAlpha(View view, int alpha) {
        view.getBackground().mutate().setAlpha(alpha);
    }

    @BindingAdapter(value = {"topToId", "toTop"})
    public static void autoToTop(View view, int id, boolean toTop) {
        ViewParent viewParent = view.getParent();
        if (viewParent == null) return;
        if (viewParent instanceof ConstraintLayout) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
            layoutParams.topToBottom = toTop
                                       ? -1
                                       : id;
            layoutParams.topToTop    = toTop
                                       ? id
                                       : -1;
            view.setLayoutParams(layoutParams);
        }
    }

    @BindingAdapter("backgroundColor")
    public static void setBackground(View view, @ColorInt Integer color) {
        view.setBackgroundColor(color);
    }

    /**
     * 避免资源ID为0会奔溃
     */
    @BindingAdapter(value = {"android:text"})
    public static void setTextRes(TextView view, int textResId) {
        if (view == null) return;
        if (textResId == 0) {
            view.setText("");
        } else {
            view.setText(textResId);
        }
    }

    //设置富文本
    @BindingAdapter(value = {"textSpan"}, requireAll = false)
    public static void textSpan(TextView view, SpannableStringBuilder textSpan) {
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setHighlightColor(view.getResources().getColor(android.R.color.transparent));
        view.setText(textSpan);
    }


    @BindingAdapter(value = {"highLightWord", "highLightColor"})
    public static void setHighLight(TextView view, String[] highLightWord, int color) {
        if (highLightWord != null) {
            CharSequence span = DestySpanUtils.getLightSpan(view.getText().toString(),
                                                            highLightWord, color);
            view.setText(span);
        }
    }

    @BindingAdapter(value = {"highLightWord", "highLightColor", "text"})
    public static void setHighLight(TextView view, String[] highLightWord, int color, String text) {
        if (highLightWord != null && text != null) {
            CharSequence span = DestySpanUtils.getLightSpan(text,
                                                            highLightWord, color);
            view.setText(span);
        } else {
            view.setText(text);
        }
    }

    @BindingAdapter(value = {"underLineWord", "text"})
    public static void setUnderLine(TextView view, String[] underLineWord, String text) {
        if (underLineWord != null && text != null) {
            CharSequence span = DestySpanUtils.getCustomSpan(text, underLineWord, UnderlineSpan::new);
            view.setText(span);
        } else {
            view.setText(text);
        }
    }

    @BindingAdapter(value = {"date", "pattern"}, requireAll = false)
    public static void setDateText(TextView text, Long date, String pattern) {
        if (text == null || date == null) return;
        if (TextUtils.isEmpty(pattern)) pattern = DestyConstants.timePattern;
        text.setText(TimeUtils.millis2String(date, pattern));
    }


//    //文字最大长度
//    @BindingAdapter(value = {"android:text", "maxSize","isBold"},requireAll = false)
//    public static void maxSize(TextView view, CharSequence text, int maxSize,Boolean isBold) {
//        if (maxSize > 0 && text != null && text.length() > maxSize) {
//            text = text.toString().substring(0, maxSize) + "...";
//        }
//        view.setText(text);
//    }

//    //文字颜色渐变
//    @BindingAdapter(value = {"startTextColor", "endTextColor", "textAlpha"}, requireAll = true)
//    public static void setGradient(TextView textView, @ColorInt Integer startTextColor,
//                                   @ColorInt Integer endTextColor, float alpha) {
//        alpha = Math.max(0f, Math.min(255f, alpha)) / 255;
//        textView.setTextColor(UIUtils.getGradientColor(startTextColor, endTextColor, alpha));
//    }
//
//    @BindingAdapter(value = {"colorText"})
//    public static void setColorText(TextView textView, List<ColorTextBean> colorText) {
//        textView.setText(UIUtils.convertColorTextToSpan(colorText));
//    }

    @BindingAdapter(value = {"android:textColor",}, requireAll = false)
    public static void setTextColor(TextView textView, @ColorInt Integer textColor) {
        if (textColor != null) {
            textView.setTextColor(textColor);
        }
    }

    @BindingAdapter("textSize")
    public static void setTextSize(TextView textView, Float textSize) {
        if (textSize == null || textSize <= 0) return;
        textView.setTextSize(textSize);

    }

    @BindingAdapter(value = {"phone", "format"})
    public static void setPhoneFormat(TextView textView, String phone, String format) {
        if (phone == null) return;
        if (phone.length() < 11) return;
        if (format == null) return;
        if (textView == null) return;
        phone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        textView.setText(StringUtils.format(format, phone));
    }


    @BindingAdapter(value = {"bank"})
    public static void setBankFormat(TextView textView, String bank) {
        if (bank == null) return;
        if (textView == null) return;
        bank = bank.replaceAll("(\\d{4})\\d{1}(\\d{4})", "$1****$2");
        textView.setText(bank);
        int length = bank.toString().length();
        if (length <= 4) {
            textView.setText(bank);
        } else if (length > 4 && length <= 8) {
            textView.setText(bank.substring(0, 4) + (bank.substring(4)).replaceAll(".", "*"));
        } else {
            textView.setText(bank.substring(0, 4) + "****" + bank.substring(bank.length() - 4));
        }
    }


    /****************** ImageView ******************/

    //加载图片
//    @BindingAdapter(value = {"url", "option", "centerCrop", "urlImageWidth",
//                             "urlImageHeight"}, requireAll = false)
//    public static void setUrl(ImageView view, final String url, RequestOptions requestOptions,
//                              boolean centerCrop, Float urlImageWidth, Float urlImageHeight) {
//        if (centerCrop && url != null
//                && (url.contains("static.desty.app") || url.contains("static.desty.page") || url.contains("desty-upload-indonesia.oss-ap-southeast-5.aliyuncs.com"))) {
//            view.post(() -> {
//                int width = SizeUtils.dp2px(ObjectUtils.getOrDefault(urlImageWidth, 0f));
//                int height = SizeUtils.dp2px(ObjectUtils.getOrDefault(urlImageHeight, 0f));
//                if (width == 0) {
//                    width = view.getWidth();
//                }
//                if (height == 0) {
//                    height = view.getHeight();
//                }
//                Uri.Builder builder = Uri.parse(url).buildUpon();
//                builder.appendQueryParameter("x-oss-process", StringUtils.format("image/resize,m_fill,w_%d,h_%d", width, height));
//                String url1 = builder.build().toString();
//                loadImage(view, requestOptions, url1);
//            });
//        } else {
//            loadImage(view, requestOptions, url);
//        }
//    }
//
//    private static void loadImage(ImageView view, RequestOptions requestOptions, String url) {
//        if (!isValidContextForGlide(view.getContext())) return;
//        RequestBuilder<Drawable> load = Glide.with(view).load(url).timeout(6000);
//        if (requestOptions != null) {
//            load = load.apply(requestOptions);
//        }
//        load.into(view);
//    }
//
//    /**
//     * 校验activity是否已经结束
//     */
//    private static boolean isValidContextForGlide(final Context context) {
//        if (context == null) {
//            return false;
//        }
//        if (context instanceof Activity) {
//            final Activity activity = (Activity) context;
//            if (activity.isDestroyed() || activity.isFinishing()) {
//                return false;
//            }
//        }
//        return true;
//    }

    //image view tint颜色渐变
    @BindingAdapter(value = {"startTintColor", "endTintColor", "tintAlpha"})
    public static void setTintGradient(ImageView imageView, @ColorInt Integer startTextColor,
                                       @ColorInt Integer endTextColor, float alpha) {
        Drawable drawable = imageView.getDrawable();
        if (drawable == null) return;
        alpha = Math.max(0f, Math.min(255f, alpha)) / 255;
        drawable.setTint(ColorUtils.getGradientColor(startTextColor, endTextColor, alpha));
        imageView.setImageDrawable(drawable);
    }

    /****************** EditText ******************/
    @BindingAdapter(value = {"showPassword"})
    public static void setShowPassword(EditText editText, Boolean showPassword) {
        if (ObjectUtils.getOrDefault(showPassword, false)) {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        // 切换后将 EditText的光标置于末尾
        Spannable charSequence = editText.getText();
        if (charSequence != null) {
            Selection.setSelection(charSequence, charSequence.length());
        }

    }

    //自动弹出软键盘
    @BindingAdapter(value = {"showKeyBoard"}, requireAll = false)
    public static void isShowKeyboard(EditText view, boolean b) {
        if (!b) return;
        view.postDelayed(() -> {
            view.requestFocus();
            InputMethodManager manager = ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
            if (manager != null) manager.showSoftInput(view, 0);

            view.setSelection(view.getText().length());//移动光标到文字末尾
        }, 100);
    }

    //纯数字键盘,首位不能输入0
    @BindingAdapter(value = {"onlyNumber"}, requireAll = false)
    public static void setOnlyNumber(EditText view, boolean b) {
        if (!b) return;
        view.setInputType(InputType.TYPE_CLASS_NUMBER);
        view.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        if (!isNumber(arg0.toString())) {
                            arg0.clear();
                        }
                        if (arg0.toString().startsWith("0")) {
                            arg0.delete(0, 1);
                        }
                    }
                });
    }

    //是否是数字
    public static boolean isNumber(String str) {
        if (TextUtils.isEmpty(str)) return false;
        String speChat = "(^[0-9]*$)";
        Pattern pattern = Pattern.compile(speChat);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    @BindingAdapter(value = {"numberDecimalLength"})
    public static void setNumberDecimalLength(EditText editText, int length) {
        editText.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            String newString = dest.toString().substring(0, dstart) + source.subSequence(start, end) + dest.subSequence(dend, dest.length());
            int dotIndex = newString.lastIndexOf(".");
            if (dotIndex >= 0 && dotIndex + length + 1 < newString.length()) {
                return "";
            }
            return source;
        }});

    }

    @BindingAdapter(value = "autoCap")
    public static void setAutoCap(EditText editText, Boolean autoCap) {
        if (editText != null) {
            editText.setTransformationMethod(AutoCapUtil.buildAutoCapMethod());
        }
    }

    /**
     * 禁止EditText输入空格,同时设置两个后一个会覆盖前一个,有需求另改
     */
    @BindingAdapter(value = {"noSpace", "noSpeChat"}, requireAll = false)
    public static void setEditTextInhibitInputSpace(EditText editText, boolean noSpace,
                                                    boolean noSpeChat) {
        InputFilter filter;
        InputFilter filter1;

        if (noSpace) {
            filter = new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                           int dstart, int dend) {
                    if (source.equals(" ") || source.toString().contentEquals("\n")) return "";
                    else return null;
                }
            };

        }

        if (noSpeChat) {
            filter1 = new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                           int dstart, int dend) {
                    String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                    Pattern pattern = Pattern.compile(speChat);
                    Matcher matcher = pattern.matcher(source.toString());
                    if (matcher.find()) return "";
                    else return null;
                }
            };


            editText.setFilters(new InputFilter[]{filter1});
        }
    }


    @BindingAdapter("editAction")
    public static void setEditAction(TextView textView,
                                     TextView.OnEditorActionListener onEditorActionListener) {
        if (textView != null) {
            textView.setOnEditorActionListener(onEditorActionListener);
        }
    }

    /****************** FlexboxLayout ******************/

    @BindingAdapter(value = {"flowId", "stringList", "adapter"})
    public static void setViews(ConstraintLayout constraintLayout, int flowId,
                                List<?> stringList, FlowAdapter flowAdapter) {
        View viewById = constraintLayout.findViewById(flowId);
        if (viewById instanceof Flow) {
            Flow flow = (Flow) viewById;
            int childCount = constraintLayout.getChildCount();
            boolean hasFoundFlowChild = false;
            for (int i = 0; i < childCount; i++) {
                View childAt = constraintLayout.getChildAt(i);
                if (childAt == null) continue;
                if (FlowAdapter.viewTag.equals(childAt.getTag())) {
                    constraintLayout.removeView(childAt);
                    flow.removeView(childAt);
                    i--;
                }
            }
            if (flowAdapter == null) return;
            if (stringList == null) return;
            int[] referencedIds = flow.getReferencedIds();
            List<Integer> ids = new ArrayList<>();
            for (int i = 0; i < stringList.size(); i++) {
                Object s = stringList.get(i);
                View view = flowAdapter.convertView(constraintLayout, s, i, stringList.size());
                if (view != null) {
                    if (view.getId() == View.NO_ID) {
                        view.setId(View.generateViewId());
                    }
                    view.setTag(FlowAdapter.viewTag);
                    constraintLayout.addView(view);
                    ids.add(view.getId());
                }
            }
            for (int referencedId : referencedIds) {
                ids.add(referencedId);
            }
            int[] newIds = new int[ids.size()];
            for (int i = 0; i < ids.size(); i++) {
                newIds[i] = ids.get(i);
            }
            flow.setReferencedIds(newIds);
            flow.requestLayout();
        }
    }

//
//    //flexbox子view设置
//    @BindingAdapter(value = {"items", "childMarginLeft"}, requireAll = false)
//    public static void setView(FlexboxLayout flex, List<View> items, int marginLeft) {
//        flex.removeAllViews();
//        if (items != null)
//            for (int i = 0; i < items.size(); i++) {
//                if (items.get(i).getParent() != null) continue;// item 已经存在parent，跳过该view
//
//                flex.addView(items.get(i));
//
//                ViewGroup.LayoutParams params = items.get(i).getLayoutParams();
//                if (params instanceof FlexboxLayout.LayoutParams && marginLeft != 0) {
//                    FlexboxLayout.LayoutParams layoutParams = (FlexboxLayout.LayoutParams) params;
//                    if (i == 0) {
//                        layoutParams.setMargins(SizeUtils.dp2px(0), SizeUtils.dp2px(0), SizeUtils.dp2px(0), SizeUtils.dp2px(0));
//                    } else {
//                        layoutParams.setMargins(SizeUtils.dp2px(marginLeft), SizeUtils.dp2px(0), SizeUtils.dp2px(0), SizeUtils.dp2px(0));
//                    }
//                }
//            }
//    }
//
//    //flexbox子view设置
//    @BindingAdapter(value = {"items", "childMarginLeft"}, requireAll = false)
//    public static void setList(FlexboxLayout flex, List<?> items, int marginLeft) {
//        flex.removeAllViews();
//
//        if (items != null)
//            for (int i = 0; i < items.size(); i++) {
//                if (!(items.get(i) instanceof View)) {
//                    return;
//                }
//                View view = (View) items.get(i);
//
//                if (view.getParent() != null) continue;// item 已经存在parent，跳过该view
//
//                flex.addView(view);
//
//                ViewGroup.LayoutParams params = view.getLayoutParams();
//                if (params instanceof FlexboxLayout.LayoutParams && marginLeft != 0) {
//                    FlexboxLayout.LayoutParams layoutParams = (FlexboxLayout.LayoutParams) params;
//                    if (i == 0) {
//                        layoutParams.setMargins(SizeUtils.dp2px(0), SizeUtils.dp2px(0), SizeUtils.dp2px(0), SizeUtils.dp2px(0));
//                    } else {
//                        layoutParams.setMargins(SizeUtils.dp2px(marginLeft), SizeUtils.dp2px(0), SizeUtils.dp2px(0), SizeUtils.dp2px(0));
//                    }
//                }
//            }
//    }

    /****************** RecyclerView ******************/
    @BindingAdapter(
            value = {"adapter", "autoScrollToTopWhenInsert", "autoScrollToBottomWhenInsert"},
            requireAll = false
    )
    public static void bindAdapter(final RecyclerView recyclerView, RecyclerView.Adapter adapter,
                                   final boolean autoScrollToTopWhenInsert,
                                   final boolean autoScrollToBottomWhenInsert) {
        if (recyclerView != null) {
            if (recyclerView.getItemAnimator() instanceof DefaultItemAnimator) {
                ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
            }
            if (recyclerView.getAdapter() == null && adapter != null) {
                recyclerView.setAdapter(adapter);
                adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        if (autoScrollToTopWhenInsert) {
                            recyclerView.scrollToPosition(0);
                        } else if (autoScrollToBottomWhenInsert) {
                            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount());
                        }
                    }
                });
            }
        }
    }

    @BindingAdapter(value = {"itemDecoration"}, requireAll = false)
    public static void addItemDecoration(final RecyclerView recyclerView,
                                         RecyclerView.ItemDecoration itemDecoration) {
        if (recyclerView != null && itemDecoration != null) {
            int count = recyclerView.getItemDecorationCount();
            //清除上一个
            if (count > 0) {
                recyclerView.removeItemDecorationAt(0);
            }
            recyclerView.addItemDecoration(itemDecoration);
        }
    }

    @BindingAdapter(value = {"spacingLeft", "spacingTop", "spacingRight",
                             "spacingBottom"}, requireAll = false)
    public static void addItemDecoration(final RecyclerView recyclerView,
                                         int spacingLeft,
                                         int spacingTop,
                                         int spacingRight,
                                         int spacingBottom) {
        if (recyclerView != null) {
            int count = recyclerView.getItemDecorationCount();
            for (int i = 0; i < count; i++) {
                recyclerView.removeItemDecorationAt(i);
            }
            recyclerView.addItemDecoration(new SpacesItemDecoration(SizeUtils.dp2px(spacingLeft),
                                                                    SizeUtils.dp2px(spacingTop),
                                                                    SizeUtils.dp2px(spacingRight),
                                                                    SizeUtils.dp2px(spacingBottom)));
        }
    }

    @BindingAdapter("spacing")
    public static void setItemDecoration(RecyclerView recyclerView, int spacing) {
        if (recyclerView != null) {
            int count = recyclerView.getItemDecorationCount();
            for (int i = 0; i < count; i++) {
                recyclerView.removeItemDecorationAt(i);
            }
            recyclerView.addItemDecoration(new SpacesItemDecoration(SizeUtils.dp2px(spacing)));
        }
    }

    @BindingAdapter(value = {"verticalSpace", "horizontalSpace"}, requireAll = false)
    public static void setItemDecoration(RecyclerView recyclerView, int verticalSpace,
                                         int horizontalSpace) {

        if (recyclerView != null) {
            int count = recyclerView.getItemDecorationCount();
            for (int i = 0; i < count; i++) {
                recyclerView.removeItemDecorationAt(i);
            }
            recyclerView.addItemDecoration(new SpacesItemDecoration(SizeUtils.dp2px(verticalSpace), SizeUtils.dp2px(horizontalSpace)));
        }
    }


    @BindingAdapter(value = {"setHasFixedSize"}, requireAll = false)
    public static void setHasFixedSize(final RecyclerView recyclerView, boolean b) {
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(b);
        }
    }

    @BindingAdapter(value = {"setNestedScrollingEnabled"}, requireAll = false)
    public static void setNestedScrollingEnabled(final RecyclerView recyclerView, boolean b) {
        if (recyclerView != null) {
            recyclerView.setNestedScrollingEnabled(b);
        }
    }

    @BindingAdapter("allowItemAnimator")
    public static void allowItemAnimator(RecyclerView recyclerView, boolean allow) {
        DefaultItemAnimator itemAnimator = (DefaultItemAnimator) recyclerView.getItemAnimator();
        if (itemAnimator != null) {
            itemAnimator.setSupportsChangeAnimations(allow);
        }

    }

    @BindingAdapter(value = {"onScrolled", "onScrollStateChanged"}, requireAll = false)
    public static void setOnScroll(RecyclerView recyclerView,
                                   final OnScroll onScroll,
                                   final OnScrollStateChanged onScrollStateChanged) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (onScrollStateChanged != null) {
                    onScrollStateChanged.onScrollStateChanged(recyclerView, newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (onScroll != null) {
                    onScroll.onScrolled(recyclerView, dx, dy);
                }
            }
        });
    }

    @BindingAdapter(value = {"scrollBy"}, requireAll = false)
    public static void scrollBy(RecyclerView recyclerView,
                                int x, int y) {
        recyclerView.scrollBy(x, y);
    }

    @BindingAdapter(value = {"color"}, requireAll = false)
    public static void setColor(SwipeRefreshLayout view, @ColorInt int color) {
        view.setColorSchemeColors(color);
    }


    public interface OnScroll {
        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }

    public interface OnScrollStateChanged {
        void onScrollStateChanged(RecyclerView recyclerView, int newState);
    }

    /****************** LottieAnimationView ******************/
//    @BindingAdapter(value = {"animListener"})
//    public static void setAnimListener(LottieAnimationView view, Animator.AnimatorListener listener) {
//        if (view != null) {
//            view.addAnimatorListener(listener);
//        }
//    }
//
//    @BindingAdapter(value = {"startAnim"})
//    public static void startAnim(LottieAnimationView view, boolean isStart) {
//        if (view != null && isStart) {
//            view.setVisibility(View.VISIBLE);
//            view.playAnimation();
//        } else {
//            view.setVisibility(View.GONE);
//        }
//    }

    /****************** ViewPage2 ******************/
    @BindingAdapter("adapter")
    public static void setAdapter(ViewPager2 viewPager2, RecyclerView.Adapter adapter) {
        viewPager2.setAdapter(adapter);
    }

    @BindingAdapter("overScrollMode")
    public static void setOverScrollMode(ViewPager2 viewPager2, int scrollMode) {
        try {
            View childAt = viewPager2.getChildAt(0);
            if (childAt instanceof RecyclerView) {
                childAt.setOverScrollMode(scrollMode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BindingAdapter("OnPageChange")
    public static void setOnPageChangeCallBack(ViewPager2 viewPager2,
                                               ViewPager2.OnPageChangeCallback callback) {
        viewPager2.registerOnPageChangeCallback(callback);
    }

    /****************** StateLayout ******************/
    public static void setStateLayout(StateLayout stateLayout){
        stateLayout.showContent(null);

    }



    /****************** 自定义控件 ******************/
    @BindingAdapter("setCheckedListener")
    public static void setCheckedListener(SwitchCompat switchCompat,
                                          CompoundButton.OnCheckedChangeListener listener) {
        switchCompat.setOnCheckedChangeListener(listener);

    }

}
