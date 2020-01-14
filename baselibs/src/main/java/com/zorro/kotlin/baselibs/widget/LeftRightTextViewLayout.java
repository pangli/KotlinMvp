package com.zorro.kotlin.baselibs.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v4.media.RatingCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zorro.kotlin.baselibs.R;


/**
 * Created by Zorro on 2019/8/16 15:59
 * 备注：左右TestView布局
 */
public class LeftRightTextViewLayout extends LinearLayout {

    private TextView leftTextView, rightTextView;
    private String leftString, rightString;
    private int leftTextSize = 14, rightTextSize = 14, rightMaxLines = Integer.MAX_VALUE;
    private int leftTextColor = 0xff333333, rightTextColor = 0xff333333;

    public LeftRightTextViewLayout(Context context) {
        super(context, null);
    }

    public LeftRightTextViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeftRightTextViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        init(context, attrs, defStyleAttr);
        addView();
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LeftRightTextViewLayout, defStyleAttr, 0);
        leftString = typedArray.getString(R.styleable.LeftRightTextViewLayout_leftTextContent);
        rightString = typedArray.getString(R.styleable.LeftRightTextViewLayout_rightTextContent);
        leftTextColor = typedArray.getColor(R.styleable.LeftRightTextViewLayout_leftTextColor, leftTextColor);
        rightTextColor = typedArray.getColor(R.styleable.LeftRightTextViewLayout_rightTextColor, rightTextColor);
        leftTextSize = typedArray.getInteger(R.styleable.LeftRightTextViewLayout_leftTextSize, leftTextSize);
        rightTextSize = typedArray.getInteger(R.styleable.LeftRightTextViewLayout_rightTextSize, rightTextSize);
        rightMaxLines = typedArray.getInteger(R.styleable.LeftRightTextViewLayout_rightMaxLines, rightMaxLines);
        typedArray.recycle();
    }

    private void addView() {
        //setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        //
        leftTextView = new TextView(getContext());
        leftTextView.setId(R.id.itemview_left_text_view);
        leftTextView.setTextSize(leftTextSize);
        leftTextView.setGravity(Gravity.CENTER);
        leftTextView.setTextColor(leftTextColor);
        LayoutParams leftLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        leftLayoutParams.setMarginStart(dpTpPx(15));
        leftTextView.setLayoutParams(leftLayoutParams);
        addView(leftTextView, 0);
        //
        rightTextView = new TextView(getContext());
        rightTextView.setTextSize(rightTextSize);
        rightTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        rightTextView.setTextColor(rightTextColor);
        rightTextView.setMaxLines(rightMaxLines);
        if (rightMaxLines == 1) {
            rightTextView.setEllipsize(TextUtils.TruncateAt.END);
        }
        LayoutParams rightLayoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        rightLayoutParams.setMarginStart(dpTpPx(15));
        rightLayoutParams.setMarginEnd(dpTpPx(15));
        rightTextView.setLayoutParams(rightLayoutParams);
        addView(rightTextView, 1);

        bindContent(leftString, rightString);
    }


    public void bindContent(String leftString, String rightString) {
        if (leftTextView != null) {
            leftTextView.setText(leftString);
        }
        if (rightTextView != null) {
            rightTextView.setText(rightString);
        }
    }

    public void setLeftString(String leftString) {
        if (leftTextView != null) {
            leftTextView.setText(leftString);
        }
    }

    public void setLeftTextStyle(@RatingCompat.Style int style) {
        if (leftTextView != null) {
//            TextPaint tp = leftTextView.getPaint();
//            tp.setFakeBoldText(bold);
            leftTextView.setTypeface(Typeface.defaultFromStyle(style));
        }
    }

    public void setRightTextStyle(@RatingCompat.Style int style) {
        if (rightTextView != null) {
//            TextPaint tp = rightTextView.getPaint();
//            tp.setFakeBoldText(bold);
            rightTextView.setTypeface(Typeface.defaultFromStyle(style));
        }
    }

    public void setRightString(String rightString) {
        if (rightTextView != null) {
            if (TextUtils.isEmpty(rightString)) {
                rightTextView.setText("---");
            } else {
                rightTextView.setText(rightString);
            }

        }
    }


    public int dpTpPx(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }

}
