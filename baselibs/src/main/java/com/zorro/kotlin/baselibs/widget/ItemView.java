package com.zorro.kotlin.baselibs.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
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
public class ItemView extends RelativeLayout {
    private TextView leftTextView, rightTextView;
    private View viewLine;
    private String leftString, rightString;
    private boolean showLine;

    public ItemView(Context context) {
        super(context, null);
    }

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        addView();
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ItemView);
        leftString = a.getString(R.styleable.ItemView_leftText);
        rightString = a.getString(R.styleable.ItemView_rightText);
        showLine = a.getBoolean(R.styleable.ItemView_showLine, false);
        a.recycle();
    }

    private void addView() {
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        //
        leftTextView = new TextView(getContext());
        leftTextView.setId(R.id.itemview_left_text_view);
        leftTextView.setTextSize(14f);
        leftTextView.setGravity(Gravity.CENTER);
        leftTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_333333));
        LayoutParams leftLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        leftLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        leftLayoutParams.setMarginStart(dpTpPx(20));
        leftTextView.setLayoutParams(leftLayoutParams);
        addView(leftTextView, 0);
        //
        rightTextView = new TextView(getContext());
        rightTextView.setTextSize(14f);
        rightTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        rightTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_333333));
        rightTextView.setMaxLines(1);
        rightTextView.setEllipsize(TextUtils.TruncateAt.END);
        LayoutParams rightLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        rightLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        rightLayoutParams.addRule(RelativeLayout.END_OF, leftTextView.getId());
        rightLayoutParams.setMarginStart(dpTpPx(20));
        rightLayoutParams.setMarginEnd(dpTpPx(20));
        rightTextView.setLayoutParams(rightLayoutParams);
        addView(rightTextView, 1);
        //
        viewLine = new View(getContext());
        viewLine.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_eeeeee));
        LayoutParams lineLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, dpTpPx(1));
        lineLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lineLayoutParams.setMarginStart(dpTpPx(20));
        viewLine.setLayoutParams(lineLayoutParams);
        addView(viewLine, 2);
        bindContent(leftString, rightString, showLine);
    }

    public void bindContent(String leftString, String rightString, boolean showLine) {
        if (leftTextView != null) {
            if (!TextUtils.isEmpty(leftString)) {
                leftTextView.setText(leftString);
            } else {
                leftTextView.setText("");
            }
        }
        if (rightTextView != null) {
            if (!TextUtils.isEmpty(rightString)) {
                rightTextView.setText(rightString);
            } else {
                rightTextView.setText("");
            }
        }
        if (viewLine != null) {
            if (showLine) {
                viewLine.setVisibility(VISIBLE);
            } else {
                viewLine.setVisibility(GONE);
            }
        }
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

    public void setRightString(String rightString) {
        if (rightTextView != null) {
            if(TextUtils.isEmpty(rightString)){
                rightTextView.setText("---");
            }else {
                rightTextView.setText(rightString);
            }

        }

    }

    public void setRightString(String rightString,Boolean isClick) {
        if (rightTextView != null) {
            rightTextView.setText(rightString);
        }
        if(isClick){
            if (rightTextView != null) {
                rightTextView.setTextColor(getResources().getColor(R.color.color_3c73b1));
                rightTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            }
        }

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
