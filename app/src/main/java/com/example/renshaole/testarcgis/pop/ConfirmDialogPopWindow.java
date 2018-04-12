package com.example.renshaole.testarcgis.pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.renshaole.testarcgis.R;


/**
 * 内容：提示弹框
 * 作者：赵鹏帅
 * 时间：2017/4/26--10:08
 */

public class ConfirmDialogPopWindow extends PopupWindow {
    private final TextView tvContent;
    private Context context;
    private int state=0;
    TextView btnQuXiao;
    TextView btnQueDing;
   private String velar;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ConfirmDialogPopWindow(Context context, String velar) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.pop_confirm_dialog, null);
        // 设置SelectPicPopupWindow的Viewt
        this.context=context;
        this.velar=velar;
        this.setContentView(content);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.MyDialogActivityStyle);

        tvContent= (TextView) content.findViewById(R.id.tvContent);
         btnQuXiao= (TextView) content.findViewById(R.id.btnQuXiao);
         btnQueDing= (TextView) content.findViewById(R.id.btnQueDing);
         tvContent.setText(velar);
        //显示PopupWindow
        this.showAsDropDown(content,0,0, Gravity.CENTER);

    }

    public int getState() {
        return state;
    }

    // 设置菜单项点击监听器
    public void setBtnQueDingOnClickListener(View.OnClickListener listener) {
        // this.listener = listener;
        btnQueDing.setOnClickListener(listener);
    }
    // 设置菜单项点击监听器
    public void setBtnQuXiaoOnClickListener(View.OnClickListener listener) {
        // this.listener = listener;
        btnQuXiao.setOnClickListener(listener);
    }



}
