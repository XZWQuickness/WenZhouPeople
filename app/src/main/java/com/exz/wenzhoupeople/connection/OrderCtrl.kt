package com.exz.wenzhoupeople.connection

import android.content.Context
import cn.com.szw.lib.myframework.app.MyApplication
import cn.com.szw.lib.myframework.config.Constants
import cn.com.szw.lib.myframework.utils.net.NetEntity
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback
import com.blankj.utilcode.util.EncryptUtils
import com.exz.wenzhoupeople.config.Urls
import com.exz.wenzhoupeople.entity.OrderBean
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import org.jetbrains.anko.toast

/**
 * Created by 史忠文
 * on 2017/9/11.
 */
object OrderCtrl {
    /**
     * 编辑订单
     * @param [context] 上下文
     * @param [orderId]  订单id
     * @param [editType]  订单id 编辑类型（0取消  1删除  2确认收货）
     * @param [listener] 成功监听
     * */
    fun editOrder(context:Context,orderId:String,editType:String,listener:()->Unit) {
        /**
         * id	string	必填	用户id
         * orderId	string	必填	订单id
         * editType	string	必填	编辑类型（0取消  1删除  2确认收货）
         * requestCheck	string	必填	验证请求
         *
         * requestCheck	“用户id+订单id+秘钥”的 MD5加密
         *
         * App/Order/Edit.aspx
         */
        val params = HashMap<String, String>()
        params.put("id", MyApplication.getLoginUserId())
        params.put("orderId", orderId)
        params.put("editType", editType)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.getLoginUserId()+orderId, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<Void>>(Urls.OrderEdit)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<Void>>(context) {
                    override fun onSuccess(response: Response<NetEntity<Void>>) {
                        context.toast(response.body().message)
                        if (response.body().code == Constants.NetCode.SUCCESS) {
                            listener.invoke()
                        }
                    }

                })
    }

    /**
     * 获取订单部分信息：订单详情（信息补充）
     * @param [context] 上下文
     * @param [orderId]  订单id
     * @param [listener] 成功监听
     * */
    fun detailAdditional(context:Context,orderId:String,listener:(entity: OrderBean)->Unit) {

        /**
         * id	string	必填	用户id
         * orderId	string	必填	订单id
         * requestCheck	string	必填	验证请求
         *
         * requestCheck	“用户id+订单id+秘钥”的 MD5加密
         *
         * App/Order/DetailAdditional.aspx
         */
        val params = HashMap<String, String>()
        params.put("id", MyApplication.getLoginUserId())
        params.put("orderId", orderId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.getLoginUserId()+orderId, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<OrderBean>>(Urls.DetailAdditional)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<OrderBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<OrderBean>>) {
                        if (response.body().code == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        }
                    }

                })
    }
    /**
     * 获取订单信息：订单详情
     * @param [context] 上下文
     * @param [orderId]  订单id
     * @param [listener] 成功监听
     * */
    fun detailAll(context:Context,orderId:String,listener:(entity: OrderBean)->Unit) {
        /**
         * id	string	必填	用户id
         * orderId	string	必填	订单id
         * requestCheck	string	必填	验证请求
         *
         * requestCheck	“用户id+订单id+秘钥”的 MD5加密
         *
         * http://xxx/App/Order/DetailAll.aspx
         */
        val params = HashMap<String, String>()
        params.put("id", MyApplication.getLoginUserId())
        params.put("orderId", orderId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.getLoginUserId()+orderId, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<OrderBean>>(Urls.DetailAll)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<OrderBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<OrderBean>>) {
                        if (response.body().code == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        }
                    }

                })
    }

    /**
     * 评价内容
     * @param [context] 上下文
     * @param [jsonStr]  订单json
     * @param [listener] 成功监听
     * */
    fun orderJudge(context:Context,jsonStr:String,listener:()->Unit) {
        /**
         *  {
         *  "id":""
         *  "orderId":""
         *  "serveStar":""
         *  "logisticsStar":""
         *  "judge":[{
         *  "goodsId":""
         *  "skuid":""
         *  "images":"用英文逗号隔开的多张图片（base64）"
         *  "content:"评价内容" //UTF8编码后提交
         *  "star:"评价星级"
         *  },…]
         *  "requestCheck":""
         *  }
         *
         *
         *  http://xxx/App/Order/Judge.aspx
         */
        val params = HashMap<String, String>()
        params.put("info", jsonStr)
        OkGo.post<NetEntity<OrderBean>>(Urls.OrderJudge)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<OrderBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<OrderBean>>) {
                        context.toast(response.body().message)
                        if (response.body().code == Constants.NetCode.SUCCESS) {
                            listener.invoke()
                        }
                    }

                })
    }
}