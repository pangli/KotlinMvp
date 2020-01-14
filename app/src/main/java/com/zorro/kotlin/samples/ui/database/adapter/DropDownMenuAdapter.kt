package com.zorro.kotlin.samples.ui.database.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

import com.zorro.kotlin.baselibs.widget.dropdownmenu.DropdownListItemView
import com.zorro.kotlin.samples.R
import com.zorro.kotlin.samples.bean.OtherFilterData


/**
 *
 * Created by Zorro on 2019/12/19.
 * 备注：筛选
 */
class DropDownMenuAdapter(data: List<OtherFilterData>) :
    BaseQuickAdapter<OtherFilterData, BaseViewHolder>(R.layout.dropdown_list_item, data) {
    private var checkItemPosition: Int = 0
    //单选逻辑
    fun setCheckItem(position: Int) {
        if (checkItemPosition != position) {
            notifyItemChanged(checkItemPosition)
            notifyItemChanged(position)
        }
        checkItemPosition = position
    }

    override fun convert(helper: BaseViewHolder, item: OtherFilterData?) {
        item ?: return
        helper.layoutPosition
        if (checkItemPosition == helper.layoutPosition) {
            helper.getView<DropdownListItemView>(R.id.dropdown_item).bind(item.name, true)
        } else {
            helper.getView<DropdownListItemView>(R.id.dropdown_item).bind(item.name, false)
        }

    }
}