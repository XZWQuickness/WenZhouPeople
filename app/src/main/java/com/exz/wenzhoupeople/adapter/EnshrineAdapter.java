package com.exz.wenzhoupeople.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.entity.EnshrineModel;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by weicao on 2017/2/10.
 */

public class EnshrineAdapter extends BaseQuickAdapter<EnshrineModel, BaseViewHolder> {



    public EnshrineAdapter() {
        super(R.layout.item_enshrine, new ArrayList<EnshrineModel>());
    }

    @Override
    protected void convert(final BaseViewHolder helper, EnshrineModel item) {
        View convertView = helper.getConvertView();
        ButterKnife.bind(this, convertView);




    }

}
