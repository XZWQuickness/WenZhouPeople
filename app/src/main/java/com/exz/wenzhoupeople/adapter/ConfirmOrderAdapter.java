package com.exz.wenzhoupeople.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.entity.GouWuCheEntity;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pc on 2017/8/29.
 */

public class ConfirmOrderAdapter extends BaseQuickAdapter<GouWuCheEntity.GoodsInfoBean, BaseViewHolder> {
    @BindView(R.id.img)
    SimpleDraweeView img;
    @BindView(R.id.goodsName)
    TextView goodsName;
    @BindView(R.id.goodsChooseClassify)
    TextView goodsChooseClassify;
    @BindView(R.id.priceText)
    TextView priceText;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.count)
    TextView count;
    public ConfirmOrderAdapter() {
        super(R.layout.item_confirm_order,null);
    }
    @Override
    protected void convert(BaseViewHolder helper, GouWuCheEntity.GoodsInfoBean item) {
        ButterKnife.bind(this,helper.itemView);
        try {
            goodsName.setText(URLDecoder.decode(item.getTitle(),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        count.setText(String.format("x%s", TextUtils.isEmpty(item.getCount()) ? "1" : Double.valueOf(item.getCount()) == 0 ? "1" : item.getCount()));
        goodsChooseClassify.setText(item.getGoodsType());
        price.setText(item.getPrice());
        img.setImageURI(item.getImgUrl());

    }
}
