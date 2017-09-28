package com.exz.wenzhoupeople.adapter;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.entity.TakeGoodsAddressEntity;
import com.exz.wenzhoupeople.utils.TextUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pc on 2017/8/29.
 */

public class TakeGoodsAddressAdapter extends BaseQuickAdapter<TakeGoodsAddressEntity, BaseViewHolder> {
    @BindView(R.id.select)
    ImageView select;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.llLay)
    LinearLayout llLay;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.type)
    TextView type;
    String className;

    public TakeGoodsAddressAdapter(String className) {
        super(R.layout.adapter_select_goods_address, null);
        this.className = className;
    }

    @Override
    protected void convert(BaseViewHolder helper, TakeGoodsAddressEntity item) {
        ButterKnife.bind(this, helper.itemView);
        name.setText(item.getName());
        phone.setText(item.getPhone());
        address.setText(item.getIsDef().equals("1")?"[默认]"+item.getArea() + item.getDetail():item.getArea() + item.getDetail());
        if(item.getIsDef().equals("1")){

            TextUtils.setTextChanesColor(mContext,address, ContextCompat.getColor(mContext,R.color.blue2),address.getText().toString().trim(),0,4);
        }else{
            address.setTextColor(ContextCompat.getColor(mContext,R.color.text_gray));
        }

        if (className.equals("选择收货地址")) {
            if (item.getIsDef().equals("1")) {
                select.setBackgroundResource(R.mipmap.ic_gwc_true);
            } else {
                select.setBackgroundResource(R.mipmap.ic_gwc_false);
            }
        } else {
            select.setBackgroundResource(R.mipmap.ic_right_gray_next);
        }
        if (item.getType().equals("0")) {
            type.setText("公司");
            type.setVisibility(View.VISIBLE);
        }else if(item.getType().equals("1")){
            type.setText("家");
            type.setVisibility(View.VISIBLE);
        }else{
            type.setVisibility(View.GONE);
        }
    }
}
