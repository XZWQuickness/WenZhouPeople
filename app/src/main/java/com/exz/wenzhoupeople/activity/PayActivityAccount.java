package com.exz.wenzhoupeople.activity;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.EncryptUtils;
import com.exz.wenzhoupeople.activity.pay.PayActivity;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.config.Urls;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;

import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.config.Constants;
import cn.com.szw.lib.myframework.utils.DialogUtils;
import cn.com.szw.lib.myframework.utils.PayResult;
import cn.com.szw.lib.myframework.utils.RxBus;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static cn.com.szw.lib.myframework.config.Constants.BusAction.Pay_Finish;


/**
 * Created by Swain
 * on 2016/12/8.
 */

public abstract class PayActivityAccount extends BaseActivity {
    protected String rechargeOrderId = "";

    private IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);


    private boolean isWXAppInstalledAndSupported() {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
        msgApi.registerApp(Urls.APP_ID);
        return msgApi.isWXAppInstalled()
                && msgApi.isWXAppSupportAPI();
    }

    // 微信支付
    protected void WeChatPay(String url, String key, String value) {
//        userId	string	必填	会员id
//        buyCardRecordId	string	必填	会员购买卡种记录id
//        requestCheck	string	必填	验证请求
        HashMap<String, String> map = new HashMap<>();
        map.put("id", App.getLoginUserId());
        map.put(key, value);
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() +  App.salt).toLowerCase());
        OkGo.<NetEntity<PayActivity.WxBean>>post(url).params(map).tag(this).execute(new DialogCallback<NetEntity<PayActivity.WxBean>>(mContext) {
            @Override
            public void onSuccess(Response<NetEntity<PayActivity.WxBean>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    rechargeOrderId = response.body().getData().getRechargeOrderId();
                    PayReq req = new PayReq();
                    req.appId = response.body().getData().getAppId();
                    Urls.APP_ID = response.body().getData().getAppId();
                    req.partnerId = response.body().getData().getPartnerId();
                    req.prepayId = response.body().getData().getPrepayId();
                    req.packageValue = response.body().getData().getPackageValue();
                    req.nonceStr = response.body().getData().getNonceStr();
                    req.timeStamp = response.body().getData().getTimeStamp();
                    req.sign = response.body().getData().getSign();
                    if (!isWXAppInstalledAndSupported()) {
                        Toast.makeText(PayActivityAccount.this, "没安装微信客户端", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    msgApi.registerApp(response.body().getData().getAppId());
                    msgApi.sendReq(req);
                }
            }

        });

    }



    // 支付宝支付
    protected void AliPay(String url, String key, String value) {
//        userId	string	必填	会员id
//        buyCardRecordId	string	必填	会员购买卡种记录id
//        requestCheck	string	必填	验证请求
        HashMap<String, String> map = new HashMap<>();
        map.put("id", App.getLoginUserId());
        map.put(key, value);
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() +  App.salt).toLowerCase());
        OkGo.<NetEntity<PayActivity.AliBean>>post(url).params(map).tag(this).execute(new DialogCallback<NetEntity<PayActivity.AliBean>>(mContext) {
            @Override
            public void onSuccess(Response<NetEntity<PayActivity.AliBean>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {

                    if (response.body().getData() != null) {
                        pay(response.body().getData().getPayDescription(), response.body().getData().getSign());
                    }
                }
            }

        });

    }

    private void pay(String orderInfo, String sign) {
        /*
          完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + "sign_type=\"RSA\"";//获取签名方式
        Flowable.just(payInfo)
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        // 构造PayTask 对象
                        PayTask aliPay = new PayTask(PayActivityAccount.this);
                        // 调用支付接口，获取支付结果
                        return aliPay.pay(s, true);
                    }
                }).subscribeOn(Schedulers.io())//自下而上 第一个有用，下面的覆盖
                .observeOn(AndroidSchedulers.mainThread())//自上而下，可多次切换，切换后subscribeOn不可改变，改变无效。
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        PayResult payResult = new PayResult(s);
                        /*
                          同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                          detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                          docType=1) 建议商户依赖异步通知
                         */
//                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String mess;
                        final String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                        if (TextUtils.equals(resultStatus, "9000")) {
                            mess = "支付成功";
                        } else {
                            // 判断resultStatus 为非"9000"则代表可能支付失败
                            // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            if (TextUtils.equals(resultStatus, "8000")) {
                                mess = "支付结果确认中";
                            } else if (TextUtils.equals(resultStatus, "6001")) {
                                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                mess = "支付被取消";
                            } else if (TextUtils.equals(resultStatus, "6002")) {
                                mess = "网络连接出错";
                            } else {
                                mess = "支付失败";
                            }
                        }
                        DialogUtils.Warning(PayActivityAccount.this, mess);
                        if (DialogUtils.dialog != null) {
                            DialogUtils.dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    if (TextUtils.equals(resultStatus, "9000")) {
                                        // TODO: 2017/1/16 刷新
                                        RxBus.get().post(Pay_Finish, Pay_Finish);
                                    } else
                                        DialogUtils.dialog.dismiss();

                                }
                            });
                            DialogUtils.dialog.setOkBtn("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DialogUtils.dialog.dismiss();
                                }
                            });
                        }
                    }
                });
        // 必须异步调用
    }

}
