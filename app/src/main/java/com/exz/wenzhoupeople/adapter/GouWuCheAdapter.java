package com.exz.wenzhoupeople.adapter;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.entity.GouWuCheEntity;
import com.exz.wenzhoupeople.fragment.GouWuCheFragment;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pc on 2017/8/31.
 */

public class GouWuCheAdapter extends BaseQuickAdapter<GouWuCheEntity.GoodsInfoBean, BaseViewHolder> {

    @BindView(R.id.check)
    TextView check;
    @BindView(R.id.img)
    SimpleDraweeView img;
    @BindView(R.id.goodsName)
    TextView goodsName;
    @BindView(R.id.guige)
    TextView guige;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.minus)
    ImageView minus;
    @BindView(R.id.count)
    TextView count;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.countLay)
    LinearLayout countLay;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    @BindView(R.id.swipeLay)
    SwipeMenuLayout swipeLay;
    GouWuCheFragment c;

    public GouWuCheAdapter(GouWuCheFragment c) {
        super(R.layout.adapter_gouwuch, null);
        this.c = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, final GouWuCheEntity.GoodsInfoBean item) {
        ButterKnife.bind(this, helper.itemView);
        check.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, item.isCheck() ? R.mipmap.ic_gwc_true : R.mipmap.ic_gwc_false), null, null, null);
        img.setImageURI(item.getImgUrl());
        goodsName.setText(item.getTitle());
        guige.setText(item.getGoodsType());
        count.setText(item.getCount());
        price.setText("￥" + item.getPrice());
        if (!TextUtils.isEmpty(getClickState()) && getClickState().equals("2")) {
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }
        helper.addOnClickListener(R.id.llChekc);
        helper.addOnClickListener(R.id.minus);
        helper.addOnClickListener(R.id.add);
        helper.addOnClickListener(R.id.count);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDeleteOnclick.deleteOnclick(item);
            }
        });
    }

    private String clickState = "";

    public String getClickState() {
        return clickState;
    }

    public void setClickState(String clickState) {
        this.clickState = clickState;
    }

    public void setDeleteOnclick(deleteOnclick mDeleteOnclick) {
        this.mDeleteOnclick = mDeleteOnclick;
    }

    public deleteOnclick mDeleteOnclick;

    public interface deleteOnclick {
        void deleteOnclick(GouWuCheEntity.GoodsInfoBean item);
    }
}
