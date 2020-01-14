package com.zorro.kotlin.baselibs.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zorro.kotlin.baselibs.R;


/**
 * Created by Zorro on 2019/3/27 16:05
 * 备注：审批RowView
 */
public class ItemViewAdd extends RelativeLayout {
    private TextView leftTextView, rightTextView;
    private View viewLine;
    private String leftString;

    public ItemViewAdd(Context context) {
        super(context, null);
    }

    public ItemViewAdd(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemViewAdd(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        addView();
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ItemView);
        leftString = a.getString(R.styleable.ItemView_leftText);
        a.recycle();
    }

    private void addView() {
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        //
        leftTextView = new TextView(getContext());
        leftTextView.setTextSize(14f);
        leftTextView.setGravity(Gravity.CENTER);
        //字体加粗
        leftTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
//        TextPaint tp = leftTextView.getPaint();
//        tp.setFakeBoldText(true);
        //
        leftTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_333333));
        LayoutParams leftLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        leftLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        leftLayoutParams.setMarginStart(dpTpPx(15));
        leftTextView.setLayoutParams(leftLayoutParams);
        addView(leftTextView, 0);
        //
        rightTextView = new TextView(getContext());
        rightTextView.setTextSize(14f);
        rightTextView.setGravity(Gravity.CENTER);
        rightTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗

        Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.icon_add);// 找到资源图片
        // 这一步必须要做，否则不会显示。
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 设置图片宽高
        rightTextView.setCompoundDrawables(drawable, null, null, null);// 设置到控件中
        rightTextView.setText("  添加");
        rightTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_10b2ff));
        LayoutParams rightLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        rightLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        rightLayoutParams.setMarginEnd(dpTpPx(12));
        rightTextView.setLayoutParams(rightLayoutParams);
        addView(rightTextView, 1);

        //
        viewLine = new View(getContext());
        viewLine.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_eeeeee));
        LayoutParams lineLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, dpTpPx(1));
        lineLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lineLayoutParams.setMarginStart(dpTpPx(15));
        viewLine.setLayoutParams(lineLayoutParams);
        addView(viewLine, 2);
        bindContent(leftString);
    }

    public void bindContent(String leftString) {
        if (leftTextView != null && !TextUtils.isEmpty(leftString)) {
            leftTextView.setText(leftString);
        }
    }

    public void clearRightContent() {
        rightTextView.setText("");
    }

    public void setRightGone(boolean visible) {
        if (visible) {
            rightTextView.setVisibility(View.VISIBLE);
        } else {
            rightTextView.setVisibility(View.GONE);
        }

    }


    public void bindContent(String leftString, boolean visible) {
        if (leftTextView != null && !TextUtils.isEmpty(leftString)) {
            leftTextView.setText(leftString);
        }
        if (visible) {
            rightTextView.setVisibility(View.VISIBLE);
        } else {
            rightTextView.setVisibility(View.GONE);
        }
    }

    public void setLeftString(String leftString) {
        if (leftTextView != null && !TextUtils.isEmpty(leftString)) {
            leftTextView.setText(leftString);
        }
    }

    public void setRightString(String rightString) {
        if (rightTextView != null && !TextUtils.isEmpty(rightString)) {
            rightTextView.setText(rightString);
        }
    }

    public String getRightString() {
        if (rightTextView != null) {
            return rightTextView.getText().toString();
        }
        return "";
    }

    public void showLine(boolean showLine) {
        if (viewLine != null) {
            if (showLine) {
                viewLine.setVisibility(VISIBLE);
            } else {
                viewLine.setVisibility(GONE);
            }
        }
    }

    public int dpTpPx(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }
}
