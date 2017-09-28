package com.exz.wenzhoupeople.adapter

import android.graphics.Paint
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.wenzhoupeople.R
import com.exz.wenzhoupeople.entity.GoodsBean
import kotlinx.android.synthetic.main.item_order.view.*

class ItemOrderAdapter<T : GoodsBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_order, null) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        itemView.goodsName.text=item.title
        itemView.img.setImageURI(item.imgUrl)
        itemView.price.text="￥${item.price}"
        itemView.oldPrice.text="￥${item.oldPrice}"
        itemView.oldPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        itemView.spec.text=item.goodsType
        itemView.count.text="x${item.count}"
    }
}
