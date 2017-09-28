package com.exz.wenzhoupeople.activity.pay

import android.app.Activity
import android.content.Intent
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import cn.com.szw.lib.myframework.app.MyApplication.salt
import cn.com.szw.lib.myframework.base.BaseActivity
import cn.com.szw.lib.myframework.config.Constants
import cn.com.szw.lib.myframework.config.PreferencesService
import cn.com.szw.lib.myframework.observer.SmsContentObserver
import cn.com.szw.lib.myframework.utils.StringUtil
import cn.com.szw.lib.myframework.utils.net.NetEntity
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.SizeUtils
import com.exz.wenzhoupeople.R
import com.exz.wenzhoupeople.config.Urls
import com.exz.wenzhoupeople.utils.SPutils
import com.exz.wenzhoupeople.utils.SZWUtils
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import kotlinx.android.synthetic.main.action_bar_tv.*
import kotlinx.android.synthetic.main.pwd_activity_get_code.*
import org.jetbrains.anko.toast
import java.util.*

class PwdGetCodeActivity : BaseActivity(), View.OnClickListener {


    private lateinit var countDownTimer: CountDownTimer
    private val time = 120000//倒计时时间
    private val downKey = "R"
    private lateinit var smsContentObserver: SmsContentObserver

    override fun initToolbar(): Boolean {
        toolbar.setContentInsetsAbsolute(0, 0)
        mTitle.textSize = 16f
        mTitle.text = "验证手机"
        mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray))
        return true
    }

    override fun init() {
        ed_phone.setText(SZWUtils.hideMidPhone(SPutils.getString(mContext,"phone")))
        val l = System.currentTimeMillis()
        if (PreferencesService.getDownTimer(this, downKey) in 1..(l - 1)) {
            downTimer(time - (l - PreferencesService.getDownTimer(this, downKey)))
        }
        // 先判断是否有权限。
        locationAndSMSWithCheck(null, false)
        // 注册读取短信Observer
        smsContentObserver = SZWUtils.registerSMS(mContext, SZWUtils.patternCode(mContext, get_code))
        mLeftImg.setOnClickListener(this)
        get_code.setOnClickListener(this)
        bt_next.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 解除注册读取短信Observer
        contentResolver.unregisterContentObserver(smsContentObserver)

    }

    private fun downTimer(l: Long) {
        countDownTimer = object : CountDownTimer(l, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                resetTimer(false, millisUntilFinished)
            }

            override fun onFinish() {
                resetTimer(true, java.lang.Long.MIN_VALUE)
            }
        }
        countDownTimer.start()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.mLeftImg -> finish()
            R.id.get_code -> getSecurityCode()
            R.id.bt_next -> check()
        }
    }

    private fun resetTimer(b: Boolean, millisUntilFinished: Long) {
        if (b) {
            countDownTimer.cancel()
            get_code.text = "点击获取"
            get_code.isClickable = true
            get_code.setTextColor(ContextCompat.getColor(this, R.color.blue2))
            get_code.setBackgroundResource(R.drawable.login_bg_line)
            get_code.setPadding(SizeUtils.dp2px(10f), SizeUtils.dp2px(10f), SizeUtils.dp2px(10f), SizeUtils.dp2px(10f))
            PreferencesService.setDownTimer(this, downKey, 0)
        } else {
            get_code.isClickable = false
            get_code.background = ContextCompat.getDrawable(this, R.drawable.login_grey_lin)
            get_code.setPadding(SizeUtils.dp2px(10f), SizeUtils.dp2px(10f), SizeUtils.dp2px(10f), SizeUtils.dp2px(10f))
            get_code.setTextColor(ContextCompat.getColor(this, R.color.MaterialGrey400))
            get_code.text = "重发(${millisUntilFinished / 1000})s"
        }

    }

    override fun setInflateId(): Int = R.layout.pwd_activity_get_code


    private fun check() {
        if (TextUtils.isEmpty(ed_phone.text.toString().trim { it <= ' ' })) {
            //            phone.setShakeAnimation();
        } else if (!StringUtil.isPhone( SPutils.getString(mContext,"phone"))) {
            //            phone.setShakeAnimation();
            Toast.makeText(this, "手机号码格式不正确", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(ed_code.text.toString().trim { it <= ' ' })) {
            ed_code.setShakeAnimation()
        } else {
            checkCode()

        }
    }

    /**
     * 验证验证码
     */
    private fun checkCode() {
        //        phone	string	必填	手机号
        //        code	string	必填	短信验证码
        //        requestCheck	string	必填	验证请求
        val map = HashMap<String, String>()
        map.put("phone", SPutils.getString(mContext,"phone"))
        map.put("code", ed_code.text.toString().trim { it <= ' ' })
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(SPutils.getString(mContext,"phone")+ed_code.text.toString().trim { it <= ' ' }, salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.PayPwdCodeVerify).tag(this)
                .params(map)
                .execute(object : DialogCallback<NetEntity<String>>(this) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().data=="1") {
                        val intent = Intent(mContext, PwdSetActivity::class.java)
                        intent.putExtra(PwdSetActivity.PwdSetActivity_Type, PwdSetActivity.PwdSetActivity_Type_2)
                        intent.putExtra(PwdSetActivity.PwdSetActivity_Security_Code, ed_code.text.toString().trim { it <= ' ' })
                        startActivityForResult(intent, 100)
                    }else {
                            toast(response.body().message)
                        }
                    }
                })

    }

    private fun getSecurityCode() {
        if (TextUtils.isEmpty(ed_phone.text.toString().trim { it <= ' ' }) || !StringUtil.isPhone( SPutils.getString(mContext,"phone"))) {
            //            phone.setShakeAnimation();
        } else {
            downTimer(time.toLong())
            PreferencesService.setDownTimer(this, downKey, System.currentTimeMillis())
            val map = HashMap<String, String>()
            val mobile = SPutils.getString(mContext,"phone")
            map.put("phone", if (TextUtils.isEmpty(mobile)) "" else mobile)
//            map.put("purpose", if (PwdSetActivity.IsSetPwd)"3" else "2")
            map.put("purpose", "3")
            map.put("requestCheck", EncryptUtils.encryptMD5ToString(mobile, salt).toLowerCase())
            OkGo.post<NetEntity<String>>(Urls.VerifyCode).tag(this)
                    .params(map)
                    .execute(object : DialogCallback<NetEntity<String>>(this) {

                        override fun onSuccess(response: Response<NetEntity<String>>) {
                            Toast.makeText(mContext, response.body().message, Toast.LENGTH_SHORT).show()
                            if (response.body().code == Constants.NetCode.SUCCESS) {
                                ed_code.setText(response.body().data)
                            }
                        }

                        override fun onError(response: Response<NetEntity<String>>) {
                            super.onError(response)
                            resetTimer(true, java.lang.Long.MIN_VALUE)
                        }
                    })

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            finish()
        }
    }
}