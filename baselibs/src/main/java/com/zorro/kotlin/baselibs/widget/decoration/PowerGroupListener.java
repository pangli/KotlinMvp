package com.zorro.kotlin.baselibs.widget.decoration;

import android.view.View;

/**
 * Created by Zorro
 * Created date 2019/10/18
 * 显示自定义View的Group监听
 */

public interface PowerGroupListener extends GroupListener {

    View getGroupView(int position);
}
