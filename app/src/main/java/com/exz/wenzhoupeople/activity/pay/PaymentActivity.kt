package com.exz.wenzhoupeople.activity.pay

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import cn.com.exz.jkzc.bean.CheckPayBean
import cn.com.exz.jkzc.popWin.PwdPop
import cn.com.szw.lib.myframework.app.MyApplication
import cn.com.szw.lib.myframework.config.Constants
import cn.com.szw.lib.myframework.utils.RxBus
import cn.com.szw.lib.myframework.utils.net.NetEntity
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback
import cn.com.szw.lib.myframework.view.pwd.widget.OnPasswordInputFinish
import com.blankj.utilcode.util.EncryptUtils
import com.exz.wenzhoupeople.R
import com.exz.wenzhoupeople.activity.OrderActivity
import com.exz.wenzhoupeople.config.Urls
import com.exz.wenzhoupeople.connection.OrderCtrl
import com.exz.wenzhoupeople.utils.DialogUtils
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import kotlinx.android.synthetic.main.action_bar_tv.*
import kotlinx.android.synthetic.main.activity_payment.*
import org.jetbrains.anko.toast
import java.util.*

/**
 * Created by pc on 2017/8/30.
 * 结算页面
 */

class PaymentActivity : PayActivity(), View.OnClickListener {


    lateinit var pwdPop: PwdPop
    var canBalancePay = false
    var realPrice = 0.00

    companion object {
        var payPrice = ""
        var payName = ""
        var payAddress = ""
    }

    override fun initToolbar(): Boolean {
        toolbar.setContentInsetsAbsolute(0, 0)
        mTitle.text = "结算"
        mTitle.textSize = 16f
        mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray))
        mLeftImg.setOnClickListener { DialogUtils.payBack(this) }
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_payment

    override fun init() {
        pwdPop = PwdPop(this) {
            OnPasswordInputFinish {
                balancePay(it)
                pwdPop.dismiss()
            }
        }
        OrderActivity.orderId = intent.getStringExtra("orderId")
        OrderCtrl.detailAdditional(mContext, intent.getStringExtra("orderId")) {
            payName = it.address.name
            payAddress = it.address.area + it.address.detail
            totalMoney.text = "￥ ${it.totalMoney}"
            couponMoney.text = "￥ ${it.couponMoney}"
            postMoney.text = "￥ ${it.postMoney}"
            realMoney.text = "￥ ${it.realMoney}"
            realPrice = if (it.realMoney.isNotEmpty()) it.realMoney.toDouble() else 1000000.00
            pwdPop.setPrice(String.format("￥%s", it.realMoney))
            myBalance()
        }


        rb.check(rb.getChildAt(0).id)
        pay.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        if (rb.checkedRadioButtonId == rb.getChildAt(0).id)
            AliPay(Urls.OrderAliPay, "orderId", intent.getStringExtra("orderId"))
        else if (rb.checkedRadioButtonId == rb.getChildAt(2).id)
            WeChatPay(Urls.OrderWeChatPay, "orderId", intent.getStringExtra("orderId"))
        else if (rb.checkedRadioButtonId == rb.getChildAt(4).id) {
            if (canBalancePay)
                checkHavePayPwd()
            else
                Toast.makeText(mContext, "余额不足", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 我的余额
     */
    private fun myBalance() {
        /**
         * id	string	必填	用户id
        timestamp	string	必填	时间戳
        requestCheck	string	必填	验证请求

         */
        val map = HashMap<String, String>()
        map.put("id", MyApplication.getLoginUserId())
        val time = Calendar.getInstance().timeInMillis.toString()
        map.put("timestamp", time)
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.getLoginUserId() + time, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.Balance).tag(this)
                .params(map)
                .execute(object : DialogCallback<NetEntity<String>>(this) {

                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        (rb.getChildAt(4) as RadioButton).text = String.format("%s(￥%s)", "余额支付", response.body().data)//我的余额
                        canBalancePay = (if (TextUtils.isEmpty(response.body().data)) "0" else response.body().data).toDouble() >= realPrice

                    }

                    override fun onError(response: Response<NetEntity<String>>?) {
                        super.onError(response)
                        (rb.getChildAt(4) as RadioButton).text = String.format("%s(%s)", "余额支付", "未查询到余额")//我的余额
                        canBalancePay = false
                    }
                })

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
                        if (response.body().code == Constants.NetCode.SUCCESS) {
                            if ("0" == response.body().data) {
                                DialogUtils.PayNoPwd(this@PaymentActivity, { startActivity(Intent(mContext, PwdGetCodeActivity::class.java)) })
                            } else {
                                pwdPop.showPopupWindow()
                            }
                        } else {
                            toast(response.body().message)
                        }
                    }
                })

    }

    /**
     * 余额支付
     */
    private fun balancePay(payPwd: String) {
        // id	string	必填	用户id
//        orderId	string	必填	订单id
//         payPwd	string	必填	支付密码
//         requestCheck	string	必填	验证请求


        val map = HashMap<String, String>()
        map.put("id", MyApplication.getLoginUserId())
        map.put("payPwd", payPwd)
        map.put("orderId", intent.getStringExtra("orderId"))
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.getLoginUserId() + intent.getStringExtra("orderId") + payPwd, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<CheckPayBean>>(Urls.OrderBalancePay).tag(this)
                .params(map)
                .execute(object : DialogCallback<NetEntity<CheckPayBean>>(this) {

                    override fun onSuccess(response: Response<NetEntity<CheckPayBean>>) {
                        if (response.body().code == Constants.NetCode.SUCCESS) {
                            if (response.body().data != null) {
                                payPrice = response.body().data.payMoney
                            }
                            val intent = Intent(mContext, PaySuccessActivity::class.java)
                            startActivity(intent)
                            RxBus.get().post(Constants.BusAction.Pay_Finish, Constants.BusAction.Pay_Finish)
                            finish()
                        } else {
                            toast(response.body().message)
                        }
                    }
                })

    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = arrayOf(Tag(Constants.BusAction.Pay_Finish)))
    fun PayFinish(tag: String) {
        checkPay()
    }

    private fun checkPay() {
        //        userId		String		必填		用户Id
        //        orderId		String		必填		货源订单id
        //        requestCheck		string		必填		验证请求
        val map = HashMap<String, String>()
        map.put("id", MyApplication.getLoginUserId())
        map.put("orderId", intent.getStringExtra("orderId"))
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.getLoginUserId() + intent.getStringExtra("orderId"), MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<CheckPayBean>>(Urls.OrderPayState).tag(this)
                .params(map)
                .execute(object : DialogCallback<NetEntity<CheckPayBean>>(this) {

                    override fun onSuccess(response: Response<NetEntity<CheckPayBean>>) {
                        if (response.body().code == Constants.NetCode.SUCCESS) {
                            if (response.body().data.payState == "1") {
                                payPrice = response.body().data.payMoney
                                val intent = Intent(mContext, PaySuccessActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                cn.com.szw.lib.myframework.utils.DialogUtils.Warning(mContext, "服务器验证支付失败，请联系平台")
                            }
                        } else {
                            toast(response.body().message)
                        }
                    }
                })

    }

    override fun onBackPressed() {
        DialogUtils.payBack(this)
    }
}
