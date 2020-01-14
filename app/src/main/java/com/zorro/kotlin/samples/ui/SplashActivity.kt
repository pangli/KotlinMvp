package com.zorro.kotlin.samples.ui


import android.Manifest
import android.app.ProgressDialog
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.LinearLayout
import android.widget.TextView
import com.zorro.kotlin.baselibs.net.showSnackMsg
import com.zorro.kotlin.baselibs.http.download.DownLoadManager
import com.zorro.kotlin.baselibs.http.download.ProgressCallBack
import com.zorro.kotlin.baselibs.utils.Preference
import com.zorro.kotlin.baselibs.utils.VersionUtils
import com.zorro.kotlin.samples.BuildConfig
import com.zorro.kotlin.samples.R
import com.zorro.kotlin.samples.constant.Constant
import com.zorro.kotlin.samples.mvp.contract.TestContract
import com.zorro.kotlin.samples.mvp.presenter.TestPresenter
import com.zorro.kotlin.samples.ui.base.ZorroBaseMvpActivity
import com.zorro.kotlin.samples.utils.ActivityHelper
import kotlinx.android.synthetic.main.activity_splash.*
import okhttp3.ResponseBody
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

/**
 * Created by Zorro on 2019/10/15.
 * 备注：启动页
 */
class SplashActivity : ZorroBaseMvpActivity<TestContract.View, TestContract.Presenter>(),
    TestContract.View {

    private var abandonUpdateVersion: String = BuildConfig.VERSION_NAME
    private var isShowGuide: Boolean by Preference(Constant.GUIDE_VERSION_NAME, false)


    override fun createPresenter(): TestContract.Presenter = TestPresenter()
    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        jumpToNextActivity()
    }


    private var alphaAnimation: AlphaAnimation? = null


    override fun attachLayoutRes(): Int {
        return R.layout.activity_splash
    }


    override fun initView() {
        super.initView()
        setBlackStatusBar()
    }

    override fun bindListener() {
    }

    override fun start() {
        animation()
    }


    /**
     * 启动页动画
     */
    private fun animation() {
        alphaAnimation = AlphaAnimation(0.3F, 1.0F)
        alphaAnimation?.run {
            duration = 2000
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    jumpToNextActivity()
                }

                override fun onAnimationStart(p0: Animation?) {
                }
            })
        }
        root_layout.startAnimation(alphaAnimation)
    }

    /**
     * 跳转到登录页或者首页
     */
    private fun jumpToNextActivity() {
        if (isLogin) {
            Constant.token = token
            ActivityHelper.jumpToMainActivity(this)
        } else {
            ActivityHelper.jumpToLoginActivity(this)
        }
        finish()
    }

    override fun onDestroy() {
        alphaAnimation?.cancel()
        alphaAnimation = null
        dismissListener = null
        super.onDestroy()
    }


    fun geyForceUpdatePopWindow(description: String?) {
        val vew = layoutInflater.inflate(R.layout.dialog_common, null)
        val builder = AlertDialog.Builder(this).setView(vew)
            .setCancelable(false)
            .create()
        vew.findViewById<LinearLayout>(R.id.dialog_title).visibility = View.VISIBLE
        vew.findViewById<TextView>(R.id.dialog_title_text).text = "版本升级"
        vew.findViewById<TextView>(R.id.dialog_content_text).text = description
        vew.findViewById<TextView>(R.id.dialog_sure).text = "去升级"
        vew.findViewById<TextView>(R.id.dialog_sure).setOnClickListener {
            filePermission()
            builder.dismiss()
        }
        vew.findViewById<TextView>(R.id.dialog_cancel).visibility = View.GONE
        builder.show()

    }

    private var dismissListener: DialogInterface.OnDismissListener? = null

    fun geyUpdatePopWindow(description: String?) {
        val vew = layoutInflater.inflate(R.layout.dialog_common, null)
        val builder = AlertDialog.Builder(this).setView(vew)
            .create()
        vew.findViewById<LinearLayout>(R.id.dialog_title).visibility = View.VISIBLE
        vew.findViewById<TextView>(R.id.dialog_title_text).text = "版本升级"
        vew.findViewById<TextView>(R.id.dialog_content_text).text = description
        vew.findViewById<TextView>(R.id.dialog_sure).text = "去升级"
        vew.findViewById<TextView>(R.id.dialog_cancel).text = "放弃"
        vew.findViewById<TextView>(R.id.dialog_sure).setOnClickListener {
            filePermission()
            builder.dismiss()
        }
        vew.findViewById<TextView>(R.id.dialog_cancel).setOnClickListener {
            var abandon: Boolean by Preference(abandonUpdateVersion, false)//是否放弃更新
            abandon = true
            builder.dismiss()
        }
        dismissListener = DialogInterface.OnDismissListener {
            jumpToNextActivity()
        }
        builder.setOnDismissListener(dismissListener)
        builder.show()

    }

    private var uploadApkUrl: String? = null


    /**
     * 文件权限
     */
    @AfterPermissionGranted(101)
    private fun filePermission() {
        val perms = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (hasPermissions(*perms)) {//检查是否获取该权限
            //获取权限
            downFile(uploadApkUrl)
        } else {
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "读写文件权限未开启,请先在设置中打开存储权限", 101, *perms)
        }
    }

    private fun downFile(url: String?) {
        val destFileDir = application.cacheDir.path
        val destFileName = System.currentTimeMillis().toString() + ".apk"
        val progressDialog = ProgressDialog(this)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.setTitle("正在下载更新...")
        progressDialog.setCancelable(false)
        DownLoadManager.getInstance()
            .load(url, object : ProgressCallBack<ResponseBody>(destFileDir, destFileName) {
                override fun onStart() {
                    progressDialog.show()
                }

                override fun onCompleted() {
                    progressDialog.dismiss()
                }

                override fun onSuccess(responseBody: ResponseBody?, saveFile: File?) {
                    VersionUtils.installApk(this@SplashActivity, saveFile)
                }

                override fun progress(progress: Long, total: Long, tag: String) {
                    progressDialog.max = (total / 1024).toInt()
                    progressDialog.progress = (progress / 1024).toInt()
                    progressDialog.setProgressNumberFormat("%1d KB/%2d KB")
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    showSnackMsg("更新失败！请重试")
                    progressDialog.dismiss()
                }
            })

    }

}