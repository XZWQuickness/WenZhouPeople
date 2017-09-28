package com.exz.wenzhoupeople.pop;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.blankj.utilcode.util.ScreenUtils;
import com.exz.wenzhoupeople.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by pc on 2017/7/11.
 */

public class SerachPop extends BasePopupWindow {
    String result = "";
public static String type="0";
    public SerachPop(Activity context) {
        super(context);
    }

    @Override
    protected Animation initShowAnimation() {
        Animation shakeAnimate = new TranslateAnimation(0, 0, (float) (-ScreenUtils.getScreenHeight() * 0.8),
                0);
        shakeAnimate.setDuration(400);
        return shakeAnimate;
    }

    @Override
    protected Animation initExitAnimation() {
        Animation shakeAnimate = new TranslateAnimation(0, -ScreenUtils.getScreenWidth(), 0, 0);
        shakeAnimate.setDuration(400);
        return shakeAnimate;
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        View inflate = View.inflate(getContext(), R.layout.pop_serach, null);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.llLay);
    }

    //infoType：1求租信息 2出租信息 3出售信息 4求购信息 5招聘信息 6求职信息
    @OnClick({R.id.dp, R.id.lb})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dp:
                result = "单品";
                type="0";
                dismiss();
                break;
            case R.id.lb:
                result = "礼包";
                type="1";
                dismiss();
                break;
        }
    }


    public String getResult() {
        return result;
    }
}
