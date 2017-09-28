package com.exz.wenzhoupeople.entity

/**
 * Created by 史忠文
 * on 2017/9/12.
 */

class EvaluateBean {
    /**
     * id :
     * orderId :
     * serveStar :
     * logisticsStar :
     * judge : [{"goodsId":"","skuid":"","images":"用英文逗号隔开的多张图片（base64）","content":"评价内容//UTF8编码后提交","star":"评价星级"}]
     * requestCheck :
     */

    var id=""
    var orderId=""
    var serveStar="5"
    var logisticsStar="5"
    var requestCheck=""
    var judge= ArrayList<JudgeBean>()


}
