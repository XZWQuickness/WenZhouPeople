package com.exz.wenzhoupeople.activity

import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import cn.com.szw.lib.myframework.base.BaseActivity
import com.exz.wenzhoupeople.R
import com.exz.wenzhoupeople.fragment.OrderFragment
import kotlinx.android.synthetic.main.action_bar_tv.*
import kotlinx.android.synthetic.main.activity_order.*
import java.util.*

/**
 * Created by pc on 2017/9/1.
 * 我的订单
 */

class OrderActivity : BaseActivity() {
    internal var list = ArrayList<Fragment>()

    override fun initToolbar(): Boolean {
        toolbar.setContentInsetsAbsolute(0, 0)
        mTitle.textSize = 18f
        mTitle.text = "我的订单"
        mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray))
        mLeftImg.setOnClickListener{finish()}
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_order
    override fun init() {
        list.add(OrderFragment.newInstance("0"))
        list.add(OrderFragment.newInstance("1"))
        list.add(OrderFragment.newInstance("2"))
        list.add(OrderFragment.newInstance("3"))
        list.add(OrderFragment.newInstance("4"))
        val tabTitles = arrayOf("全部", "待付款", "待发货", "待收货", "待评价")
        mainTabBar.setViewPager(pager, tabTitles, this, list)
        mainTabBar.currentTab = Integer.parseInt(intent.getStringExtra(Intent_Order_Type))
    }

    companion object {
        val Intent_Order_Type = "Intent_Order_Type"
        var orderId = ""
    }
}
