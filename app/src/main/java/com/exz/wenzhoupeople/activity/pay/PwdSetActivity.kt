package com.exz.wenzhoupeople.activity.pay

import android.app.Activity
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import cn.com.szw.lib.myframework.app.MyApplication
import cn.com.szw.lib.myframework.app.MyApplication.salt
import cn.com.szw.lib.myframework.base.BaseActivity
import cn.com.szw.lib.myframework.config.Constants
import cn.com.szw.lib.myframework.utils.net.NetEntity
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback
import cn.com.szw.lib.myframework.view.pwd.widget.OnPasswordInputFinish
import cn.com.szw.lib.myframework.view.pwd.widget.PasswordView
import com.blankj.utilcode.util.EncryptUtils
import com.exz.wenzhoupeople.R
import com.exz.wenzhoupeople.config.Urls
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import kotlinx.android.synthetic.main.action_bar_tv.*
import kotlinx.android.synthetic.main.pwd_activity_set.*
import org.jetbrains.anko.toast
import java.util.*

/**
 * Created by 史忠文
 * on 2017/5/4.
 */

class PwdSetActivity : BaseActivity(), OnPasswordInputFinish {
    var type = ""
    private var oldPwd: String=""
    private var newPwd: String=""
    private var  url: String=""
    lateinit var mPasswordView:PasswordView
    override fun initToolbar(): Boolean {
        toolbar.setContentInsetsAbsolute(0, 0)
        mTitle.textSize = 16f
        mTitle.text = "修改密码"
        mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray))
        mLeftImg.setOnClickListener { finish() }
        return true
    }

    override fun setInflateId(): Int = R.layout.pwd_activity_set

    override fun init() {
        type = if (TextUtils.isEmpty(type)) intent.getStringExtra(PwdSetActivity_Type) else type
        mPasswordView = findViewById(R.id.mPasswordView) as PasswordView
        mPasswordView.virtualKeyboardView.layoutBack.visibility = View.GONE
        mPasswordView.setInputFinish(this)
        passReset()
    }

    private fun passReset() {
        when (type) {
            PwdSetActivity_Type_1 -> {info.text = "输入支付密码，以验证身份"
                                url= Urls.PayPwdVerify
            }
            PwdSetActivity_Type_2 -> {info.text = "输入新密码"
            }
            PwdSetActivity_Type_3 -> {info.text = "重新输入新密码"
                //                url=Urls.ModifyPayPwd;
                url=Urls.ModifyPayPwd
            }
        }

        mPasswordView.reset()
    }

    override fun inputFinish(password: String) {
        when (type) {
            PwdSetActivity_Type_1 -> setPayPwd(password, "", "")
            PwdSetActivity_Type_2 -> if (TextUtils.isEmpty(intent.getStringExtra(PwdSetActivity_Security_Code))) {
                newPwd = password
                type = PwdSetActivity_Type_3
                passReset()
            } else {
                //                    url=Urls.SetPayPwd;
                url=Urls.SetPayPwd
                setPayPwd(password, "", "")
            }
            PwdSetActivity_Type_3 -> if (newPwd == password) {
                setPayPwd("", oldPwd, newPwd)
            } else {
                Toast.makeText(mContext, "两次输入密码不一致", Toast.LENGTH_SHORT).show()
                newPwd = ""
                type = PwdSetActivity_Type_2
                passReset()
            }
        }

    }

    /**
     * 设置支付密码
     */
    private fun setPayPwd(payPwd: String, oldPayPwd: String, newPayPwd: String) {
        val map = HashMap<String, String>()
        map.put("id", MyApplication.getLoginUserId())
        if (!TextUtils.isEmpty(payPwd)) {
            if (TextUtils.isEmpty(intent.getStringExtra(PwdSetActivity_Security_Code))) {
                //验证身份
                map.put("payPwd", payPwd)
                map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.getLoginUserId()+payPwd, salt).toLowerCase())
            } else {//设置密码
                map.put("id", MyApplication.getLoginUserId())
                map.put("payPwd", payPwd)
//                map.put("attribute", if (IsSetPwd)"3" else "2")
                map.put("code", intent.getStringExtra(PwdSetActivity_Security_Code))
                map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.getLoginUserId()+intent.getStringExtra(PwdSetActivity_Security_Code)+ payPwd, salt).toLowerCase())
            }

        } else {//重置密码
            map.put("oldPayPwd", oldPayPwd)
            map.put("newPayPwd", newPayPwd)
            map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.getLoginUserId()+oldPayPwd+newPayPwd, salt).toLowerCase())
        }
        OkGo.post<NetEntity<String>>(url).tag(this)
                .params(map)
                .execute(object : DialogCallback<NetEntity<String>>(this) {

                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        when (type) {
                            PwdSetActivity_Type_1 -> {
                                if (response.body().code == Constants.NetCode.SUCCESS) {
                                if (response.body().data=="1"){
                                    oldPwd = payPwd
                                    type = PwdSetActivity_Type_2
                                } else {
                                    Toast.makeText(mContext, "支付密码错误请重试", Toast.LENGTH_SHORT).show()
                                }
                                }
                                init()
                            }
                            PwdSetActivity_Type_2, PwdSetActivity_Type_3 -> {
                                Toast.makeText(mContext, response.body().message, Toast.LENGTH_SHORT).show()
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                        }
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        toast(response.exception.message.toString())
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                })

    }

    companion object {
        var IsSetPwd=false
        val PwdSetActivity_Type = "PwdSetActivity_Type"
        val PwdSetActivity_Type_1 = "PwdSetActivity_Type_1"//原密码
        val PwdSetActivity_Type_2 = "PwdSetActivity_Type_2"//新密码
        val PwdSetActivity_Type_3 = "PwdSetActivity_Type_3"//确认密码
        val PwdSetActivity_Security_Code = "PwdSetActivity_Security_Code"//验证码
    }


}
