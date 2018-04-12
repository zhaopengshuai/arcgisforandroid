package com.example.renshaole.testarcgis.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.renshaole.testarcgis.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zyn on 2017-12-08.
 */

public class CustomDialog extends Dialog {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.now_time)
    TextView nowTime;
    @BindView(R.id.your_time)
    TextView yourTime;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.submit)
    TextView submit;

    Context mContext;
    String now_time;
    String time;

    private OnCloseListener listener;


    public CustomDialog(@NonNull Context context) {
        super(context);
    }
    public CustomDialog(Context context, int themeResId, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = listener;
    }

    public CustomDialog setNowtime(String now_time) {
        this.now_time = now_time;
        return this;
    }

    public CustomDialog setTime(String time) {
        this.time = time;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom);
        setCanceledOnTouchOutside(false);

        if(!TextUtils.isEmpty(now_time)){
            nowTime.setText(now_time);
        }

        if(!TextUtils.isEmpty(time)){
            yourTime.setText(time);
        }

    }

    @OnClick({R.id.cancel, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                if(listener != null){
                    listener.onClick(this, false);
                }
                this.dismiss();
                break;
            case R.id.submit:
                if(listener != null){
                    listener.onClick(this, true);
                }
                break;
        }
    }


    public interface OnCloseListener{
        void onClick(Dialog dialog, boolean confirm);
    }

}
