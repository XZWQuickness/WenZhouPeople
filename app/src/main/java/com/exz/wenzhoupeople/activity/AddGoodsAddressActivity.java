package com.exz.wenzhoupeople.activity;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;
import cn.com.szw.lib.myframework.view.ClearWriteEditText;

import static com.exz.wenzhoupeople.R.id.addressDetail;
import static com.exz.wenzhoupeople.activity.TakeGoodsAddressActivity.RESULTCODE_REFRESH;

/**
 * Created by pc on 2017/8/30.
 * 增加收货地址
 */

public class AddGoodsAddressActivity extends BaseActivity {
    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.addAddress)
    TextView addAddress;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.name)
    ClearWriteEditText edName;
    @BindView(R.id.phone)
    ClearWriteEditText edPhone;
    @BindView(R.id.address)
    TextView tvAddress;
    @BindView(addressDetail)
    ClearWriteEditText edAddressDetail;
    @BindView(R.id.cb1)
    CheckBox cb1;
    @BindView(R.id.cb2)
    CheckBox cb2;
    @BindView(R.id.cb3)
    CheckBox cb3;
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
        mTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
        mTitle.setText(getIntent().getStringExtra("className"));
        mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
        setSupportActionBar(toolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_add_goods_address;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();

        initAddress(JSON.parseArray(getJson("address.json", mContext), AddressEntity.class));

    }

    private void initView() {
        if (getIntent().getStringExtra("className").equals("新增收货地址")) {
            addAddress.setText("保存地址");
            url = Urls.AddAddress;
        } else {
            addAddress.setText("确定修改");
            url = Urls.ModifyAddress;
        }
        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cb2.setChecked(false);
                }
            }
        });
        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cb1.setChecked(false);
                }
            }
        });

        if (getIntent().getSerializableExtra("bean") != null) {
            TakeGoodsAddressEntity entity = (TakeGoodsAddressEntity) getIntent().getSerializableExtra("bean");
            edName.setText(entity.getName());
            edPhone.setText(entity.getPhone());
            edAddressDetail.setText(entity.getDetail());
            if (entity.getType().equals("0")) {
                cb1.setChecked(true);
            } else if (entity.getType().equals("1")) {
                cb2.setChecked(true);
            }
        }
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

    @OnClick({R.id.mLeftImg, R.id.address, R.id.addAddress})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLeftImg:
                finish();
                break;
            case R.id.address:
                if (pvOptions != null) {
                    pvOptions.setSelectOptions(optionsAddress1, optionsAddress2, optionsAddress3);
                    pvOptions.show();
                }
                break;
            case R.id.addAddress:
                if (getIntent().getStringExtra("className").equals("新增收货地址")) {
                    name = edName.getText().toString().trim();
                    if (TextUtils.isEmpty(name)) {
                        Toast.makeText(mContext, "请输入收货人姓名", Toast.LENGTH_SHORT).show();
                        edName.setShakeAnimation();
                        return;
                    }
                    phone = edPhone.getText().toString().trim();
                    if (TextUtils.isEmpty(phone)) {
                        Toast.makeText(mContext, "请输入收货人手机号", Toast.LENGTH_SHORT).show();
                        edPhone.setShakeAnimation();
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
                    detail = edAddressDetail.getText().toString().trim();
                    if (TextUtils.isEmpty(detail)) {
                        Toast.makeText(mContext, "请输入详细地址(不用输入省、市、区)", Toast.LENGTH_SHORT).show();
                        edAddressDetail.setShakeAnimation();
                        return;
                    }

                }
                if (cb1.isChecked()) {
                    type = "0";
                } else if (cb2.isChecked()) {
                    type = "1";
                } else {
                    type = "";
                }

                if (cb3.isChecked()) {
                    isDef = "1";
                } else {
                    isDef = "0";
                }
                Map<String, String> map = new HashMap<>();
                map.put("id", App.getLoginUserId());
                if (!TextUtils.isEmpty(name)) map.put("name", name + "");
                if (!TextUtils.isEmpty(phone)) map.put("phone", phone + "");
                if (!TextUtils.isEmpty(areaId)) map.put("areaId", areaId + "");
                if (!TextUtils.isEmpty(detail)) map.put("detail", detail + "");
                if (!TextUtils.isEmpty(isDef)) map.put("isDef", isDef + "");
                map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId()+phone,
                        App.salt).toLowerCase());
                OkGo.<NetEntity<String>>post(url).params(map).tag(this).execute(new DialogCallback<NetEntity<String>>(mContext) {


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
        }
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
