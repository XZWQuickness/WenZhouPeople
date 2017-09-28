package com.exz.wenzhoupeople.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.KeyboardUtils;
import com.common.controls.dialog.CommonDialogFactory;
import com.common.controls.dialog.DialogUtil;
import com.common.controls.dialog.ICommonDialog;
import com.exz.wenzhoupeople.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.szw.lib.myframework.listener.OnNumListener;

import static com.exz.wenzhoupeople.pop.GoodsDetailClassifyPop.currentCount;
import static com.exz.wenzhoupeople.pop.GoodsDetailClassifyPop.inventory;

/**
 * Created by pc on 2017/8/29.
 */

public class DialogUtils {
    public static ICommonDialog dialog;
    /**
     * 余额支付 没支付密码
     */
    public static void PayNoPwd(Activity context , final View.OnClickListener listener) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_103);
        dialog.setTitleText("啊哦");
        dialog.setContentText("未设置支付密码！");
        dialog.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setOkBtn("去设置", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                listener.onClick(view);
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
    /**
     * 支付返回
     */
    public static void payBack(final Activity context) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_103);
        dialog.setTitleText("返回");
        dialog.setContentText("您确定放弃支付?");
        dialog.setCancelBtn("支付", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setOkBtn("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                context.finish();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
    public static void DeleteSearch(final Context context, final View.OnClickListener listener) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_103);
        dialog.setTitleText("删除");
        dialog.setContentText("确定清除记录？");
        dialog.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOkBtn("确定",listener);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public static void ChangeNum(final Context context, int count, final OnNumListener onNumListener) {

        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_104);
        LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.dialog_change_num, null);
        final ViewHolder v = new ViewHolder(view);
        dialog.setContentView(view);
        dialog.setTitleText("修改购买数量");
        v.count.setText(currentCount + "");
        v.count.setText(String.format("%s", count));
        v.count.setSelection(v.count.getText().length());
        //设置可获得焦点
        v.count.setFocusable(true);
        v.count.setFocusableInTouchMode(true);
        //请求获得焦点
        v.count.requestFocus();
        v.count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("0")) {
                    v.count.setText("1");
                    currentCount = Integer.parseInt(charSequence.toString());
                } else if (!TextUtils.isEmpty(charSequence.toString()) && Integer.parseInt(charSequence.toString()) > inventory) {
                    v.count.setText(inventory + "");
                    currentCount = Integer.parseInt(charSequence.toString());
                } else {

                }
                v.count.setSelection(v.count.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.setOkBtn("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trim = v.count.getText().toString().trim();
                if (!TextUtils.isEmpty(trim) && !trim.equals("0")) {
                    onNumListener.onNum(Integer.parseInt(trim));
                }

                dialog.dismiss();
            }
        });
        dialog.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                KeyboardUtils.toggleSoftInput();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                KeyboardUtils.toggleSoftInput();
            }
        });
        dialog.show();
    }

    public static void showDiaolog(final Context context, String title, View.OnClickListener ok) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_104);
        dialog.setTitleText(title);
        dialog.setOkBtn("确定", ok);
        dialog.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    static class ViewHolder {
        @BindView(R.id.minus)
        ImageView minus;
        @BindView(R.id.count)
        EditText count;
        @BindView(R.id.add)
        ImageView add;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.minus, R.id.add})
        public void onViewClicked(View view) {
            currentCount = Integer.parseInt(count.getText().toString().trim());
            switch (view.getId()) {
                case R.id.minus:
                    currentCount = currentCount <= 1 ? 1 : --currentCount;
                    break;
                case R.id.add:
                    currentCount += 1;
                    if (currentCount > inventory) {
                        currentCount = inventory;
                    }
                    break;
            }
            count.setText(String.format("%s", currentCount));
            count.setSelection(count.getText().length());
        }
    }
}
