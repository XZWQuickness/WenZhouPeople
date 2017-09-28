package com.exz.wenzhoupeople.entity;

import android.os.Parcelable

/**
 * Created by 史忠文
 * on 2017/8/9.
 */

abstract class KeyAndValueBean:Parcelable{
    open var key: String=""
        get() = absKey()
    open var value=""
        get() = absValue()
    var isCheck=false

   abstract fun absKey():String
   abstract fun absValue():String
}
