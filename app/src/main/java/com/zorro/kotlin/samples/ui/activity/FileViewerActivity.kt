package com.zorro.kotlin.samples.ui.activity

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.FrameLayout
import com.orhanobut.logger.Logger
import com.tencent.smtt.sdk.TbsReaderView
import com.zorro.kotlin.baselibs.utils.FileUtils
import com.zorro.kotlin.samples.R
import com.zorro.kotlin.samples.mvp.contract.TestContract
import com.zorro.kotlin.samples.mvp.presenter.TestPresenter
import com.zorro.kotlin.samples.ui.base.ZorroBaseMvpActivity
import com.zorro.kotlin.samples.utils.ActivityHelper
import kotlinx.android.synthetic.main.activity_file_viewer.*
import kotlinx.android.synthetic.main.include_toolbar_layout.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by Zorro on 2019/12/25.
 * 备注：文件查看
 */
class FileViewerActivity : ZorroBaseMvpActivity<TestContract.View, TestContract.Presenter>(),
    TestContract.View, TbsReaderView.ReaderCallback {

    companion object {
        const val SHARED = "shared"
        const val FILE_NAME = "file_name"
        const val FILE_PATH = "file_path"
    }

    private var shared: Boolean = false
    private var fileName: String = ""
    private var filePath: String = ""
    private var tbsReaderView: TbsReaderView? = null

    override fun createPresenter(): TestContract.Presenter = TestPresenter()

    override fun attachLayoutRes(): Int = R.layout.activity_file_viewer
    override fun initView() {
        super.initView()
        setBlackStatusBar(toolbar)
        iv_left.visibility = View.VISIBLE
        iv_right.setImageResource(R.mipmap.fenxiang)
        tbsReaderView = TbsReaderView(this, this)
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        tbsReaderView?.layoutParams = layoutParams
        container.addView(tbsReaderView)
    }

    override fun bindListener() {
        iv_left.setOnClickListener(onClickListener)
        iv_right.setOnClickListener(onClickListener)
    }

    override fun initData() {
        super.initData()
        val bundle = intent.getBundleExtra(ActivityHelper.DATA)
        shared = bundle.getBoolean(SHARED, false)
        fileName = bundle.getString(FILE_NAME, "")
        filePath = bundle.getString(FILE_PATH, "")
    }

    override fun start() {
        tv_title.text = fileName
        if (shared) {
            iv_right.visibility = View.VISIBLE
        } else {
            iv_right.visibility = View.GONE
        }
        if (filePath.isNullOrBlank()) {
            showMsg(getString(R.string.file_not_exist))
            finish()
        }
        filePermission()
    }


    /**
     * 文件权限
     */
    @AfterPermissionGranted(102)
    private fun filePermission() {
        val perms = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (hasPermissions(*perms)) {//检查是否获取该权限
            //获取权限成功
            openFile()
        } else {
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "读写文件权限未开启,请先在设置中打开存储权限", 102, *perms)
        }
    }

    /**
     * 打开文件
     */
    private fun openFile() {
        val bundle = Bundle()
        bundle.putString("filePath", filePath)//文件地址
        //存放临时文件的目录。运行后，会在tempPath的目录下生成.tbs...的文件
        bundle.putString("tempPath", Environment.getExternalStorageDirectory().absolutePath)
        // preOpen 需要文件后缀名 用以判断是否支持
        val result: Boolean =
            tbsReaderView?.preOpen(FileUtils.getFileExtension(filePath), true) ?: false
        if (result) {
            tbsReaderView?.openFile(bundle)
        } else {
            showMsg("文件类型不支持")
        }
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.iv_left -> {
                finish()
            }
            R.id.iv_right -> {
                ActivityHelper.jumpToConversationActivity(this)
            }
        }
    }

    /**
     * 文件浏览回调
     */
    override fun onCallBackAction(int: Int?, long1: Any?, long2: Any?) {
        Logger.d("onCallBackAction----$int----$long1----$long2")
    }

    override fun onDestroy() {
        tbsReaderView?.onStop()
        tbsReaderView?.removeAllViews()
        container.removeAllViews()
        tbsReaderView = null
        super.onDestroy()
    }
}