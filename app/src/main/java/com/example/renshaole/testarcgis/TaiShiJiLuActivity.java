package com.example.renshaole.testarcgis;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.example.renshaole.testarcgis.bean.PictureBean;
import com.example.renshaole.testarcgis.bean.QoistionBean;
import com.example.renshaole.testarcgis.helper.DatabaseOperation;
import com.example.renshaole.testarcgis.utils.FileUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 态势记录表
 */
public class TaiShiJiLuActivity extends AdaptationActivity {
    Polyline polygon;
    GraphicsLayer taishiLayer;
    private MapView mapView;
    PictureMarkerSymbol currentLocationSymbol;
    DatabaseOperation databaseOperation;
    Point wgspoint;
    Point mapPoint;
    Graphic graphics;
    GraphicsLayer gCurrentLayerPos;
    private long startTime;
    private long endTime;
    int aaaa = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_shi_ji_lu);
        databaseOperation =new DatabaseOperation(this);
        init();
        startTaiShi();
    }

    private void init() {
        mapView = this.findViewById(R.id.map);
        String strMapUrl = "http://cache1.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer";
        ArcGISTiledMapServiceLayer arcGISTiledMapServiceLayer = new ArcGISTiledMapServiceLayer(strMapUrl);
        this.mapView.addLayer(arcGISTiledMapServiceLayer);
        ArcGISRuntime.setClientId("1eFHW78avlnRUPHm");
        taishiLayer = new GraphicsLayer();
        mapView.addLayer(taishiLayer);

        gCurrentLayerPos = new GraphicsLayer();
        mapView.addLayer(gCurrentLayerPos);
        currentLocationSymbol = new PictureMarkerSymbol(this.getResources().getDrawable(R.drawable.icon_openmap_mark));
    }


    private void startTaiShi(){
        // 假如回放时间从2018-04-24 12:03:00 到2018-04-24 12:11:00
        // 先取出这个时间段的位置数据 根据路线方案取出
        final List<QoistionBean> list = databaseOperation.querySetTimePoistion("2018-04-24 18:38:43","2018-04-24 19:02:43");
        try {
            startTime = Long.parseLong(dateToStamp("2018-04-24 18:38:43"));
            endTime = Long.parseLong(dateToStamp("2018-04-24 19:02:43"));
            final long time= endTime-startTime;
            polygon = new Polyline();
            taishiLayer.removeAll();
            mapView.setScale(500000);   //设置初始比例为5公里s
            new CountDownTimer(time, 10) {
                //当前任务每完成一次倒计时间隔时间时回调
                public void onTick(long millisUntilFinished) {
                    String  times= convertDate2(startTime+=1000);
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getPoistionTime().equals(times)) {
                            gCurrentLayerPos.removeAll();
                            wgspoint = new Point(Double.parseDouble(list.get(i).getPoistion_x()), Double.parseDouble(list.get(i).getPoistion_y()));
                            mapPoint = (Point) GeometryEngine.project(wgspoint, SpatialReference.create(4326), mapView.getSpatialReference());
                            if (aaaa == 0) {
                                polygon.startPath(new Point(mapPoint.getX(), mapPoint.getY()));
                            } else {
                                polygon.lineTo(new Point(mapPoint.getX(), mapPoint.getY()));
                            }
                            aaaa++;
                            graphics = new Graphic(polygon, new SimpleLineSymbol(Color.RED, 2));
                            taishiLayer.addGraphic(graphics);
                            Graphic graphic = new Graphic(mapPoint, currentLocationSymbol);
                            gCurrentLayerPos.addGraphic(graphic);
                        }
                    }
                }
                //完成是回调
                public void onFinish() {
                }
            }.start();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }




    /**
     * 根据日期转毫秒
     * @param s
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }
    /**
     * 根据毫秒转换日期
     * @param time
     */
    public static String convertDate2(Long time){
        Date d = new Date(time);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = sdf.format(d);
        return s;
    }

}
