package com.exz.wenzhoupeople.activity

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import cn.com.szw.lib.myframework.base.BaseActivity
import cn.com.szw.lib.myframework.utils.RecycleViewDivider
import com.chad.library.adapter.base.BaseQuickAdapter
import com.exz.wenzhoupeople.R
import com.exz.wenzhoupeople.adapter.ItemOrderAdapter
import com.exz.wenzhoupeople.adapter.OrderAdapter
import com.exz.wenzhoupeople.connection.OrderCtrl
import com.exz.wenzhoupeople.entity.GoodsBean
import com.exz.wenzhoupeople.entity.OrderBean
import com.exz.wenzhoupeople.fragment.OrderFragment
import kotlinx.android.synthetic.main.action_bar_tv.*
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.footer_order_detail_lay.view.*
import kotlinx.android.synthetic.main.header_order_detail_lay.view.*
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

/**
 * Created by pc on 2017/9/4.
 * 订单详情
 */

class OrderDetailActivity : BaseActivity(), BaseQuickAdapter.OnItemClickListener, View.OnClickListener {
    private lateinit var orderBean:OrderBean
    private lateinit var mAdapter:ItemOrderAdapter<GoodsBean>
    private lateinit var headerView :View
    private lateinit var footerView :View
    override fun initToolbar(): Boolean {
        toolbar.setContentInsetsAbsolute(0, 0)
        mTitle.text = "订单详情"
        mTitle.textSize = 18f
        mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray))
        mLeftImg.setOnClickListener(this)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_order_detail

    override fun init() {
        headerView = View.inflate(mContext,R.layout.header_order_detail_lay, null)
        footerView = View.inflate(mContext,R.layout.footer_order_detail_lay, null)
        mAdapter = ItemOrderAdapter()
        mAdapter.addHeaderView(headerView)
        mAdapter.addFooterView(footerView)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.emptyView = LayoutInflater.from(mContext).inflate(R.layout.view_empty, RelativeLayout(mContext), false)
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(mContext, R.color.line_bg)))
        mAdapter.onItemClickListener = this
        btLeft.setOnClickListener(this)
        btRight.setOnClickListener(this)
        setData()
    }




    private fun setData() {
        OrderCtrl.detailAll(mContext, OrderActivity.orderId){
            orderBean=it
            OrderAdapter.initStateBtn(it.state,headerView.state,btLeft,btRight)
            btLay.visibility=if (it.state=="7")View.GONE else View.VISIBLE
            headerView.name.text=it.address.name
            headerView.phone.text=it.address.phone
            headerView.address.text= "${it.address.area}${it.address.detail}"
            mAdapter.setNewData(it.goods)
            footerView.msg.text="买家留言:${it.msg}"
            footerView.totalMoney.text="￥${it.totalMoney}"
            footerView.couponMoney.text="￥${it.couponMoney}"
            footerView.postMoney.text="￥${it.postMoney}"
            footerView.realMoney.text="￥${it.realMoney}"
            footerView.llTime.removeAllViews()
            footerView.llTime.addView(with(footerView.context){
                verticalLayout{
                    textView ( "订单编号:${it.num}"){
                        textSize = 14f
                        setTextColor(ContextCompat.getColor(mContext, R.color.text_gray))
                    }
                    for (dateBean in it.date) {
                        textView ( "${dateBean.key}:${dateBean.value}"){
                            textSize = 14f
                            setTextColor(ContextCompat.getColor(mContext, R.color.text_gray))
                        }
                    }
                }
            })
        }
    }
    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {

    }
    override fun onClick(p0: View) {
        when (p0) {
            mLeftImg -> {
                finish()
            }
            else -> {
                OrderFragment.editOrder(this,p0,orderBean){
                    setData()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==100) {
            setData()
        }
    }
}
