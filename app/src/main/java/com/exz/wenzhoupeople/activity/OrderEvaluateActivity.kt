package com.exz.wenzhoupeople.activity

import android.app.Activity
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import android.view.View
import cn.com.szw.lib.myframework.app.MyApplication
import cn.com.szw.lib.myframework.base.BaseActivity
import cn.com.szw.lib.myframework.imageloder.GlideImageLoader
import cn.com.szw.lib.myframework.view.CustomProgress
import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.FileIOUtils
import com.exz.wenzhoupeople.R
import com.exz.wenzhoupeople.adapter.OrderEvaluateAdapter
import com.exz.wenzhoupeople.connection.OrderCtrl
import com.exz.wenzhoupeople.entity.EvaluateBean
import com.exz.wenzhoupeople.entity.JudgeBean
import com.exz.wenzhoupeople.entity.OrderBean
import com.exz.wenzhoupeople.entity.PhotoEntity
import com.google.gson.Gson
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.lzy.imagepicker.view.CropImageView
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.action_bar_tv.*
import kotlinx.android.synthetic.main.activity_order_evluate.*
import kotlinx.android.synthetic.main.footer_evaluate.view.*
import top.zibin.luban.Luban
import java.util.*

/**
 * Created by pc on 2017/9/2.
 * 订单评价
 */

class OrderEvaluateActivity : BaseActivity(), View.OnClickListener {


    lateinit var adapter: OrderEvaluateAdapter<JudgeBean>
    var data = EvaluateBean()
    private lateinit var footerView: View

    override fun initToolbar(): Boolean {
        toolbar.setContentInsetsAbsolute(0, 0)
        mTitle.textSize = 18f
        mTitle.text = "发布评价"
        mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray))
        mLeftImg.setOnClickListener(this)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_order_evluate

    override fun init() {
        evaluate.setOnClickListener(this)
        val orderBean = intent.getParcelableExtra<OrderBean>("data")
        data= EvaluateBean()
        data.id=MyApplication.getLoginUserId()
        data.orderId=orderBean.id
        data.requestCheck= EncryptUtils.encryptMD5ToString(MyApplication.getLoginUserId()+orderBean.id, MyApplication.salt).toLowerCase()
        for (good in orderBean.goods) {
            data.judge.add( JudgeBean(good.id,good.skuid,good.title,good.imgUrl)) }
        data.judge.forEach { it.imgs.add(PhotoEntity(R.mipmap.icon_photo_pai, "1")) }

        adapter = OrderEvaluateAdapter()
        adapter.bindToRecyclerView(mRecyclerView)
        footerView = View.inflate(mContext, R.layout.footer_evaluate, null)
        adapter.addFooterView(footerView)
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        adapter.setNewData(data.judge)
        footerView.serveStar.setOnRatingChangeListener{data.serveStar=it.toString()}
        footerView.logisticsStar.setOnRatingChangeListener{data.logisticsStar=it.toString()}
        initImagePicker()
    }


    /**
     * 初始化相机
     */
    private fun initImagePicker() {
        val imagePicker = ImagePicker.getInstance()
        imagePicker.imageLoader = GlideImageLoader()
        //显示相机
        imagePicker.isShowCamera = true
        //是否裁剪
        imagePicker.isCrop = false
        //是否按矩形区域保存裁剪图片
        imagePicker.isSaveRectangle = true
        //圖片緩存
        imagePicker.imageLoader = GlideImageLoader()
        imagePicker.isMultiMode = true//多选

        //矩形尺寸
        imagePicker.style = CropImageView.Style.RECTANGLE
        val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300f, resources.displayMetrics).toInt()
        imagePicker.focusWidth = width
        imagePicker.focusHeight = width
        //圖片輸出尺寸
        imagePicker.outPutX = (resources.displayMetrics.widthPixels * 0.5).toInt()
        imagePicker.outPutY = (resources.displayMetrics.widthPixels * 0.5).toInt()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) { //图片选择
            val images = data?.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<*>
            for (img in images) {
                adapter.data[requestCode].imgs.add(adapter.data[requestCode].imgs.size-1,PhotoEntity((img as ImageItem).path, "2"))
            }
            adapter.notifyItemChanged(requestCode)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.mLeftImg -> finish()
            R.id.evaluate -> {
                CustomProgress.show(this, "加载中", false, null)
                Flowable.just(data.judge).map {
                    for (judgeBean in it) {
                        judgeBean.imgs.filter { it.type!="1" }.forEach {
//                            judgeBean.images+=EncodeUtils.base64Encode2String(FileIOUtils.readFile2BytesByStream(Luban.with(this).load(FileUtils.getFileByPath(it.photoImg as String)).putGear(50).get()[0]))+","
                            judgeBean.images+=EncodeUtils.base64Encode2String(FileIOUtils.readFile2BytesByStream(Luban.with(this).load(it.photoImg as String).get(it.photoImg as String)))+","
                        }
                        if (judgeBean.images.isNotEmpty())
                            judgeBean.images=judgeBean.images.substring(0,judgeBean.images.length-1)
                    }
                    Gson().toJson(data)
                }.subscribeOn(Schedulers.io())//自下而上 第一个有用，下面的覆盖
                        .observeOn(AndroidSchedulers.mainThread())//自上而下，可多次切换，切换后subscribeOn不可改变，改变无效。
                        .subscribe {
                            OrderCtrl.orderJudge(mContext,it){
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                        }


            }
        }
    }

}
