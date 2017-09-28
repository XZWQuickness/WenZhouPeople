package com.exz.wenzhoupeople.entity

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by 史忠文
 * on 2017/9/11.
 */

open class OrderBean() : Parcelable {

    /**
     * id : 1
     * num : 订单编号
     * state : 订单状态
     * totalCount : 商品总数量
     * logistics : 物流单号
     * totalMoney : 总额
     * couponMoney : 优惠金额
     * postMoney : 运费
     * realMoney : 实付款
     * msg : 买家留言
     * goods : []
     * date : [{"key":"创建时间","value":"2017-09-07 10:00"}]
     * address : {"name":"姓名","phone":"电话号码","area":"所在地区（省市区）","detail":"详细地址"}
     */
    var id: String=""
    var num: String=""
    var state: String=""
    var totalCount: String=""
    var logistics: String=""
    var totalMoney: String=""
    var couponMoney: String=""
    var postMoney: String=""
    var realMoney: String=""
    var msg: String=""
    var address= AddressBean()
    var goods =ArrayList<GoodsBean>()
    var date =ArrayList<DateBean>()

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        num = parcel.readString()
        state = parcel.readString()
        totalCount = parcel.readString()
        logistics = parcel.readString()
        totalMoney = parcel.readString()
        couponMoney = parcel.readString()
        postMoney = parcel.readString()
        realMoney = parcel.readString()
        msg = parcel.readString()
        goods = parcel.createTypedArrayList(GoodsBean.CREATOR)
        date = parcel.createTypedArrayList(DateBean.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(num)
        parcel.writeString(state)
        parcel.writeString(totalCount)
        parcel.writeString(logistics)
        parcel.writeString(totalMoney)
        parcel.writeString(couponMoney)
        parcel.writeString(postMoney)
        parcel.writeString(realMoney)
        parcel.writeString(msg)
        parcel.writeTypedList(goods)
        parcel.writeTypedList(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderBean> {
        override fun createFromParcel(parcel: Parcel): OrderBean {
            return OrderBean(parcel)
        }

        override fun newArray(size: Int): Array<OrderBean?> {
            return arrayOfNulls(size)
        }
    }


}
