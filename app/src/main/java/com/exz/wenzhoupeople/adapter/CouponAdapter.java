package com.exz.wenzhoupeople.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.entity.CouponEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pc on 2017/8/28.
 */

public class CouponAdapter extends BaseQuickAdapter<CouponEntity, BaseViewHolder> {
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.limitMoney)
    TextView limitMoney;
    @BindView(R.id.limitDate)
    TextView limitDate;

    public CouponAdapter() {
        super(R.layout.adapter_coupon, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponEntity item) {
        View itemView = helper.itemView;
        ButterKnife.bind(this, itemView);
        money.setText(item.getMoney());
        limitMoney.setText("满"+item.getLimitMoney()+"减"+item.getMoney());
        limitDate.setText(item.getLimitDate());
    }
}
