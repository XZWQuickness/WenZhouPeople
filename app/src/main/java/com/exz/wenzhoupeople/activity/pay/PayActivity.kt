package com.exz.wenzhoupeople.activity.pay

import android.text.TextUtils
import android.widget.Toast
import cn.com.szw.lib.myframework.app.MyApplication
import cn.com.szw.lib.myframework.app.MyApplication.salt
import cn.com.szw.lib.myframework.base.BaseActivity
import cn.com.szw.lib.myframework.config.Constants.BusAction.Pay_Finish
import cn.com.szw.lib.myframework.utils.DialogUtils
import cn.com.szw.lib.myframework.utils.PayResult
import cn.com.szw.lib.myframework.utils.RxBus
import cn.com.szw.lib.myframework.utils.net.NetEntity
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback
import com.alipay.sdk.app.PayTask
import com.blankj.utilcode.util.EncryptUtils
import com.exz.wenzhoupeople.config.Constants
import com.exz.wenzhoupeople.config.Urls
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.toast
import java.util.*


/**
 * Created by Swain
 * on 2016/12/8.
 */

abstract class PayActivity : BaseActivity() {
    private val msgApi = WXAPIFactory.createWXAPI(mContext, null)
    //    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Pay_Finish)})
    //    public void PayFinish(String tag) {
    //        finish();
    //    }
    protected var rechargeOrderId = ""
    private val isWXAppInstalledAndSupported: Boolean
        get() {
            val msgApi = WXAPIFactory.createWXAPI(this, null)
            msgApi.registerApp(Urls.APP_ID)
            return msgApi.isWXAppInstalled && msgApi.isWXAppSupportAPI
        }

    // 微信支付
    protected fun WeChatPay(url: String, key: String, value: String) {
        val map = HashMap<String, String>()
        map.put("id", MyApplication.getLoginUserId())
        map.put(key, value)
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.getLoginUserId(), salt).toLowerCase())
        OkGo.post<NetEntity<WxBean>>(url).tag(this)
                .params(map)
                .execute(object : DialogCallback<NetEntity<WxBean>>(this) {

                    override fun onSuccess(response: Response<NetEntity<WxBean>>) {
                        if (response.body().code == Constants.NetCode.SUCCESS) {
                            if (response.body().data != null) {

                                rechargeOrderId = response.body().data.rechargeOrderId
                                val req = PayReq()
                                req.appId = response.body().data.appId
                                Urls.APP_ID = response.body().data.appId
                                req.partnerId = response.body().data.partnerId
                                req.prepayId = response.body().data.prepayId
                                req.packageValue = response.body().data.packageValue
                                req.nonceStr = response.body().data.nonceStr
                                req.timeStamp = response.body().data.timeStamp
                                req.sign = response.body().data.sign
                                if (!isWXAppInstalledAndSupported) {
                                    Toast.makeText(this@PayActivity, "没安装微信客户端", Toast.LENGTH_SHORT).show()
                                    return
                                }
                                msgApi.registerApp(response.body().data.appId)
                                msgApi.sendReq(req)
                            }
                        } else {
                            toast(response.body().message)
                        }
                    }
                })
    }

    // 支付宝支付
    protected fun AliPay(url: String, key: String, value: String) {
        //        userId	string	必填	会员id
        //        buyCardRecordId	string	必填	会员购买卡种记录id
        //        requestCheck	string	必填	验证请求
        val map = HashMap<String, String>()
        map.put("id", MyApplication.getLoginUserId())
        map.put(key, value)
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.getLoginUserId(), salt).toLowerCase())
        OkGo.post<NetEntity<AliBean>>(url).tag(this)
                .params(map)
                .execute(object : DialogCallback<NetEntity<AliBean>>(this) {
                    override fun onSuccess(response: Response<NetEntity<AliBean>>) {
                        if (response.body().code == Constants.NetCode.SUCCESS) {
                            if (response.body().data != null) {
                                rechargeOrderId = response.body().data.rechargeOrderId
                                pay(response.body().data.payDescription, response.body().data.sign)
                            }
                        } else {
                            toast(response.body().message)
                        }
                    }

                })

    }

    private fun pay(orderInfo: String, sign: String) {
        /*
          完整的符合支付宝参数规范的订单信息
         */
        val payInfo = "$orderInfo&sign=\"$sign\"&" + "sign_type=\"RSA\""//获取签名方式
        Flowable.just(payInfo)
                .map { s ->
                    // 构造PayTask 对象
                    val aliPay = PayTask(this@PayActivity)
                    // 调用支付接口，获取支付结果
                    aliPay.pay(s, true)
                }.subscribeOn(Schedulers.io())//自下而上 第一个有用，下面的覆盖
                .observeOn(AndroidSchedulers.mainThread())//自上而下，可多次切换，切换后subscribeOn不可改变，改变无效。
                .subscribe { s ->
                    val payResult = PayResult(s)
                    /*
                          同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                          detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                          docType=1) 建议商户依赖异步通知
                         */
                    //                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    val mess: String
                    val resultStatus = payResult.resultStatus
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    mess = if (TextUtils.equals(resultStatus, "9000")) {
                        "支付成功"
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        when {
                            TextUtils.equals(resultStatus, "8000") -> "支付结果确认中"
                            TextUtils.equals(resultStatus, "6001") -> // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                "支付被取消"
                            TextUtils.equals(resultStatus, "6002") -> "网络连接出错"
                            else -> "支付失败"
                        }
                    }
                    DialogUtils.Warning(this@PayActivity, mess)
                    if (DialogUtils.dialog != null) {
                        DialogUtils.dialog.setOnDismissListener {
                            if (TextUtils.equals(resultStatus, "9000")) {
                                RxBus.get().post(Pay_Finish, Pay_Finish)
                            } else
                                DialogUtils.dialog.dismiss()
                        }
                        DialogUtils.dialog.setOkBtn("确定") { DialogUtils.dialog.dismiss() }
                    }
                }
        // 必须异步调用
    }

    class WxBean {
        /**
         * appId : wx481868c18b24a8a8
         * partnerId : 1486509272
         * prepayId : wx20170831104548852a6428e00258011767
         * nonceStr : 32e194eb19ce404cb0c4a47691489a7b
         * timeStamp : 1504147670
         * packageValue : Sign=WXPay
         * sign : 2C9682A48F4B1DFD0660638E7A59A518
         * orderId : 18311007103108
         */

        var appId: String = ""
        var partnerId: String = ""
        var prepayId: String = ""
        var nonceStr: String = ""
        var timeStamp: String = ""
        var packageValue: String = ""
        var sign: String = ""
        var rechargeOrderId: String = ""
    }

    class AliBean {

        /**
         * payDescription : 商品相关描述
         * sing : 商品相关描述
         * orderId : 订单号
         */

        var payDescription: String = ""
        var sign: String = ""
        var rechargeOrderId: String = ""
    }
}