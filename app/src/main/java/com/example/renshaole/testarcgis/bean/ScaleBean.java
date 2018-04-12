package com.example.renshaole.testarcgis.bean;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.esri.android.map.MapView;
import com.example.renshaole.testarcgis.utils.BeanUtil;
import com.example.renshaole.testarcgis.utils.SPUtils;

/**
 * Created by admin on 2018/4/4.
 */

public class ScaleBean {
    public static  void refreshScaleView(Context context, TextView tvScale, MapView mapView) {
        if(mapView == null){
            throw new NullPointerException("you can call setMapView(MapView mapView) at first");
        }
        long  scale= (long) SPUtils.get(context,"scale",(long)-1);
        if (scale==-1) {
            double scales=mapView.getScale()/100;//结果单位米，表示图上1厘米代表*米
            SPUtils.put(context,"scale",(long)scales);
            scale= (long) scales;
        }

//        double ppi=getPPIOfDevice();
        if(scale>0&&scale<=20){//换算20米
            String unit = "20米";
            int scaleWidth=(int)(20*264/2.54/scale);//264为ppi，264/2.54为1厘米的像素数
            tvScale.setText(unit);//更新文字
        }else if(scale>20&&scale<=50){//换算50米
            String unit = "50米";
            int scaleWidth=(int)(50*264/2.54/scale);
            tvScale.setText(unit);//更新文字
        }else if(scale>50&&scale<=100){//换算20米
            String unit = "100米";
            int scaleWidth=(int)(100*264/2.54/scale);
            tvScale.setText(unit);//更新文字
        }else if(scale>100&&scale<=200){//换算20米
            String unit = "200米";
            int scaleWidth=(int)(200*264/2.54/scale);
            tvScale.setText(unit);//更新文字
        }else if(scale>200&&scale<=500){//换算20米
            String unit = "500米";
            int scaleWidth=(int)(500*264/2.54/scale);
            tvScale.setText(unit);//更新文字
        }else if(scale>500&&scale<=1000){//换算20米
            String unit = "1公里";
            int scaleWidth=(int)(1000*264/2.54/scale);
            tvScale.setText(unit);//更新文字
        }else if(scale>1000&&scale<=2000){//换算20米
            String unit = "2公里";
            int scaleWidth=(int)(2000*264/2.54/scale);
            tvScale.setText(unit);//更新文字
        }else if(scale>2000&&scale<=5000){//换算20米
            String unit = "5公里";
            int scaleWidth=(int)(5000*264/2.54/scale);
            tvScale.setText(unit);//更新文字
        }else if(scale>5000&&scale<=10000){//换算20米
            String unit = "10公里";
            int scaleWidth=(int)(10000*264/2.54/scale);
            tvScale.setText(unit);//更新文字
        }else if(scale>10000&&scale<=20000){//换算20米
            String unit = "20公里";
            int scaleWidth=(int)(20000*264/2.54/scale);
            tvScale.setText(unit);//更新文字
        }else if(scale>20000&&scale<=25000){//换算20米
            String unit = "25公里";
            int scaleWidth=(int)(25000*264/2.54/scale);
            tvScale.setText(unit);//更新文字
        }else if(scale>25000&&scale<=50000){//换算20米
            String unit = "50公里";
            int scaleWidth=(int)(50000*264/2.54/scale);
            tvScale.setText(unit);//更新文字
        }else if(scale>50000&&scale<=100000){//换算20米
            String unit = "100公里";
            int scaleWidth=(int)(100000*264/2.54/scale);
            tvScale.setText(unit);//更新文字
        }else if(scale>100000&&scale<=200000){//换算20米
            String unit = "200公里";
            int scaleWidth=(int)(200000*264/2.54/scale);
            tvScale.setText(unit);//更新文字
        }else if(scale>200000&&scale<=250000){//换算20米
            String unit = "250公里";
            int scaleWidth=(int)(250000*264/2.54/scale);
            tvScale.setText(unit);//更新文字
        }else if(scale>250000&&scale<=500000){//换算20米
            String unit = "500公里";
            int scaleWidth=(int)(500000*264/2.54/scale);
            tvScale.setText(unit);//更新文字
        }else if(scale>500000&&scale<=scale){//换算20米
            String unit = "1000公里";
            int scaleWidth=(int)(1000000*264/2.54/scale);
            tvScale.setText(unit);//更新文字
        }

    }
}
