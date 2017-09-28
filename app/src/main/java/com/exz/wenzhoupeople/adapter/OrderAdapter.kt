package com.exz.wenzhoupeople.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import cn.com.szw.lib.myframework.utils.RecycleViewDivider
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.wenzhoupeople.R
import com.exz.wenzhoupeople.entity.GoodsBean
import com.exz.wenzhoupeople.entity.OrderBean
import kotlinx.android.synthetic.main.adapter_order.view.*


class OrderAdapter<T : OrderBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.adapter_order, ArrayList<T>()) {


    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        itemView.orderNum.text = "订单编号：${item.num}"

        itemView.count.text = "合计（共${item.totalCount}件）:"
        itemView.price.text = "￥${item.realMoney}"

        val adapter = ItemOrderAdapter<GoodsBean>()
        adapter.bindToRecyclerView(itemView.mRecyclerView)
        adapter.setNewData(item.goods)
        itemView.mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        itemView.mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(mContext, R.color.line_bg)))
        helper.addOnClickListener(R.id.btLeft)
        helper.addOnClickListener(R.id.btRight)

        initStateBtn(item.state,itemView.orderState,itemView.btLeft,itemView.btRight)

    }


    companion object {
        //    1待付款 2待发货 3待收货 4待评价 5已完成 6已取消 7已删除 （已删除的订单不需要返回给APP）
        private fun getState(state: String): String = when (state) {
            "1" -> "待付款"
            "2" -> "待发货"
            "3" -> "待收货"
            "4" -> "待评价"
            "5" -> "已完成"
            "6" -> "已取消"
            "7" -> "已删除"
            else -> "无此状态"

        }

        /**
         * [state] 订单状态id
         * [view] view[0] 订单状态view
         * [view] view[1] btLeft
         * [view] view[2 btRight
         */
        fun initStateBtn(state:String,vararg view:TextView){
            /**         btLeft    btRight
             * 1待付款  取消订单，去付款
             * 2待发货  联系客服
             * 3待收货  联系客服，确认收货
             * 4待评价  去评价
             * 5已完成  联系客服, 删除订单
             * 其他     删除订单
             */
            view[0].text = getState(state)
            view[1].visibility = View.VISIBLE
            view[2].visibility = View.VISIBLE
            val str_left:String
            val str_right:String
            when (state) {
                "1" -> {
                    str_left = "取消订单"
                    str_right = "去付款"
                }
                "2" -> {
                    view[1].visibility = View.GONE
                    str_left = ""
                    str_right = "联系客服"
                }
                "3" -> {
                    str_left = "联系客服"
                    str_right = "确认收货"
                }
                "4" -> {
                    view[1].visibility = View.GONE
                    str_left = ""
                    str_right = "去评价"
                }
                "5" -> {
                    str_left = "联系客服"
                    str_right = "删除订单"
                }

                else -> {
                    view[1].visibility = View.GONE
                    str_left = ""
                    str_right = "删除订单"
                }

            }
            view[1].text = str_left
            view[2].text = str_right
        }
    }

}
