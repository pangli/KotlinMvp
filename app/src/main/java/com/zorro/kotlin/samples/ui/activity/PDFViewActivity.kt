package com.zorro.kotlin.samples.ui.activity


import android.view.View
import com.zorro.kotlin.samples.R
import com.zorro.kotlin.samples.ui.base.ZorroBaseActivity
import com.zorro.kotlin.samples.utils.ActivityHelper
import kotlinx.android.synthetic.main.activity_pdf_view.*
import kotlinx.android.synthetic.main.include_toolbar_layout.*


import java.io.File

/**
 * Created by Zorro on 2019/11/5.
 * 备注：本地PDF查看(先下载)
 */
class PDFViewActivity : ZorroBaseActivity() {

    companion object {
        const val FILE_PATH = "file_path"
    }

    private var filePath: String? = null


    override fun attachLayoutRes(): Int = R.layout.activity_pdf_view

    override fun initView() {
        whiteImmersionBar(toolbar)
        tv_title.text = "文件预览"
    }

    override fun initData() {
        super.initData()
        val bundle = intent.getBundleExtra(ActivityHelper.DATA)
        filePath = bundle.getString(FILE_PATH, "")
    }

    override fun bindListener() {
        iv_left.setOnClickListener(onClickListener)
    }

    override fun start() {
        if (!filePath.isNullOrBlank())
            showPDFResult()
    }

    /**
     * 打开文件
     */
    private fun showPDFResult() {
        pdf_view.fromFile(File(filePath))
            .defaultPage(0)//默认展示第一页
            .onLoad {
            }//监听加载完成
            .onError { }//监听加载失败
            .load()
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.iv_left -> {
                finish()
            }
        }
    }

}