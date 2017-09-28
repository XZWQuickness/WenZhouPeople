package com.exz.wenzhoupeople.entity

import java.net.URLDecoder
import java.net.URLEncoder

open class JudgeBean(var goodsId: String = "", var skuid: String = "", var goodsName: String = "", var image: String = "") {

    /**
     * goodsId :
     * goodsName :
     * skuid :
     * image :
     * images : 用英文逗号隔开的多张图片（base64）
     * content : 评价内容//UTF8编码后提交
     * star : 评价星级
     */
    var imgs = ArrayList<PhotoEntity>()
    var images = ""
    var content = ""
        set(value) {
            field = URLEncoder.encode(value, "utf-8")
        }
        get() = URLDecoder.decode(field, "utf-8")
    var star = "5"
}
