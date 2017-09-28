package com.exz.wenzhoupeople.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.blankj.utilcode.util.EncryptUtils;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.config.Urls;
import com.exz.wenzhoupeople.entity.AddressEntity;
import com.exz.wenzhoupeople.entity.TakeGoodsAddressEntity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.config.Constants;
import cn.com.szw.lib.myframework.utils.Utils;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;
import cn.com.szw.lib.myframework.view.ClearWriteEditText;

import static com.exz.wenzhoupeople.activity.TakeGoodsAddressActivity.RESULTCODE_REFRESH;

/**
 * Created by pc on 2017/8/30.
 */

public class GoodsAddressDetailActivity extends BaseActivity {

    TakeGoodsAddressEntity entity;
    @BindView(R.id.mLeft)
    TextView mLeft;
    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.mRight)
    TextView mRight;
    @BindView(R.id.mRightImg)
    ImageView mRightImg;
    @BindView(R.id.mLeftImg)
    ImageView mLeftImg;
    @BindView(R.id.parent_lay)
    RelativeLayout parentLay;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvName)
    ClearWriteEditText tvName;
    @BindView(R.id.tvPhone)
    ClearWriteEditText tvPhone;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvAddressDetail)
    ClearWriteEditText tvAddressDetail;
    @BindView(R.id.cb1)
    CheckBox cb1;
    @BindView(R.id.cb2)
    CheckBox cb2;
    @BindView(R.id.llType)
    LinearLayout llType;
    @BindView(R.id.default_box)
    CheckBox defaultBox;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.delete)
    TextView delete;


    private ArrayList<String> optionsProvinces = new ArrayList<>();
    private ArrayList<ArrayList<String>> optionsCities = new ArrayList<>();
    private ArrayList<ArrayList<String>> optionsCitiesItem03;
    private ArrayList<ArrayList<ArrayList<String>>> optionsCounties = new ArrayList<>();
    List<AddressEntity> listAddress;
    int optionsAddress1 = 0, optionsAddress2 = 0, optionsAddress3 = 0;
    OptionsPickerView pvOptions;
    String name = "";
    String phone = "";
    String areaId = "";
    String detail = "";
    String type = "";
    String address = "";
    String isDef = "";
    String url = "";

    @Override
    public boolean initToolbar() {
        toolbar.setContentInsetsAbsolute(0, 0);
        mTitle.setTextSize(18);
        mTitle.setText("收货地址");
        mRight.setTextSize(18);
        mRight.setText("修改");
        setSupportActionBar(toolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_goods_address_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        initAddress(JSON.parseArray(getJson("address.json", mContext), AddressEntity.class));

    }

    private void initView() {

        if (getIntent().getSerializableExtra("bean") != null) {
            entity = (TakeGoodsAddressEntity) getIntent().getSerializableExtra("bean");
            tvName.setText(entity.getName());
            tvPhone.setText(entity.getPhone());
            if (entity.getType() != null && !TextUtils.isEmpty(entity.getType())) {

                if (entity.getType().equals("0")) {
                    cb1.setChecked(true);
                    cb2.setChecked(false);
                } else {
                    cb1.setChecked(false);
                    cb2.setChecked(true);
                }
            }
            if (entity.getIsDef() != null && !TextUtils.isEmpty(entity.getIsDef())) {
                layout.setVisibility(View.VISIBLE);
                if (entity.getIsDef().equals("1")) {
                    defaultBox.setChecked(true);
                    layout.setVisibility(View.GONE);
                } else {
                    defaultBox.setChecked(false);
                }

            }
            tvAddress.setText(entity.getArea());
            address = entity.getArea();
            tvAddressDetail.setText(entity.getDetail());
        }
    }

    @OnClick({R.id.mLeftImg, R.id.mRight, R.id.tvAddress, R.id.cb1, R.id.cb2, R.id.delete})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.mLeftImg:
                finish();
                break;

            case R.id.cb1:
                cb1.setChecked(true);
                cb2.setChecked(false);

                break;
            case R.id.cb2:
                cb1.setChecked(false);
                cb2.setChecked(true);
                break;
            case R.id.mRight://修改地址
//                intent = new Intent(mContext, AddGoodsAddressActivity.class);
//                intent.putExtra("className", "编辑收货地址");
//                Bundle b = new Bundle();
//                b.putSerializable("bean", entity);
//                intent.putExtras(b);
//                startActivityForResult(intent, RESULTCODE_REFRESH);


                name = tvName.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(mContext, "请输入收货人姓名", Toast.LENGTH_SHORT).show();
                    tvName.setShakeAnimation();
                    return;
                }
                phone = tvPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(mContext, "请输入收货人手机号", Toast.LENGTH_SHORT).show();
                    tvPhone.setShakeAnimation();
                    return;
                }

                if (!Utils.isMobileNO(tvPhone.getText().toString())) {
                    Toast.makeText(mContext, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    tvPhone.setShakeAnimation();
                    return;
                }
                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(mContext, "请选择所在地区(省、市、区)", Toast.LENGTH_SHORT).show();
                    if (pvOptions != null) {
                        pvOptions.setSelectOptions(optionsAddress1, optionsAddress2, optionsAddress3);
                        pvOptions.show();
                    }
                    return;
                }
                detail = tvAddressDetail.getText().toString().trim();
                if (TextUtils.isEmpty(detail)) {
                    Toast.makeText(mContext, "请输入详细地址(不用输入省、市、区)", Toast.LENGTH_SHORT).show();
                    tvAddressDetail.setShakeAnimation();
                    return;
                }

                if (cb1.isChecked()) {
                    type = "0";
                } else if (cb2.isChecked()) {
                    type = "1";
                } else {
                    type = "";
                }

                if (defaultBox.isChecked()) {
                    isDef = "1";
                } else {
                    isDef = "0";
                }
                Map<String, String> map = new HashMap<>();
                map.put("id", App.getLoginUserId());
                map.put("addressId", entity.getId());
                map.put("type", type);
                if (!TextUtils.isEmpty(name)) map.put("name", name + "");
                if (!TextUtils.isEmpty(phone)) map.put("phone", phone + "");
                if (!TextUtils.isEmpty(areaId)) map.put("areaId", areaId + "");
                if (!TextUtils.isEmpty(detail)) map.put("detail", detail + "");
                if (!TextUtils.isEmpty(isDef)) map.put("isDef", isDef + "");
                map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() + entity.getId(),
                        App.salt).toLowerCase());
                OkGo.<NetEntity<String>>post(Urls.ModifyAddress).params(map).tag(this).execute(new DialogCallback<NetEntity<String>>(mContext) {


                    @Override
                    public void onSuccess(Response<NetEntity<String>> response) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {

                            setResult(RESULTCODE_REFRESH);
                            finish();
                        }
                    }
                });


                setResult(RESULTCODE_REFRESH);
                finish();
                break;


            case R.id.tvAddress:
                if (pvOptions != null) {
                    pvOptions.setSelectOptions(optionsAddress1, optionsAddress2, optionsAddress3);
                    pvOptions.show();
                }
                break;


            case R.id.delete:

                Map<String, String> map1 = new HashMap<>();
                map1.put("id", App.getLoginUserId());
                map1.put("addressId", entity.getId());
                map1.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() + entity.getId(),
                        App.salt).toLowerCase());
                OkGo.<NetEntity<Void>>post(Urls.DeleteAddress).params(map1).tag(this).execute(new DialogCallback<NetEntity<Void>>(mContext) {


                    @Override
                    public void onSuccess(Response<NetEntity<Void>> response) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {

                            setResult(RESULTCODE_REFRESH);
                            finish();
                        }
                    }
                });
                break;
        }

    }

    private void operationAddress(String urls) {
        Map<String, String> map = new HashMap<>();
        map.put("id", App.getLoginUserId());
        map.put("addressId", entity.getId());
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() + entity.getId(),
                App.salt).toLowerCase());
        OkGo.<NetEntity<String>>post(urls).params(map).tag(this).execute(new DialogCallback<NetEntity<String>>(mContext) {

            @Override
            public void onSuccess(Response<NetEntity<String>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {

                    setResult(RESULTCODE_REFRESH);
                    finish();
                }
            }
        });

    }


    private void initAddress(final List<AddressEntity> addressEntities) {
        this.listAddress = addressEntities;
        pvOptions = new OptionsPickerView(mContext);
        ArrayList<String> city;
        ArrayList<String> counties = null;
        for (AddressEntity p : listAddress) {
            optionsCitiesItem03 = new ArrayList<>();
            city = new ArrayList<>();
            optionsProvinces.add(p.getAreaName());
            for (AddressEntity.CitiesBean c : p.getCities()) {
                city.add(c.getAreaName());
                counties = new ArrayList<>();
                for (AddressEntity.CitiesBean.CountiesBean countiesBean : c.getCounties()) {
                    counties.add(countiesBean.getAreaName());
                }
                optionsCitiesItem03.add(counties);
            }
            optionsCounties.add(optionsCitiesItem03);
            optionsCities.add(city);
        }

        pvOptions.setPicker(optionsProvinces, optionsCities, optionsCounties,
                true);
        pvOptions.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {

            }
        });
        pvOptions.setCyclic(false);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                try {
                    optionsAddress1 = options1;
                    optionsAddress2 = option2;
                    optionsAddress3 = options3;
                    address = addressEntities.get(options1).getAreaName() + addressEntities.get(options1).getCities().get(option2).getAreaName() + addressEntities.get(options1).getCities().get(option2).getCounties().get(options3).getAreaName();
                    areaId = addressEntities.get(options1).getCities().get(option2).getCounties().get(options3).getAreaId();
                    tvAddress.setText(address);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        if (pvOptions != null && pvOptions.isShowing()) {
            pvOptions.dismiss();
        } else {
            finish();
        }
    }

    public static String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
