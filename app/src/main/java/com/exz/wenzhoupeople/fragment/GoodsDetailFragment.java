package com.exz.wenzhoupeople.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.xlhratingbar_lib.XLHRatingBar;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.activity.LoginInActivity;
import com.exz.wenzhoupeople.adapter.GoodsEvaluateAdapter;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.config.Urls;
import com.exz.wenzhoupeople.entity.CouponEntity;
import com.exz.wenzhoupeople.entity.GoodsDetailEntity;
import com.exz.wenzhoupeople.pop.CouponMore;
import com.exz.wenzhoupeople.utils.MyScrollView;
import com.exz.wenzhoupeople.utils.PullUpToLoadMore;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.com.szw.lib.myframework.base.MyBaseFragment;
import cn.com.szw.lib.myframework.config.Constants;
import cn.com.szw.lib.myframework.imageloder.GlideApp;
import cn.com.szw.lib.myframework.utils.RecycleViewDivider;
import cn.com.szw.lib.myframework.utils.Utils;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;
import cn.com.szw.lib.myframework.utils.preview.PreviewActivity;

import static cn.com.szw.lib.myframework.utils.preview.PreviewActivity.PREVIEW_INTENT_IMAGES;
import static cn.com.szw.lib.myframework.utils.preview.PreviewActivity.PREVIEW_INTENT_IS_CAN_DELETE;
import static cn.com.szw.lib.myframework.utils.preview.PreviewActivity.PREVIEW_INTENT_POSITION;
import static com.exz.wenzhoupeople.config.Urls.CouponList;
import static com.exz.wenzhoupeople.config.Urls.CouponReceive;

/**
 * Created by pc on 2017/8/25.
 */

public class GoodsDetailFragment extends MyBaseFragment {

    Unbinder unbinder;
    @BindView(R.id.MyScrollView)
    MyScrollView MyScrollView;
    @BindView(R.id.mWebView)
    WebView mWebView;
    @BindView(R.id.banner)
    RollPagerView banner;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.oldPrice)
    TextView oldPrice;
    @BindView(R.id.postMoney)
    TextView postMoney;
    @BindView(R.id.saleCount)
    TextView saleCount;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.getCoupon)
    TextView getCoupon;
    @BindView(R.id.property)
    TextView property;
    @BindView(R.id.judgeCount)
    TextView judgeCount;
    @BindView(R.id.moreEvaluate)
    TextView moreEvaluate;
    @BindView(R.id.llEvaluateLay)
    LinearLayout llEvaluateLay;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.fds)
    TextView fds;
    @BindView(R.id.www)
    com.exz.wenzhoupeople.utils.MyScrollView www;
    @BindView(R.id.scrollviewall)
    PullUpToLoadMore scrollviewall;
    @BindView(R.id.mMyScrollView)
    RelativeLayout mMyScrollView;
    private OnMoreCommentListener listener;
    private OnChooseListener onChooseListener;
    CouponMore mCouponMore;
    GoodsEvaluateAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_goods_detail, container, false);
        }
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;

    }

    @Override
    public void initView() {
        MyScrollView.setFocusable(true);
        MyScrollView.scrollTo(0, 20);
        MyScrollView.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int l, int t, int oldl, int oldt) {


                if (onScrollListener != null) {
                    onScrollListener.onScroll(banner.getHeight(), l, t, oldl, oldt);
                }
            }
        });
        initWebView();


    }


    public void setData(GoodsDetailEntity goodsDetaile) {
        mCouponMore = new CouponMore(getActivity());
        banner.setAdapter(new BannerAdapter(getContext(),
                (ArrayList<String>) goodsDetaile.getImgUrls()));
        try {
            title.setText(URLDecoder.decode(goodsDetaile.getTitle(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        price.setText("￥" + goodsDetaile.getPrice());
        oldPrice.setText("价格￥" + goodsDetaile.getOldPrice());
        oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        oldPrice.getPaint().setAntiAlias(true);//抗锯齿
        postMoney.setText("运费" + goodsDetaile.getPostMoney());
        saleCount.setText("销量" + goodsDetaile.getSaleCount());
        address.setText(goodsDetaile.getAddress());
        judgeCount.setText("商品评价(" + goodsDetaile.getJudgeCount() + ")");
        adapter = new GoodsEvaluateAdapter();
        adapter = new GoodsEvaluateAdapter();
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        adapter.setNewData(goodsDetaile.getJudgeList());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(getContext(), R.color.lin_gray)));

        mRecyclerView.setAdapter(adapter);
        mWebView.loadUrl(Urls.GoodsInfo + goodsDetaile.getId());
        getCoupon.setVisibility(goodsDetaile.getIsCoupon().equals("1") ? View.VISIBLE : View.GONE);
        mCouponMore.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                CouponEntity couponEntity = (CouponEntity) adapter.getData().get(position);
                CouponReceive(couponEntity.getId());
            }
        });
//        mGoodsDetailClassifyPop.setInfo(goodsDetaile.getImgUrls().size() > 0 ? goodsDetaile.getImgUrls().get(0) : "", goodsDetaile.getPrice(), goodsDetaile.getStock());
    }


    public class BannerAdapter extends StaticPagerAdapter {

        private Context ctx;
        private ArrayList<String> list;

        public BannerAdapter(Context ctx, ArrayList<String> list) {
            this.ctx = ctx;
            this.list = list;
        }

        @Override
        public View getView(ViewGroup container, final int position) {
            View inflate = View.inflate(ctx, R.layout.banner_imgview, null);
            ImageView imageView = (ImageView) inflate.findViewById(R.id.img);
            //加载图片
            GlideApp.with(ctx)                             //配置上下文
//                            .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                    .load(list.get(position))
                    .error(R.mipmap.icon_empty)           //设置错误图片
                    .placeholder(R.mipmap.icon_empty)     //设置占位图片
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                    .into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), PreviewActivity.class);
                    intent.putExtra(PREVIEW_INTENT_POSITION, position);
                    intent.putExtra(PREVIEW_INTENT_IMAGES, list);
                    intent.putExtra(PREVIEW_INTENT_IS_CAN_DELETE, false);
                    ctx.startActivity(intent);
                }
            });
            return inflate;
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }


    private void initWebView() {

        // 支持js
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setVerticalScrollbarOverlay(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setHorizontalScrollbarOverlay(false);
        mWebView.setFocusable(false);
        mWebView.setEnabled(false);
        mWebView.requestFocus();

        WindowManager wm = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        mWebView.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, height));
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;   // 在当前webview内部打开url
            }
        });


    }


    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    private OnScrollListener onScrollListener;


    @OnClick({R.id.getCoupon, R.id.property, R.id.llEvaluateLay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.getCoupon://领取优惠券
                if (!App.checkUserLogin()) {
                    Utils.startActivity(getContext(), LoginInActivity.class);
                    return;
                }
                CouponList();

                break;
            case R.id.property://商品属性
                if (onChooseListener != null) {

                    onChooseListener.onChoose();
                }
                break;
            case R.id.llEvaluateLay://评价
                listener.onChange(-1);
                break;
        }
    }

    private void CouponList() {
        Map<String, String> map = new HashMap<>();
        map.put("goodsId", "0");
        map.put("requestCheck", EncryptUtils.encryptMD5ToString("0", App.salt)
                .toLowerCase());
        OkGo.<NetEntity<List<CouponEntity>>>post(CouponList).params(map).tag(this).execute(new DialogCallback<NetEntity<List<CouponEntity>>>(getContext()) {
            @Override
            public void onSuccess(Response<NetEntity<List<CouponEntity>>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    mCouponMore.setData(response.body().getData());
                    mCouponMore.showPopupWindow();
                } else {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response<NetEntity<List<CouponEntity>>> response) {
                super.onError(response);
            }
        });
    }

    /**
     * 领取优惠券
     */
    private void CouponReceive(String id) {
        Map<String, String> map = new HashMap<>();
        map.put("id", App.getLoginUserId());
        map.put("couponId", id);
        map.put("goodsId", "0");
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() + id, App.salt)
                .toLowerCase());
        OkGo.<NetEntity<String>>post(CouponReceive).params(map).tag(this).execute(new DialogCallback<NetEntity<String>>(getContext()) {
            @Override
            public void onSuccess(Response<NetEntity<String>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    mCouponMore.dismiss();
                }
                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }


    public interface OnScrollListener {
        void onScroll(int h, int l, int t, int oldl, int oldt);
    }

    public void setListener(OnMoreCommentListener listener) {
        this.listener = listener;
    }


    public interface OnMoreCommentListener {
        void onChange(int id);
    }

    public void setChooseListener(OnChooseListener listener) {
        this.onChooseListener = listener;
    }

    public interface OnChooseListener {
        void onChoose();
    }

    static class ViewEvaluateHolder {
        @BindView(R.id.headerImg)
        ImageView headerImg;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.ratingBar)
        XLHRatingBar ratingBar;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.content)
        TextView content;

        ViewEvaluateHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    String json = "[\n" +
            "    {\n" +
            "        \"id\": \"1\",\n" +
            "        \"money\": \"5\",\n" +
            "        \"limitMoney\": \"99\",\n" +
            "        \"limitDate\": \"2016.01.25 - 2016.02.25\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"2\",\n" +
            "        \"money\": \"10\",\n" +
            "        \"limitMoney\": \"199\",\n" +
            "        \"limitDate\": \"2016.01.25 - 2016.02.25\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"3\",\n" +
            "        \"money\": \"5\",\n" +
            "        \"limitMoney\": \"299\",\n" +
            "        \"limitDate\": \"2016.01.25 - 2016.02.25\"\n" +
            "    }\n" +
            "]";
}
