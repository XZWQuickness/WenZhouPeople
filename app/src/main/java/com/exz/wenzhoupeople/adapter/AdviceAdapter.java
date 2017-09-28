package com.exz.wenzhoupeople.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.entity.AdviceModel;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by weicao on 2017/3/24.
 */

public class AdviceAdapter extends BaseQuickAdapter<AdviceModel, BaseViewHolder> {


    @BindView(R.id.orderNumber)
    TextView orderNumber;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.pic)
    SimpleDraweeView pic;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.dataTime)
    TextView dataTime;
    @BindView(R.id.mall_name)
    TextView mallName;
    @BindView(R.id.buyer_msg)
    TextView buyerMsg;
    @BindView(R.id.seller_msg)
    TextView sellerMsg;
    @BindView(R.id.msg)
    LinearLayout msg;
    @BindView(R.id.btn_advice)
    Button btnAdvice;

    private String type;

    public AdviceAdapter(String type) {
        super(R.layout.item_advice, new ArrayList<AdviceModel>());
        this.type = type;
    }


    @Override
    protected void convert(BaseViewHolder helper, final AdviceModel item) {
        ButterKnife.bind(this, helper.itemView);

        if (type.equals("0")) {
            msg.setVisibility(View.GONE);
            status.setVisibility(View.GONE);
        } else {
            btnAdvice.setVisibility(View.GONE);
        }

        helper.addOnClickListener(R.id.btn_advice);


    }
}
