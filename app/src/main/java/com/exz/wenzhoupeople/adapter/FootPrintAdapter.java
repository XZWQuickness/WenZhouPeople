package com.exz.wenzhoupeople.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.entity.FootPrintModel;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.exz.wenzhoupeople.activity.FootPrintActivity.Time;


/**
 * Created by weicao on 2017/3/24.\
 */

public class FootPrintAdapter extends BaseQuickAdapter<FootPrintModel, BaseViewHolder> {


    @BindView(R.id.dataTime)
    TextView dataTime;
    @BindView(R.id.iv_head_img)
    SimpleDraweeView ivHeadImg;
    @BindView(R.id.content_1)
    TextView content1;
    @BindView(R.id.price1)
    TextView price1;
    @BindView(R.id.layout)
    RelativeLayout layout;



    public FootPrintAdapter() {
        super(R.layout.item_foot_print_one, new ArrayList<FootPrintModel>());
    }


    @Override
    protected void convert(BaseViewHolder helper, FootPrintModel item) {
        View convertView = helper.getConvertView();
        ButterKnife.bind(this, convertView);


        content1.setText(item.getTitle());
        price1.setText(item.getPrice());
        ivHeadImg.setImageURI(item.getImgUrl());

        setData(mContext, dataTime, item.getDate());
//
        helper.addOnClickListener(R.id.layout);
//

    }

    private void setData(Context mContext, TextView textView, String data) {
        Date beginTime = null;


        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime = new SimpleDateFormat("MM月dd号");


        try {
            beginTime = currentTime.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (Time == null) {
            Time = beginTime;
            dataTime.setText(data);
        } else {

            if (beginTime != null) {

                if (Time.getTime()==beginTime.getTime() ) {
                    textView.setVisibility(View.GONE);
                    dataTime.setText(data);
                } else {
                    Time = beginTime;
                    textView.setVisibility(View.VISIBLE);
                    dataTime.setText(data);

                }
            }


        }


    }


}
