package com.exz.wenzhoupeople.wxapi;



import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.exz.wenzhoupeople.config.Urls;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.com.szw.lib.myframework.utils.DialogUtils;
import cn.com.szw.lib.myframework.utils.RxBus;

import static cn.com.szw.lib.myframework.config.Constants.BusAction.Pay_Finish;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;
	private String code="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, Urls.APP_ID);
		api.handleIntent(getIntent(), this);
		switch (code) {
			case "-1":
				DialogUtils.Warning(this, "支付失败");
				break;
			case "-2":
				DialogUtils.Warning(this, "支付被取消");
				break;
			case "0":
				DialogUtils.Warning(this, "支付成功");
				break;
		}
		Log.e("WXPay","支付有了回调");
		if (DialogUtils.dialog != null){
			DialogUtils.dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialogInterface) {
					if("0".equals(code)){
						// TODO: 2017/1/16 刷新
						RxBus.get().post(Pay_Finish, Pay_Finish);
					}
					finish();
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

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(String.format("onPayFinish, errCode = %s", resp.errCode), TAG);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle("提示");
			code= String.valueOf(resp.errCode);
//			Toast.makeText(this,  resp.errStr + ";code=" + String.valueOf(resp.errCode), Toast.LENGTH_SHORT).show();
//			builder.setMessage(getString(R.string.pay_result_callback_msg, resp.errStr +";code=" + String.valueOf(resp.errCode)));
//			builder.show();
		}
	}
}