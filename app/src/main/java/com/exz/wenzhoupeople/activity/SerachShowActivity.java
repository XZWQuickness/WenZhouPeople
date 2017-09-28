package com.exz.wenzhoupeople.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.appclication.App;
import com.exz.wenzhoupeople.entity.SearchEntity;
import com.exz.wenzhoupeople.greendao.gen.SearchEntityDao;
import com.exz.wenzhoupeople.pop.SerachPop;
import com.exz.wenzhoupeople.utils.DialogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.config.Constants;
import cn.com.szw.lib.myframework.utils.Utils;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.JsonCallback;
import razerdp.basepopup.BasePopupWindow;

import static cn.com.szw.lib.myframework.app.MyApplication.salt;
import static com.exz.wenzhoupeople.config.Urls.HotSearchKey;

/**
 * Created by pc on 2017/9/14.
 */

public class SerachShowActivity extends BaseActivity {

    @BindView(R.id.mLeftImgSearch)
    ImageView mLeftImg;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.btSearch)
    TextView btSearch;
    @BindView(R.id.edSearch)
    EditText edSearch;
    @BindView(R.id.parent_lay)
    RelativeLayout parentLay;
    @BindView(R.id.toolbarSearch)
    Toolbar toolbar;
    @BindView(R.id.mHotTagFlow)
    TagFlowLayout mHotTagFlow;
    @BindView(R.id.historyLay)
    TagFlowLayout mHistoryTagFlow;
    @BindView(R.id.llDelete)
    LinearLayout llDelete;
    SerachPop mSerachPop;
    public static final String Intent_Search_Content = "content";
    SearchEntityDao dao;

    @Override
    public boolean initToolbar() {
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.VISIBLE);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_searach_show;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        edSearch.setTextSize(14f);
        edSearch.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
        if (getIntent().hasExtra(Intent_Search_Content)) {
            edSearch.setText(getIntent().getStringExtra(Intent_Search_Content));
            edSearch.setSelection(getIntent().getStringExtra(Intent_Search_Content).length());
            edSearch.requestFocus();
            KeyboardUtils.showSoftInput(edSearch);
        }
        dao = App.getApplication().getDaoSession().getSearchEntityDao();
        mSerachPop = new SerachPop(this);
        initHistoryTag(dao.loadAll());
        mSerachPop.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                type.setText(mSerachPop.getResult());
            }
        });
        initData();
    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("requestCheck", Utils.getMD5("HotSearchKey" + salt));
        OkGo.<NetEntity<List<String>>>post(HotSearchKey).params(map).tag(this).execute(new JsonCallback<NetEntity<List<String>>>() {
            @Override
            public void onSuccess(Response<NetEntity<List<String>>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                    initHotTag(response.body().getData());
                }
            }


        });
    }

    /**
     * @param list 初始化热门搜索tag列表
     */
    private void initHotTag(final List<String> list) {
        mHotTagFlow.setAdapter(new TagAdapter<String>(list) {
            @Override
            public View getView(FlowLayout parent, int position, final String content) {
                TextView textView = (TextView) View.inflate(mContext, R.layout.tag_classify, null);
                textView.setText(content);
                return textView;
            }

        });
        mHotTagFlow.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                SearchEntity searchEntity = new SearchEntity();
                searchEntity.setDate(new Date());
                searchEntity.setSearchContent(list.get(position));
                boolean isSkip = false;
                for (SearchEntity mSearchEntities : dao.loadAll()) {
                    if (mSearchEntities.getSearchContent().equals(list.get(position))) {
                        isSkip = true;
                        dao.update(searchEntity);
                        break;
                    }
                }
                if (!isSkip) {
                    dao.insert(searchEntity);
                }

                search(list.get(position));
                return false;
            }
        });


    }

    private void initHistoryTag(final List<SearchEntity> list) {
        if (list == null || list.size() == 0) {
            mHistoryTagFlow.setVisibility(View.GONE);
            llDelete.setVisibility(View.GONE);
            return;
        }
        mHistoryTagFlow.setAdapter(new TagAdapter<SearchEntity>(list) {
            @Override
            public View getView(FlowLayout parent, int position, SearchEntity searchEntity) {
                TextView textView = (TextView) View.inflate(mContext, R.layout.tag_classify, null);
                textView.setText(searchEntity.getSearchContent());
                return textView;
            }
        });
        mHistoryTagFlow.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                SearchEntity searchEntity = list.get(position);
                searchEntity.setDate(new Date());
                boolean isSkip = false;
                for (SearchEntity mSearchEntities : dao.loadAll()) {
                    if (mSearchEntities.getSearchContent().equals(list.get(position).getSearchContent())) {
                        isSkip = true;
                        dao.update(searchEntity);
                        break;
                    }
                }
                if (!isSkip) {
                    dao.insert(searchEntity);
                }

                search(searchEntity.getSearchContent());
                return false;
            }
        });
    }

    /**
     * @param content 搜索内容
     */
    private void search(String content) {
        Intent intent = new Intent(mContext, SeafoodListActivity.class);
        intent.putExtra(Intent_Search_Content, content);
        intent.putExtra("className", "");
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.mLeftImgSearch, R.id.btSearch, R.id.type, R.id.llDelete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLeftImgSearch:
                finish();
                break;
            case R.id.btSearch:
                String searchContent = edSearch.getText().toString().trim();
                if (!TextUtils.isEmpty(searchContent)) {
                    boolean isSkip = false;
                    for (SearchEntity mSearchEntities : dao.loadAll()) {
                        if (mSearchEntities.getSearchContent().equals(searchContent)) {
                            isSkip = true;
                            dao.update(mSearchEntities);
                            break;
                        }

                    }
                    if (!isSkip) {
                        dao.insert(new SearchEntity(searchContent, new Date()));
                    }
                    search(searchContent);

                }
                break;
            case R.id.type:
                mSerachPop.showPopupWindow(type);
                break;
            case R.id.llDelete:

                DialogUtils.DeleteSearch(mContext, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        49-51 53-55
                        DialogUtils.dialog.dismiss();
                        dao.deleteAll();
                        initHistoryTag(dao.loadAll());
        }
    });
                break;
        }
    }

}
