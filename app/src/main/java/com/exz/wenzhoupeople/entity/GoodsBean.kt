package com.exz.wenzhoupeople.entity

import android.os.Parcel
import android.os.Parcelable

open class GoodsBean() : Parcelable {
    /**
     * id : 商品id
     * imgUrl : 商品图片
     * title : 商品名称
     * count : 商品数量
     * price : 17.8
     * oldPrice : 30.4
     * skuid : 1  //评价用
     * goodsType : 规格内容
     */
    var id: String = ""
    var imgUrl: String = ""
    var title: String = ""
    var count: String = ""
    var price: String = ""
    var oldPrice: String = ""
    var skuid: String = ""
    var goodsType: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        imgUrl = parcel.readString()
        title = parcel.readString()
        count = parcel.readString()
        price = parcel.readString()
        oldPrice = parcel.readString()
        skuid = parcel.readString()
        goodsType = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(imgUrl)
        parcel.writeString(title)
        parcel.writeString(count)
        parcel.writeString(price)
        parcel.writeString(oldPrice)
        parcel.writeString(skuid)
        parcel.writeString(goodsType)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<GoodsBean> {
        override fun createFromParcel(parcel: Parcel): GoodsBean = GoodsBean(parcel)

        override fun newArray(size: Int): Array<GoodsBean?> = arrayOfNulls(size)
    }
}