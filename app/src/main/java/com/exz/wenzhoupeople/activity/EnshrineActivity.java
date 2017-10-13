package com.exz.wenzhoupeople.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.adapter.SelectQuestionListsAdapter;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.entity.EnshrineModel;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.config.Constants;
import cn.com.szw.lib.myframework.utils.DateUtils;
import cn.com.szw.lib.myframework.utils.RecycleViewDivider;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;
import cn.com.szw.lib.myframework.view.CustomLoadMoreView;

import static com.exz.wenzhoupeople.config.Urls.CollectAction;
import static com.exz.wenzhoupeople.config.Urls.CollectList;

/**
 * Created by weicao on 2017/8/31.、
 * 收藏的商品
 */

public class EnshrineActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.mLeft)
    TextView mLeft;
    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.mRight)
    TextView mRight;
    @BindView(R.id.mRightImg)
    ImageView mRightImg;
    @BindView(R.id.mLeftImg)
    ImageView mLeftImg;
    @BindView(R.id.parent_lay)
    RelativeLayout parentLay;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text0)
    TextView text0;
    @BindView(R.id.view0)
    View view0;
    @BindView(R.id.layout0)
    LinearLayout layout0;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.layout1)
    LinearLayout layout1;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.layout2)
    LinearLayout layout2;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    private String state = "0";


    private SelectQuestionListsAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;

    private int page = 1;
    public int refreshState = Constants.RefreshState.STATE_REFRESH;
    private SelectQuestionListsAdapter adapter;


    @Override
    public boolean initToolbar() {
        mTitle.setText("收藏的商品");
        mTitle.setTextSize(18);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_enshrine;
    }

    @Override
    public void init() throws Exception {
        super.init();
        initView();
        initPort();
    }


    private void initView() {


//        list.add(new EnshrineModel());
//        list.add(new EnshrineModel());
//        list.add(new EnshrineModel());
//        list.add(new EnshrineModel());
//        list.add(new EnshrineModel());

        adapter = new SelectQuestionListsAdapter();
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, 10, ContextCompat.getColor(mContext, R.color.app_bg)));
        recyclerView.setAdapter(adapter);
        recyclerView.setClipToPadding(false);
        recyclerView.setPadding(SizeUtils.dp2px(0), SizeUtils.dp2px(10), SizeUtils.dp2px(0), SizeUtils.dp2px(0));
        adapter.setOnLoadMoreListener(this, recyclerView);
        refresh.setOnRefreshListener(this);
        adapter.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.view_empty, new RelativeLayout(mContext), false));
        adapter.setOnDelListener(new SelectQuestionListsAdapter.onSwipeListener() {
            @Override
            public void onDel(final int pos) {
                cancel(adapter.getData().get(pos).getId(),pos);
            }


        });

        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.layout:
                        Intent intent = new Intent(mContext,GoodsDetailActivity.class);
                        intent.putExtra("goodsId",((EnshrineModel)adapter.getData().get(position)).getId());
                        startActivity(intent);
                        break;
                }
            }
        });

    }
    public void cancel(String goodsId,final int pos){
        Map<String, String> map = new HashMap<>();
        map.put("id", App.getLoginUserId());
        map.put("goodsId", goodsId);
        map.put("type", "0");
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() + goodsId, App.salt)
                .toLowerCase());
        OkGo.<NetEntity<String>>post(CollectAction).params(map).tag(this).execute(new DialogCallback<NetEntity<String>>(mContext) {
            @Override
            public void onSuccess(Response<NetEntity<String>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    if (pos >= 0 && pos < adapter.getData().size()) {
//                    initDelete(dataList.get(pos).getHomeworkPaperId());


                        adapter.getData().remove(pos);
                        adapter.notifyItemRemoved(pos);//推荐用这个
                        //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                        //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
//                    mAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @OnClick({R.id.mLeftImg, R.id.layout0, R.id.layout1, R.id.layout2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLeftImg:
                finish();
                break;
            case R.id.layout0:
                text0.setTextColor(getResources().getColor(R.color.blue2));
                view0.setBackgroundColor(getResources().getColor(R.color.blue2));
                text1.setTextColor(getResources().getColor(R.color.text_gray));
                view1.setBackgroundColor(getResources().getColor(R.color.white));
                text2.setTextColor(getResources().getColor(R.color.text_gray));
                view2.setBackgroundColor(getResources().getColor(R.color.white));
                adapter.setI(0);
                state = "0";
                onRefresh();
                break;
            case R.id.layout1:
                text0.setTextColor(getResources().getColor(R.color.text_gray));
                view0.setBackgroundColor(getResources().getColor(R.color.white));
                text1.setTextColor(getResources().getColor(R.color.blue2));
                view1.setBackgroundColor(getResources().getColor(R.color.blue2));
                text2.setTextColor(getResources().getColor(R.color.text_gray));
                view2.setBackgroundColor(getResources().getColor(R.color.white));
                adapter.setI(1);
                state = "1";
                onRefresh();
                break;
            case R.id.layout2:
                text0.setTextColor(getResources().getColor(R.color.text_gray));
                view0.setBackgroundColor(getResources().getColor(R.color.white));
                text1.setTextColor(getResources().getColor(R.color.text_gray));
                view1.setBackgroundColor(getResources().getColor(R.color.white));
                text2.setTextColor(getResources().getColor(R.color.blue2));
                view2.setBackgroundColor(getResources().getColor(R.color.blue2));
                adapter.setI(2);
                state = "2";
                onRefresh();
                break;
        }
    }

    @Override
    public void onRefresh() {
        refresh.setRefreshing(false);
        page = 1;
        refreshState = Constants.RefreshState.STATE_REFRESH;
        initPort();

    }

    @Override
    public void onLoadMoreRequested() {
        refresh.setEnabled(false);
        refreshState = Constants.RefreshState.STATE_LOAD_MORE;
        initPort();
    }


    private void initPort() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", App.getLoginUserId());
        params.put("timestamp", String.valueOf(DateUtils.dateToUnixTimestamp()));
        params.put("state", state);
        params.put("page", String.valueOf(page));
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() + String.valueOf(DateUtils.dateToUnixTimestamp()) + App.salt).toLowerCase());
        OkGo.<NetEntity<List<EnshrineModel>>>post(CollectList)
                .params(params)
                .tag(this)
                .execute(new DialogCallback<NetEntity<List<EnshrineModel>>>(mContext) {

                    @Override
                    public void onSuccess(Response<NetEntity<List<EnshrineModel>>> response) {
                        refresh.setEnabled(true);
                        refresh.setRefreshing(false);

                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            if (refreshState == Constants.RefreshState.STATE_REFRESH) {
                                if (state.equals("0") &&response.body().getData().size()!= 0) {
                                    text0.setText(String.format("全部商品(%s)", response.body().getData().size()));
                                }
                                adapter.setNewData(response.body().getData());

                            } else {
                                adapter.addData(response.body().getData());
                            }

                            if (response.body().getData().isEmpty()) {
                                adapter.loadMoreComplete();
                                page++;
                            } else {
                                adapter.loadMoreEnd();
                            }
                        }

                    }

                    @Override
                    public void onError(Response<NetEntity<List<EnshrineModel>>> response) {
                        super.onError(response);

                        refresh.setEnabled(true);
                        refresh.setRefreshing(false);
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        adapter.loadMoreFail();
                    }
                });
    }


}
