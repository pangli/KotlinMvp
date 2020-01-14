package com.zorro.kotlin.baselibs.widget.dropdownmenu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.zorro.kotlin.baselibs.R;


public class DropdownListItemView extends AppCompatTextView {
    public DropdownListItemView(Context context) {
        this(context, null);
    }

    public DropdownListItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropdownListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bind(CharSequence text, boolean checked) {
        setText(text);
        if (checked) {
            setTextColor(getResources().getColor(R.color.color_10b2ff));
            Drawable icon = getResources().getDrawable(R.mipmap.ic_task_status_list_check);
            setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
        } else {
            setTextColor(getResources().getColor(R.color.color_666666));
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }


}
