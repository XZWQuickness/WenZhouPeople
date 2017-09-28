package com.exz.wenzhoupeople.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.entity.MsgListEntity;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pc on 2017/8/22.
 */

public class MsgListAdapter extends BaseQuickAdapter<MsgListEntity, BaseViewHolder> {
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.img)
    SimpleDraweeView img;
    @BindView(R.id.subTitle)
    TextView subTitle;
    @BindView(R.id.line)
    View line;

    public MsgListAdapter() {
        super(R.layout.adapter_msg_list, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, MsgListEntity item) {
        View itemView = helper.itemView;
        ButterKnife.bind(this, itemView);
        line.setVisibility(helper.getAdapterPosition() == getData().size() - 1 ? View.VISIBLE : View.GONE);
        date.setText(item.getDate());
        img.setImageURI(item.getImg());
        try {
            title.setText(item.getTitle());
            subTitle.setText(item.getSubTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
