package com.exz.wenzhoupeople.activity.pay

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.view.View
import cn.com.szw.lib.myframework.base.BaseActivity
import com.exz.wenzhoupeople.R
import com.exz.wenzhoupeople.activity.OrderDetailActivity
import com.exz.wenzhoupeople.utils.TextUtils
import kotlinx.android.synthetic.main.action_bar_tv.*
import kotlinx.android.synthetic.main.activity_pay_success.*
class PaySuccessActivity : BaseActivity(), View.OnClickListener {


    override fun initToolbar(): Boolean {
        toolbar.setContentInsetsAbsolute(0, 0)
        mTitle.textSize = 18f
        mTitle.text = "支付成功"
        mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray))
        setSupportActionBar(toolbar)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_pay_success


    override fun init() {
        name.text="收件人:${PaymentActivity.payName}"
        address.text=PaymentActivity.payAddress
        price.text="总价:￥${PaymentActivity.payPrice}"
        TextUtils.setTextChanesColor(mContext, price, ContextCompat.getColor(mContext, R.color.blue2), price.text.toString(), 4, price.text.length)
        mLeftImg.setOnClickListener(this)
        lookOrder.setOnClickListener(this)
        backHome.setOnClickListener(this)
    }
    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.mLeftImg -> finish()
            R.id.lookOrder ->{
                val intent = Intent(mContext, OrderDetailActivity::class.java)
                startActivityForResult(intent,100)
                finish()
            }
            R.id.backHome -> finish()
        }
    }

}
