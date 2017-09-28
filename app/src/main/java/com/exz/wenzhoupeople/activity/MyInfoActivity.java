package com.exz.wenzhoupeople.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EncryptUtils;
import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.appclication.App;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.config.Constants;
import cn.com.szw.lib.myframework.imageloder.GlideImageLoader;
import cn.com.szw.lib.myframework.utils.DateUtils;
import cn.com.szw.lib.myframework.utils.Utils;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;

import static com.exz.wenzhoupeople.config.Urls.ModifyUserInfo;

/**
 * Created by weicao on 2017/8/28.
 * 个人资料
 */

public class MyInfoActivity extends BaseActivity {

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
    @BindView(R.id.icon_head)
    SimpleDraweeView iconHead;
    @BindView(R.id.layout_head)
    LinearLayout layoutHead;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.layout_name)
    LinearLayout layoutName;

    private ImagePicker imagePicker;



    @Override
    public boolean initToolbar() {
        mTitle.setText("个人资料");
        mTitle.setTextSize(18);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_my_info;
    }


    @Override
    public void init() throws Exception {
        super.init();
        initCamera();

        iconHead.setImageURI(getIntent().getStringExtra("url"));
        name.setText(getIntent().getStringExtra("name"));
    }


    private void initCamera() {
        imagePicker = ImagePicker.getInstance();
//        imagePicker.setMultiMode(true);////////
        imagePicker.setImageLoader(new GlideImageLoader());
        //显示相机
        imagePicker.setShowCamera(true);
        //是否裁剪
        imagePicker.setCrop(true);
        //是否按矩形区域保存裁剪图片
        imagePicker.setSaveRectangle(true);
        //圖片緩存
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setMultiMode(false);//单选
        //矩形尺寸
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
        imagePicker.setFocusWidth(width);
        imagePicker.setFocusHeight(width);
        //圖片輸出尺寸
//        imagePicker.setOutPutX(getResources().getDisplayMetrics().widthPixels);
//        imagePicker.setOutPutY((int) (getResources().getDisplayMetrics().heightPixels * 0.7));
        imagePicker.setOutPutX(width);
        imagePicker.setOutPutY(width);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) { //图片选择
            final ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
//            changInfo(images.get(0).path);
//            DraweeController controller = Fresco.newDraweeControllerBuilder()
//                    .setUri(Uri.fromFile(new File(images.get(0).path)))
//                    .setAutoPlayAnimations(true)
//                    .build();
//            iconHead.setController(controller);

            try {
                initPort(Utils.encodeBase64File(images.get(0).path),"");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (resultCode == 1) {
//            nickname = data.getStringExtra("name");
//            initPort();
            initPort("",data.getStringExtra("name"));
//            name.setText(data.getStringExtra("name"));
        }

    }

    @OnClick({R.id.mLeftImg, R.id.layout_head, R.id.layout_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLeftImg:
                finish();
                break;
            case R.id.layout_head:
                Intent intent1 = new Intent(MyInfoActivity.this, ImageGridActivity.class);
                startActivityForResult(intent1, 100);
                break;
            case R.id.layout_name:
                Intent intent = new Intent(mContext, ModifyNameActivity.class);
                intent.putExtra("name",getIntent().getStringExtra("name"));
                startActivityForResult(intent, 1);
                break;

        }
    }

    private void initPort(String heard, final String name1) {

        Map<String, String> map = new HashMap<>();
        map.put("id", App.getLoginUserId());
        map.put("timestamp", String.valueOf(DateUtils.dateToUnixTimestamp()));
        map.put("header", heard);
        map.put("name", name1);
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(App.getLoginUserId() + String.valueOf(DateUtils.dateToUnixTimestamp()), App.salt)
                .toLowerCase());
        OkGo.<NetEntity<String >>post(ModifyUserInfo).
                params(map).tag(this).execute(new DialogCallback<NetEntity<String>>(mContext) {
            @Override
            public void onSuccess(Response<NetEntity<String>> response) {
                if (response.body().getCode() == Constants.NetCode.SUCCESS) {

                    if (response.body().getData().equals("")){
                        App.getUserInfo().setName(name1);
                        name.setText(name1);
                    }else {
                        iconHead.setImageURI(response.body().getData());
                    }
                }
            }

        });
    }

}
