package com.zorro.kotlin.samples.ui.database.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zorro.kotlin.baselibs.utils.CacheDataUtil
import com.zorro.kotlin.samples.R
import com.zorro.kotlin.samples.bean.DatabaseFile


/**
 * @author Zorro
 * @date 2019/12/19
 * @desc 资料Adapter
 */
class DatabaseAdapter :
    BaseQuickAdapter<DatabaseFile, BaseViewHolder>(R.layout.item_database_file) {
    override fun convert(helper: BaseViewHolder, item: DatabaseFile?) {
        item ?: return
        helper.setText(R.id.tv_file_name, item.file_name)
            .setText(
                R.id.tv_file_info,
                "${CacheDataUtil.getFormatSize(item.file_size.toDouble())}  ${item.create_time}"
            )

    }
}