package com.exz.wenzhoupeople.activity

import android.content.Intent
import android.view.View
import cn.com.szw.lib.myframework.app.MyApplication
import cn.com.szw.lib.myframework.base.BaseActivity
import cn.com.szw.lib.myframework.utils.Utils
import cn.com.szw.lib.myframework.utils.net.NetEntity
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback
import com.blankj.utilcode.util.EncryptUtils
import com.exz.wenzhoupeople.R
import com.exz.wenzhoupeople.activity.pay.PwdGetCodeActivity
import com.exz.wenzhoupeople.activity.pay.PwdSetActivity
import com.exz.wenzhoupeople.config.Urls
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import kotlinx.android.synthetic.main.action_bar_tv.*
import kotlinx.android.synthetic.main.activity_account_safe.*
import java.util.*

/**
 * Created by weicao on 2017/8/28.
 * 账户与安全
 */

class AccountSafeActivity : BaseActivity(), View.OnClickListener {


    override fun initToolbar(): Boolean {
        mTitle.text = "账户与安全"
        mTitle.textSize = 18f
        toolbar.setContentInsetsAbsolute(0, 0)
        setSupportActionBar(toolbar)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_account_safe

    override fun init() {
        mLeftImg.setOnClickListener(this)
        login_pwd.setOnClickListener(this)
        pay_set_pwd.setOnClickListener(this)
        pay_change_pwd.setOnClickListener(this)
        checkHavePayPwd()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.mLeftImg -> finish()
            R.id.login_pwd -> Utils.startActivity(mContext, ReSetPwdActivity::class.java)
            R.id.pay_set_pwd -> startActivity(Intent(mContext, PwdGetCodeActivity::class.java))
            R.id.pay_change_pwd -> {
                val intent = Intent(this, PwdSetActivity::class.java)
                intent.putExtra(PwdSetActivity.PwdSetActivity_Type, PwdSetActivity.PwdSetActivity_Type_1)
                startActivity(intent)
            }
        }
    }

    /**
     * 是否有支付密码
     */
    private fun checkHavePayPwd() {
        //        buyCardRecordId	string	必填	会员购买卡种记录id
        //        requestCheck	string	必填	验证请求

        val map = HashMap<String, String>()
        map.put("id", MyApplication.getLoginUserId())
        val time = Calendar.getInstance(Locale.CHINA).timeInMillis.toString()
        map.put("timestamp", time)
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.getLoginUserId() + time, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.IsSetPayPwd).tag(this)
                .params(map)
                .execute(object : DialogCallback<NetEntity<String>>(this) {

                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        //                                "data":"能否支付，0未设置 1已设置
                        when {
                            "0" == response.body().data -> {
                                PwdSetActivity.IsSetPwd = true
                                setting_set_pwd.text = "设置支付密码"
                            }
                            "1" == response.body().data -> {
                                PwdSetActivity.IsSetPwd = false
                                setting_set_pwd.text = "忘记支付密码"
                            }
                            else -> {
                                setting_set_pwd.text = "暂无法忘记支付密码"
                                setting_set_pwd.isClickable=false
                            }
                        }
                    }
                })

    }

    override fun onResume() {
        super.onResume()
        checkHavePayPwd()
    }

}
