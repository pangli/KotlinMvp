package com.zorro.kotlin.samples.ui.login.activity

import android.view.View
import com.lxj.xpopup.XPopup
import com.zorro.kotlin.samples.R
import com.zorro.kotlin.samples.bean.UserInfo
import com.zorro.kotlin.samples.constant.Constant
import com.zorro.kotlin.samples.mvp.contract.LoginContract
import com.zorro.kotlin.samples.mvp.presenter.LoginPresenter
import com.zorro.kotlin.samples.network.modelToJsonString
import com.zorro.kotlin.samples.popup.ManMachineVerificationPopupView
import com.zorro.kotlin.samples.popup.OnVerificationListener
import com.zorro.kotlin.samples.ui.base.ZorroBaseMvpActivity
import com.zorro.kotlin.samples.utils.ActivityHelper
import kotlinx.android.synthetic.main.activity_login.*


/**
 * Created by Zorro on 2019/1/9.
 * 备注:登录界面
 */
class LoginActivity : ZorroBaseMvpActivity<LoginContract.View, LoginContract.Presenter>(),
    LoginContract.View {
    private var inputUsername: String = ""
    private var inputPassword: String = ""
    override fun createPresenter(): LoginContract.Presenter = LoginPresenter()

    override fun attachLayoutRes(): Int = R.layout.activity_login


    override fun initView() {
        super.initView()

    }

    override fun bindListener() {
        btn_login.setOnClickListener(onClickListener)
        btn_forget_password.setOnClickListener(onClickListener)
    }

    override fun initData() {
        super.initData()

    }


    override fun start() {
    }


    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btn_login -> {
                login()
            }
            R.id.btn_forget_password -> {

            }

        }
    }

    /**
     * Login
     */
    private fun login() {
        if (validate()) {
            showManMachineVerificationPopup()
        }
    }

    /**
     * Check UserName and PassWord
     */
    private fun validate(): Boolean {
        inputUsername = et_username.text.toString().trim()
        inputPassword = et_password.text.toString().trim()
        if (inputUsername.isEmpty()) {
            et_username.error = getString(R.string.email_not_empty)
            return false
        }
        if (inputPassword.isEmpty()) {
            et_password.error = getString(R.string.password_not_empty)
            return false
        }
        return true
    }

    /**
     * 登录用户信息接口返回数据
     */
    override fun setLoginResult(userInfo: UserInfo) {
        isLogin = true
        token = Constant.token
        userInfoJson = modelToJsonString(UserInfo::class.java, userInfo)
        this@LoginActivity.userInfo = userInfo
        ActivityHelper.jumpToMainActivity(this)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.famous_hall_none, R.anim.famous_hall_out)
    }

    /**
     * 显示项目咨询弹窗
     */
    private fun showManMachineVerificationPopup() {
        val popup =
            ManMachineVerificationPopupView(this, object : OnVerificationListener {
                override fun onResultToKen(token: String) {
                    mPresenter?.login(inputUsername, inputPassword, token)
                }
            })
        XPopup.Builder(this)
            .moveUpToKeyboard(false)
            .hasStatusBarShadow(true)
            .asCustom(popup)
            .show()
    }

}