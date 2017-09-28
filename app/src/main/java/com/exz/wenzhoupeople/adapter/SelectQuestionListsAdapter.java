package com.exz.wenzhoupeople.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.entity.EnshrineModel;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectQuestionListsAdapter extends BaseItemDraggableAdapter<EnshrineModel, BaseViewHolder> {


    @BindView(R.id.iv_head_img)
    SimpleDraweeView ivHeadImg;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.layout)
    RelativeLayout layout;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    @BindView(R.id.ll_hidden)
    LinearLayout llHidden;
    @BindView(R.id.ll_item)
    SwipeMenuLayout llItem;
    @BindView(R.id.reduce)
    TextView reduce;
    @BindView(R.id.failed)
    TextView failed;

    private int i;

    public SelectQuestionListsAdapter(int i) {
        super(R.layout.item_enshrine, new ArrayList<EnshrineModel>());
        this.i = i;
    }

    @Override
    public void convert(final BaseViewHolder helper, final EnshrineModel item) {
        ButterKnife.bind(this, helper.itemView);

        helper.addOnClickListener(R.id.layout);


        if (item.getState().equals("1")){
            reduce.setText(String.format("比收藏时降价%s", item.getDownPrice()));
            failed.setVisibility(View.GONE);
        }else if (item.getState().equals("2")){
            failed.setVisibility(View.VISIBLE);
            reduce.setVisibility(View.GONE);
        }else {
            failed.setVisibility(View.GONE);
            reduce.setVisibility(View.GONE);
        }

        price.setText(item.getPrice());
        ivHeadImg.setImageURI(item.getImgUrl());
        content.setText(item.getTitle());



        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onDel(helper.getAdapterPosition());
                }
            }
        });


        if (i == 1) {
            failed.setVisibility(View.GONE);
        } else if (i == 2) {
            reduce.setVisibility(View.GONE);
        }else {
        }

    }


    public interface onSwipeListener {
        void onDel(int pos);

    }

    private onSwipeListener mOnSwipeListener;

    public onSwipeListener getOnDelListener() {
        return mOnSwipeListener;
    }

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }


}
