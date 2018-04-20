package com.example.renshaole.testarcgis.pop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.renshaole.testarcgis.R;


/**
 * 内容：提示弹框
 * 作者：赵鹏帅
 * 时间：2017/4/26--10:08
 */

public class AddPositionPopWindow extends PopupWindow {
    private  TextView tvClose;
    private  TextView btnQueDing;
    private Context context;
    EditText edtY;
    EditText edtX;
   private String velar;

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public AddPositionPopWindow(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.pop_add_position, null);
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

        tvClose=  content.findViewById(R.id.tvClose);
        btnQueDing=  content.findViewById(R.id.btnQueDing);
        edtY=  content.findViewById(R.id.edtY);
        edtX= content.findViewById(R.id.edtX);
        //显示PopupWindow
        this.showAsDropDown(content,0,0, Gravity.CENTER);

    }

    public String  getEdtY(){
        return edtY.getText().toString();
    }
    public String  getEdtX(){
        return edtX.getText().toString();
    }

    // 设置菜单项点击监听器
    public void setBtnQueDingOnClickListener(View.OnClickListener listener) {
        // this.listener = listener;
        btnQueDing.setOnClickListener(listener);
    }
    // 设置菜单项点击监听器
    public void setBtnQuXiaoOnClickListener(View.OnClickListener listener) {
        // this.listener = listener;
        tvClose.setOnClickListener(listener);
    }



}
