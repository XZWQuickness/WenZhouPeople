package com.exz.wenzhoupeople.adapter

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.xlhratingbar_lib.XLHRatingBar
import com.exz.wenzhoupeople.R
import com.exz.wenzhoupeople.activity.OrderEvaluateActivity
import com.exz.wenzhoupeople.entity.JudgeBean
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.ui.ImageGridActivity
import kotlinx.android.synthetic.main.item_order_evluate.view.*
import org.jetbrains.anko.toast


class OrderEvaluateAdapter<T : JudgeBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_order_evluate, null) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        itemView.img.setImageURI(item.image)
        itemView.goodsName.text=item.goodsName
        itemView.content.setText(item.content)
        itemView.content.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable) {
                if (p0.toString().length>200){
                    mContext.toast("字数超出限制")
                    itemView.content.setText(p0.toString().substring(0,200))
                    itemView.content.setSelection(200)
                }
                data[helper.adapterPosition].content=p0.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        itemView.ratingBar.onRatingChangeListener= XLHRatingBar.OnRatingChangeListener{ data[helper.adapterPosition].star= it.toString() }
        val adapter=PhotoAdapter()
        adapter.bindToRecyclerView(itemView.mRecyclerView)
        itemView.mRecyclerView.layoutManager = GridLayoutManager(mContext, 3)
        adapter.setOnItemClickListener { _, _, position ->
            val entity = item.imgs[position]
            when (entity.type) {
                "1"//拍照
                -> if (adapter.data.size < 6) {
                    val intent = Intent(mContext, ImageGridActivity::class.java)
                    val imagePicker = ImagePicker.getInstance()
                    imagePicker.selectLimit=6-adapter.data.size
                    (mContext as OrderEvaluateActivity).startActivityForResult(intent, helper.adapterPosition)
                }
                "2"//本地图片
                    , "3"//网络图片
                -> {
                    item.imgs.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
            } }
        adapter.setNewData(item.imgs)
    }
}