package com.exz.wenzhoupeople.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.entity.AddressModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by weicao on 2017/3/24.\
 *
 */

public class AddressAdapter extends BaseQuickAdapter<AddressModel, BaseViewHolder> {


    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.address)
    TextView address;

    public AddressAdapter() {
        super(R.layout.item_address, new ArrayList<AddressModel>());
    }


    @Override
    protected void convert(BaseViewHolder helper, AddressModel item) {
        View convertView = helper.getConvertView();
        ButterKnife.bind(this, convertView);

        if (item.getStu().equals("1")){
            address.setText(String.format("[默认]%s", item.getAddress()));
        }else {
            address.setText(item.getAddress());
        }

        if(!TextUtils.isEmpty(item.getPhone()) && item.getPhone().length() > 6 ){
            StringBuilder sb  =new StringBuilder();
            for (int i = 0; i < item.getPhone().length(); i++) {
                char c = item.getPhone().charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }

            phone.setText(sb.toString());
        }

        name.setText(item.getName());
        address.setText(item.getAddress());
    }


}
