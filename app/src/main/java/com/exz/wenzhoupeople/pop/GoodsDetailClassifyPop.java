package com.exz.wenzhoupeople.pop;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.activity.ConfirmOrderActivity;
import com.exz.wenzhoupeople.activity.LoginInActivity;
import com.exz.wenzhoupeople.adapter.GoodsDetailClassifyAdapter;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.entity.GoodsClassifyBean;
import com.exz.wenzhoupeople.utils.DialogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.szw.lib.myframework.config.Constants;
import cn.com.szw.lib.myframework.listener.OnNumListener;
import cn.com.szw.lib.myframework.utils.RecycleViewDivider;
import cn.com.szw.lib.myframework.utils.RxBus;
import cn.com.szw.lib.myframework.utils.Utils;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;
import cn.com.szw.lib.myframework.view.CustomLoadMoreView;
import razerdp.basepopup.BasePopupWindow;

import static com.exz.wenzhoupeople.config.Urls.AddShopCar;

/**
 * Created by 史忠文
 * on 2017/1/24.
 */

public class GoodsDetailClassifyPop extends BasePopupWindow implements OnNumListener {


    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.inventory)
    TextView tvInventory;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.count)
    TextView count;
    @BindView(R.id.minus)
    ImageView minus;
    @BindView(R.id.addCar)
    TextView addCar;
    @BindView(R.id.buy)
    TextView buy;
    @BindView(R.id.img)
    SimpleDraweeView img;
    @BindView(R.id.animationView)
    RelativeLayout animationView;
    private long countIndex = 1;
    private GoodsDetailClassifyAdapter<GoodsClassifyBean.RankInfoBean> adapter;
    Activity context;
    GoodsClassifyBean data;
    List<GoodsClassifyBean.RankCombBean> comList = new ArrayList<>();

    public GoodsDetailClassifyPop(Activity context) {
        super(context);
        this.context = context;
        try {
            RxBus.get().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter = new GoodsDetailClassifyAdapter<>();
        adapter.bindToRecyclerView(mRecyclerView);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(getContext(), R.color.white)));
        adapter.setOnTagClickListener(new GoodsDetailClassifyAdapter.OnTagClickListener() {
            @Override
            public void OnTagClickListener(View view, int position, GoodsClassifyBean.RankInfoBean entity) {
                String typeName = "请选择";
                String choseStr = "";//选中规格名称
                boolean typeNameB = false;
                String rankCombId = "";
                for (GoodsClassifyBean.RankInfoBean rankInfo : adapter.getData()) {
                    for (GoodsClassifyBean.RankInfoBean.SubRankBean sub : rankInfo.getSubRank()) {
                        if (sub.getState().equals("1")) {
                            typeNameB = true;
                            choseStr += "\"" + sub.getRankName() + "\" ";
                            rankCombId += sub.getRankId() + ",";
                        }
                    }
                    if (!typeNameB) {
                        typeName += " " + rankInfo.getRankName() + " ";
                    }
                    typeNameB = false;
                }
                data.setTypeName(typeName.equals("请选择") ? "已选择 " + choseStr : typeName);
                if (rankCombId.length() > 0) {
                    rankCombId = rankCombId.substring(0, rankCombId.length() - 1);
                    data.setRankCombId(rankCombId);
                    for (GoodsClassifyBean.RankCombBean comb : comList) {

                        if (dimMatching(comb, rankCombId)) {
                            data.setSkuid(comb.getSkuid());
                            inventory = comb.getStock();
                            data.setStock(comb.getStock() + "");
                            data.setPrice(comb.getPrice());
                            data.setHeaderImg(comb.getImage());
                        }
                    }
                }
                iniInfo();
            }
        });

    }

    public void setData(GoodsClassifyBean data) {
        this.data = data;
        if (data.getRankInfo()!=null&&data.getRankInfo().size() > 0) {
            data.setSkuid("");
        }
        iniInfo();
        adapter.setRankComb(data.getRankComb());
        adapter.setNewData(data.getRankInfo());

        if(data.getRankComb()!=null){
            for (GoodsClassifyBean.RankCombBean rank : data.getRankComb()) {
                if (rank.getStock() > 0) {
                    comList.add(rank);
                }
            }
        }

    }

    public boolean dimMatching(GoodsClassifyBean.RankCombBean comb, String rankCombId) {
        boolean b = false;
        if (rankCombId.length() == comb.getRankCombId().length()) {
            if (rankCombId.split(",").length == 1 && rankCombId.equals(comb.getRankCombId())) {
                return b = true;
            } else if (rankCombId.split(",").length == 2) {
                String rankCombId0 = rankCombId.split(",")[0];
                String rankCombId1 = rankCombId.split(",")[1];
                String rankRankCombId0 = comb.getRankCombId().split(",")[0];
                String rankRankCombId1 = comb.getRankCombId().split(",")[1];

                if (rankCombId.equals(comb.getRankCombId())) {
                    return b = true;
                } else if (rankCombId0.equals(rankRankCombId1) && rankCombId1.equals(rankRankCombId0)) {
                    return b = true;
                }
                Log.i("规格","rankCombId0.equals(rankRankCombId1)="+rankCombId0.equals(rankRankCombId1)+"rankCombId1.equals(rankRankCombId0)="+rankCombId1.equals(rankRankCombId0));
            }

        } else {
            b = false;
        }
        return b;
    }

    private void iniInfo() {
        this.img.setImageURI(data.getHeaderImg());
        this.price.setText("￥" + data.getPrice());
        this.tvInventory.setText("库存" + data.getStock() + "件");
        this.type.setText(data.getTypeName());
        inventory = !TextUtils.isEmpty(data.getStock()) ? Integer.parseInt(data.getStock()) : 0;

    }


    @Override
    public void dismiss() {
        super.dismiss();

    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        View inflate = View.inflate(getContext(), R.layout.pop_classify, null);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.animationView);
    }

    @Override
    protected Animation initShowAnimation() {
        Animation shakeAnimate = new TranslateAnimation(0, 0, 600, 0);
        shakeAnimate.setDuration(300);
        return shakeAnimate;
    }

    @Override
    protected Animation initExitAnimation() {
        Animation shakeAnimate = new TranslateAnimation(0, 0, 0, 900);
        shakeAnimate.setDuration(300);
        return shakeAnimate;
    }

    public static int inventory = 200, currentCount;

    @OnClick({R.id.close, R.id.add, R.id.count, R.id.minus, R.id.addCar, R.id.buy})
    public void onViewClicked(View view) {
        currentCount = Integer.parseInt(count.getText().toString().trim());
        switch (view.getId()) {
            case R.id.minus://购买数量减
                currentCount = currentCount <= 1 ? 1 : --currentCount;
                count.setText(String.format("%s", currentCount));
                break;
            case R.id.add://购买数量加
                currentCount += 1;
                if (currentCount > inventory) {
                    currentCount = inventory;
                }
                count.setText(String.format("%s", currentCount));
                break;
            case R.id.count://购买数量
                DialogUtils.ChangeNum(context, Integer.parseInt(count.getText().toString().trim()), this);
                break;
            case R.id.addCar://加入购物车
                if (!App.checkUserLogin()) {
                    Utils.startActivity(getContext(), LoginInActivity.class);
                    return;
                }
                if (Integer.parseInt(data.getStock()) < 1) {
                    Toast.makeText(context, "当前规格暂无库存", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(data.getSkuid())) {
                    Toast.makeText(context, data.getTypeName(), Toast.LENGTH_SHORT).show();
                    return;
                }
                AddShopCar();
                break;
            case R.id.buy://购买
                if (!App.checkUserLogin()) {
                    Utils.startActivity(getContext(), LoginInActivity.class);
                    return;
                }
                if (TextUtils.isEmpty(data.getSkuid())) {
                    Toast.makeText(context, "请选择商品属性", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(context, ConfirmOrderActivity.class);
                context.startActivity(intent);
                break;
            case R.id.close:
                dismiss();
                break;
        }
    }

    private void AddShopCar() {

        Map<String, String> map = new HashMap<>();
        map.put("id", App.getLoginUserId());
        map.put("goodsId", context.getIntent().getStringExtra("goodsId"));
        map.put("skuid", data.getSkuid());
        map.put("count", count.getText().toString().trim());
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() + context.getIntent().getStringExtra("goodsId"), App.salt)
                .toLowerCase());
        OkGo.<NetEntity<String>>post(AddShopCar).params(map).tag(this).execute(new DialogCallback<NetEntity<String>>(context) {
            @Override
            public void onSuccess(Response<NetEntity<String>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    dismiss();
                }
                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

            }

        });
    }

    @Override
    public void onNum(int num) {
        count.setText(num + "");
    }


}
