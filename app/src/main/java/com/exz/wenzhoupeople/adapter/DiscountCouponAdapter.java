package com.exz.wenzhoupeople.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.entity.DiscountCouponModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by weicao on 2017/3/24.
 */

public class DiscountCouponAdapter extends BaseQuickAdapter<DiscountCouponModel, BaseViewHolder> {


    @BindView(R.id.shopName)
    TextView shopName;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.price1)
    TextView price1;
    @BindView(R.id.dataTime)
    TextView dataTime;

    public DiscountCouponAdapter() {
        super(R.layout.item_discount_coupon, new ArrayList<DiscountCouponModel>());
    }


    @Override
    protected void convert(BaseViewHolder helper, final DiscountCouponModel item) {
        ButterKnife.bind(this, helper.itemView);

        price.setText(String.format("满%s可用", item.getLimitMoney()));
        dataTime.setText(item.getLimitDate());
        price1.setText(item.getMoney());



    }
}
