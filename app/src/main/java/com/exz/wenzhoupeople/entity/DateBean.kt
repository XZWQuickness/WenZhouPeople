package com.exz.wenzhoupeople.entity

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by 史忠文
 * on 2017/9/11.
 */
class DateBean() :Parcelable{

    /**
     * key : 创建时间
     * value : 2017-09-07 10:00
     */

     var key: String=""
     var value: String=""
    constructor(parcel: Parcel) : this() {
        key = parcel.readString()
        value = parcel.readString()
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(key)
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DateBean> {
        override fun createFromParcel(parcel: Parcel): DateBean {
            return DateBean(parcel)
        }

        override fun newArray(size: Int): Array<DateBean?> {
            return arrayOfNulls(size)
        }
    }

}