package com.exz.wenzhoupeople.adapter;

import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.entity.GoodsClassifyBean;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsDetailClassifyAdapter<T extends GoodsClassifyBean.RankInfoBean> extends BaseQuickAdapter<T, BaseViewHolder> {
    public List<GoodsClassifyBean.RankCombBean> rankComb;

    public GoodsDetailClassifyAdapter() {
        super(R.layout.item_goods_detail_classify, new ArrayList<T>());
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final GoodsClassifyBean.RankInfoBean entity) {
        final ViewHolder holder = new ViewHolder(baseViewHolder.itemView);
        holder.classifyName.setText(entity.getRankName());
        final TagAdapter<GoodsClassifyBean.RankInfoBean.SubRankBean> adapter = new TagAdapter<GoodsClassifyBean.RankInfoBean.SubRankBean>(entity.getSubRank()) {
            @Override
            public View getView(FlowLayout parent, int position, GoodsClassifyBean.RankInfoBean.SubRankBean subRankBean) {

//                for (GoodsClassifyBean.RankCombBean comb : rankComb) {
//                    if (comb.getRankCombId().equals(entity.getSubRank().get(position).getRankId())) {
//                        if(comb.getStock()<1){
//                            subRankBean.setState("3");
//                        }else{
//                            subRankBean.setState("1");
//                        }
//
//                    }
//                }
                TextView textView = (TextView) mLayoutInflater.inflate(R.layout.tag_classify, parent, false);
                textView.setText(subRankBean.getRankName());
                switch (subRankBean.getState()) {
                    case "1":
                        textView.setBackgroundResource(R.drawable.tag_classify_can);
                        break;
                    case "2":
                        textView.setBackgroundResource(R.drawable.tag_classify_can);
                        break;
                    case "3":
                        textView.setBackgroundResource(R.drawable.goods_detail_stroke);
                        break;
                }

                textView.setPadding(SizeUtils.dp2px(15), SizeUtils.dp2px(3), SizeUtils.dp2px(15), SizeUtils.dp2px(3));
                return textView;
            }
        };
        holder.tagFlow.setAdapter(adapter);
        for (int i = 0; i < entity.getSubRank().size(); i++) {
            if ("1".equals(entity.getSubRank().get(i).getState())) {
                adapter.setSelectedList(i);
                break;
            }
        }

        holder.tagFlow.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                String state = entity.getSubRank().get(position).getState();
                if (holder.tagFlow.getSelectedList().size() != 0) {
                    for (GoodsClassifyBean.RankInfoBean.SubRankBean goodsSubClassifyBean : entity.getSubRank()) {
                        if ("1".equals(goodsSubClassifyBean.getState())) {
                            goodsSubClassifyBean.setState("2");
                        }
                    }
                }
                entity.getSubRank().get(position).setState("1");
                notifyDataSetChanged();

                if (mOnTagClickListener != null) {
                    mOnTagClickListener.OnTagClickListener(view, position, entity);
                }
                return false;
            }
        });
    }

    public void setOnTagClickListener(OnTagClickListener mOnTagClickListener) {
        this.mOnTagClickListener = mOnTagClickListener;
    }

    public OnTagClickListener mOnTagClickListener;

    public void setRankComb(List<GoodsClassifyBean.RankCombBean> rankComb) {
        this.rankComb = rankComb;
    }


    public interface OnTagClickListener {
        void OnTagClickListener(View view, int position, GoodsClassifyBean.RankInfoBean entity);
    }

    static class ViewHolder {
        @BindView(R.id.classifyName)
        TextView classifyName;
        @BindView(R.id.tagFlow)
        TagFlowLayout tagFlow;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}