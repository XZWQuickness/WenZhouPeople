package com.exz.wenzhoupeople.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.entity.BalanceChangeModel;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by weicao on 2017/2/10.
 */

public class BanlanceChangeAdapter extends BaseQuickAdapter<BalanceChangeModel, BaseViewHolder> {


    @BindView(R.id.pic)
    SimpleDraweeView pic;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.dataTime)
    TextView dataTime;
    @BindView(R.id.price)
    TextView price;

    public BanlanceChangeAdapter() {
        super(R.layout.item_banlance_change, new ArrayList<BalanceChangeModel>());
    }

    @Override
    protected void convert(final BaseViewHolder helper, BalanceChangeModel item) {
        View convertView = helper.getConvertView();
        ButterKnife.bind(this, convertView);


        dataTime.setText(item.getDate());
        content.setText(item.getTitle());

        if (item.getIsIncrease().equals("0")){
            price.setText(String.format("-%s", item.getMoney()));
            pic.setImageResource(R.mipmap.icon_account_recharge);
        }else {
            price.setText(String.format("+%s", item.getMoney()));
            pic.setImageResource(R.mipmap.icon_account_chong);

        }

//        if (item.getPayType().equals("1")){
//            payType.setText("支付宝");
//        }else {
//            payType.setText("余额支付");
//
//        }
//        if (item.getType().equals("1")||item.getType().equals("6")||item.getType().equals("2")){
//            money.setText("+"+item.getMoney()+"元");
//        }else {
//            money.setText("-"+item.getMoney()+"元");
//        }
//
//        note.setText(item.getNote());
//        time.setText(item.getTime());

    }

}
