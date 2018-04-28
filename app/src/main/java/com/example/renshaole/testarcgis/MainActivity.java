package com.example.renshaole.testarcgis;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.math.BigDecimal;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnZoomListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.example.renshaole.testarcgis.bean.MarkCorerBean;
import com.example.renshaole.testarcgis.bean.PictureBean;
import com.example.renshaole.testarcgis.bean.QoistionBean;
import com.example.renshaole.testarcgis.bean.RouteNewsBean;
import com.example.renshaole.testarcgis.bean.ScaleBean;
import com.example.renshaole.testarcgis.bean.SituationBean;
import com.example.renshaole.testarcgis.bean.StaffPositionBean;
import com.example.renshaole.testarcgis.decoding.DecodingLibrary;
import com.example.renshaole.testarcgis.helper.DatabaseHelper;
import com.example.renshaole.testarcgis.helper.DatabaseOperation;
import com.example.renshaole.testarcgis.location.GPSLocationListener;
import com.example.renshaole.testarcgis.location.GPSLocationManager;
import com.example.renshaole.testarcgis.location.GPSProviderStatus;
import com.example.renshaole.testarcgis.pop.ConfirmDialogPopWindow;
import com.example.renshaole.testarcgis.utils.BeanUtil;
import com.example.renshaole.testarcgis.utils.FileUtil;
import com.example.renshaole.testarcgis.utils.MapScaleView;
import com.example.renshaole.testarcgis.utils.SPUtils;
import com.example.renshaole.testarcgis.utils.util;
import com.example.renshaole.testarcgis.widget.Entity;
import com.example.renshaole.testarcgis.widget.MyMessage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static com.example.renshaole.testarcgis.utils.util.CORER_ZHUANGZHAI;


public class MainActivity extends AdaptationActivity implements DrawEventListener {

    Map<String, StaffPositionBean> locationMap = new HashMap<>();
    Polyline polygon;
    Graphic graphics;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CALL_CAMERA = 2;
    String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    List<String> mPermissionList = new ArrayList<>();
    private String network = "gps";
    private MapView mapView = null;
    private GraphicsLayer drawLayer;
    private DrawTool drawTool;
    ImageView GPS_btn;
    //一个checkbox来设置 视角是否跟随
    CheckBox chkbox_isGuiJi;
    CheckBox chk_isfollow;
    CheckBox chkbox_isLookGuiJi;
    Point point;
    Point wgspoint;
    MapScaleView tvScale;
    TextView tvX;
    TextView tvY;
    TextView tvRouteNews;
    ImageView iv_connect;
    GraphicsLayer gLayerPos;
    GraphicsLayer markCover;
    GraphicsLayer gCurrentLayerPos;
    GraphicsLayer line;
    GraphicsLayer taishiLayer;
    PictureMarkerSymbol locationSymbol;
    PictureMarkerSymbol currentLocationSymbol;
    LocationManager locMag;
    Location loc;
    Point mapPoint;

    TextView txtview;
    ZoomControls zoomCtrl;
    private MarkerSymbol markerSymbol;
    private FillSymbol fillSymbol;
    private Envelope envelope;
    private Timer timer = new Timer();
    private LocationListener locationListener;
    private Handler handler;
    private int pointCount = 0;
    private int pointCount1 = 0;
    LocationDisplayManager locationDisplayManager;
    DatabaseOperation databaseOperation;
    double x = 0;
    double y = 0;
    boolean isSelected = true;
    private App app;
    private ConfirmDialogPopWindow confirmDialogPopWindow;
    private PictureMarkerSymbol pictureMarkerSymbol;
    private List<MarkCorerBean> markCorerBeanList;
    private CheckBox chkbox_Corer;
    private GPSLocationManager gpsLocationManager;
    private List<PictureBean>  list;
    private List<MarkCorerBean>  listMarkCorerBean;
    private long startTime;
    private long endTime;
    int aaaa = 0;
    int state = 0;
    private TextView tvTaiShi;
    private CountDownTimer mCountDownTimer;
    private long sTime;
    private long aTime;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint({"MissingPermission", "HandlerLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (App) getApplication();
        mPermissionList.clear();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0x11) {
//                    locMag.requestLocationUpdates(network, 2000, 10, locationListener);
                    Bundle bundle = msg.getData();
                    String position = bundle.getString("position");
                    String time = bundle.getString("time");
                    if (BeanUtil.isEmpty(confirmDialogPopWindow)) {
                        confirmDialogPopWindow = new ConfirmDialogPopWindow(MainActivity.this, position);
                        databaseOperation.addRouteNews(position, time,"1");
                    } else {
                        confirmDialogPopWindow.dismiss();
                        confirmDialogPopWindow = new ConfirmDialogPopWindow(MainActivity.this, position);
                        databaseOperation.addRouteNews(position, time,"1");
                    }
                    confirmDialogPopWindow.setBtnQuXiaoOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            confirmDialogPopWindow.dismiss();
                        }
                    });
                    confirmDialogPopWindow.setBtnQueDingOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            confirmDialogPopWindow.dismiss();
                        }
                    });

                }
            }
        };
        app.setHandler(handler);
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }

        databaseOperation = new DatabaseOperation(this);
        gpsLocationManager = GPSLocationManager.getInstances(MainActivity.this);
        iv_connect = this.findViewById(R.id.iv_connnect);
        tvScale = findViewById(R.id.tvScale);
        tvX = findViewById(R.id.tvX);
        tvY = findViewById(R.id.tvY);
        tvTaiShi = findViewById(R.id.tvTaiShi);
        tvRouteNews = findViewById(R.id.tvRouteNews);
        chk_isfollow = this.findViewById(R.id.chkbox_isFollow);
        chkbox_Corer = this.findViewById(R.id.chkbox_Corer);
        chkbox_isGuiJi = this.findViewById(R.id.chkbox_isGuiJi);
        chkbox_isLookGuiJi = this.findViewById(R.id.chkbox_isLookGuiJi);
        GPS_btn = findViewById(R.id.GPS_btn);
        mapView = this.findViewById(R.id.map);
        tvScale.setMapView(mapView);
//        MarkCorerBean markCorerBean =new MarkCorerBean();
//        markCorerBean.setStart("1");
//        markCorerBean.setType("10");
//        markCorerBean.setPoistion_x(114.46469);
//        markCorerBean.setPoistion_y(38.0075);
//        databaseOperation.addmarkCorer(markCorerBean);
        tvRouteNews.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RouteNewsActivity.class);
                startActivity(intent);
            }
        });
        // 跳转到设置界面
        iv_connect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),
                        SettingsActivity.class));
            }
        });
        //在线地图
        //String strMapUrl="http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer";
        String strMapUrl = "http://cache1.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer";
        ArcGISTiledMapServiceLayer arcGISTiledMapServiceLayer = new ArcGISTiledMapServiceLayer(strMapUrl);
        this.mapView.addLayer(arcGISTiledMapServiceLayer);
        ArcGISRuntime.setClientId("1eFHW78avlnRUPHm");
        //未授予的权限为空，表示都授予了
        if (mPermissionList.isEmpty()) {
            //离线地图
//            String theOfflineTiledLayers = getSDPath() + "/DCIM/test.tpk";
//            ArcGISLocalTiledLayer tLayer = new ArcGISLocalTiledLayer(theOfflineTiledLayers);
//            this.mapView.addLayer(tLayer);
//            this.mapView.addLayer(new GraphicsLayer());
            locationDisplayManager = mapView.getLocationDisplayManager();//获取定位类
            locationDisplayManager.setShowLocation(true);
            locationDisplayManager.setAccuracyCircleOn(false);       //是否要圈
            locationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);//设置模式
            locationDisplayManager.setShowPings(true);
            try {
                locationDisplayManager.setAccuracySymbol(new SimpleFillSymbol(Color.GREEN).setAlpha(20));
                locationDisplayManager.setDefaultSymbol(new PictureMarkerSymbol(this.getResources().getDrawable(R.drawable.icon_openmap_mark)));
                locationDisplayManager.setLocationAcquiringSymbol(new PictureMarkerSymbol(this.getResources().getDrawable(R.drawable.icon_openmap_mark)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            locationDisplayManager.start();//开始定位
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, permissions, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }

        //定时刷新
        timer.schedule(timerTask, 2000, 2000);
        this.drawLayer = new GraphicsLayer();
        this.mapView.addLayer(this.drawLayer);
        this.drawTool = new DrawTool(this.mapView);
        this.drawTool.addEventListener(this);
        /*zoomCtrl=(ZoomControls)findViewById(R.id.zoomCtrl);
        zoomCtrl.setOnZoomInClickListener(new OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mapView.zoomin();
            }
        });
        zoomCtrl.setOnZoomOutClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                mapView.zoomout();
            }
        });*/

        gLayerPos = new GraphicsLayer();
        mapView.addLayer(gLayerPos);
        markCover = new GraphicsLayer();
        mapView.addLayer(markCover);

        gCurrentLayerPos = new GraphicsLayer();
        mapView.addLayer(gCurrentLayerPos);
        line = new GraphicsLayer();
        mapView.addLayer(line);
        taishiLayer = new GraphicsLayer();
        mapView.addLayer(taishiLayer);
        locationSymbol = new PictureMarkerSymbol(this.getResources().getDrawable(R.drawable.location));
        currentLocationSymbol = new PictureMarkerSymbol(this.getResources().getDrawable(R.drawable.icon_openmap_mark));
        locMag = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        GPS_btn.setOnClickListener(new OnClickListener() {
            @SuppressLint("MissingPermission")
            public void onClick(View v) {
                // TODO Auto-generate0 id method stub
                locationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);//设置模式
                locationDisplayManager.start();
            }

        });


        //第一个点startPath,后面的点用lineTo
        polygon = new Polyline();
        //定位回调监听
        locationDisplayManager.setLocationListener(new LocationListener() {
            DecimalFormat df = new DecimalFormat("###.000000");

            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationChanged(Location location) {
                double lon = location.getLongitude();
                double lat = location.getLatitude();

                Log.d("AAA", "Lon:" + df.format(lon) + " , " + "Lat:" + df.format(lat));
                wgspoint = new Point(lon, lat);
                tvX.setText("纬度：" +   new DecimalFormat("#.000000").format(wgspoint.getX()));
                tvY.setText("经度：" + new DecimalFormat("#.000000").format(wgspoint.getY()));
                String locStr = getCurrentLocation(location);
                MyMessage.MSG_ENTITYLOCXY = locStr;
//                dataHelper.addTask(wgspoint.getX(),wgspoint.getY());
//                dataHelper.GetUserList();
                mapPoint = (Point) GeometryEngine.project(wgspoint, SpatialReference.create(4326), mapView.getSpatialReference());

                //判断是否开启轨迹
                if (chkbox_isGuiJi.isChecked()) {
                    if (pointCount == 0) {
                        polygon.startPath(new Point(mapPoint.getX(), mapPoint.getY()));
                        databaseOperation.addPoistion(wgspoint.getX(), wgspoint.getY(),"1",getNowDate());    //保存经纬度
                        x = wgspoint.getX();
                        y = wgspoint.getY();
                        pointCount++;
                    } else {
                        if (chkbox_isLookGuiJi.isChecked()) {
                            if (isSelected) {
                                polygon.lineTo(new Point(mapPoint.getX(), mapPoint.getY()));
                            }
                            if (x == wgspoint.getX() || y == wgspoint.getY()) {
                            } else {
                                databaseOperation.addPoistion(wgspoint.getX(), wgspoint.getY(),"1",getNowDate());    //保存经纬度
                            }
                            x = wgspoint.getX();
                            y = wgspoint.getY();
                        } else {
                            if (x == wgspoint.getX() || y == wgspoint.getY()) {
                            } else {
                                databaseOperation.addPoistion(wgspoint.getX(), wgspoint.getY(),"1",getNowDate());    //保存经纬度
                            }
                            x = wgspoint.getX();
                            y = wgspoint.getY();
                        }

                    }
                    if (pointCount == 0) {
                        long scale = (long) SPUtils.get(MainActivity.this, "scale", (long) -1);
                        if (scale == -1) {
                            mapView.setScale(500000);   //设置初始比例为5公里
                        } else {
                            mapView.setScale(scale * 100);
                        }
//                        ScaleBean.refreshScaleView(MainActivity.this, tvScale, mapView);
                        tvScale.refreshScaleView();
                    }

                    if (!chkbox_isLookGuiJi.isChecked()) {

                    } else {
                        graphics = new Graphic(polygon, new SimpleLineSymbol(Color.RED, 2));
                        line.addGraphic(graphics);
                    }
                    pointCount++;
                }else {
                    if (pointCount1 == 0) {
                        long scale = (long) SPUtils.get(MainActivity.this, "scale", (long) -1);
                        if (scale == -1) {
                            mapView.setScale(500000);   //设置初始比例为5公里
                        } else {
                            mapView.setScale(scale * 100);
                        }
//                        ScaleBean.refreshScaleView(MainActivity.this, tvScale, mapView);
                        tvScale.refreshScaleView();
                    }
                    pointCount1++;
                }
                //判断是否为跟随模式
                if (chk_isfollow.isChecked()) {
                    mapView.centerAt(mapPoint, true);
                }


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override

            public void onProviderDisabled(String provider) {
            }
        });
        //通过设置checkbox的监听事件，判断checkbox是否被选中
        chkbox_isLookGuiJi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isSelected = false;
                    polygon = new Polyline();
                    line.removeAll();
                    int a = 0;
                    List<QoistionBean> list = databaseOperation.queryPoistion();
                    for (int i = 0; i < list.size(); i++) {
                        wgspoint = new Point(Double.parseDouble(list.get(i).getPoistion_x()), Double.parseDouble(list.get(i).getPoistion_y()));
                        mapPoint = (Point) GeometryEngine.project(wgspoint, SpatialReference.create(4326), mapView.getSpatialReference());
                        if (a == 0) {
                            polygon.startPath(new Point(mapPoint.getX(), mapPoint.getY()));
                        } else {
                            polygon.lineTo(new Point(mapPoint.getX(), mapPoint.getY()));
                        }
                        a++;
                        graphics = new Graphic(polygon, new SimpleLineSymbol(Color.RED, 2));
                        line.addGraphic(graphics);
                    }
                } else {
                    isSelected = true;
                    line.removeAll();
                }
            }
        });
        chkbox_Corer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    markCover();
                } else {
                    markCover.removeAll();
                }
            }
        });
        /**
         * 获取比例尺
         */
        mapView.setOnZoomListener(new OnZoomListener() {
            @Override
            public void preAction(float v, float v1, double v2) {
                //todo
            }

            @Override
            public void postAction(float v, float v1, double v2) {
                double scales = mapView.getScale() / 100;//结果单位米，表示图上1厘米代表*米
                SPUtils.put(MainActivity.this, "scale", (long) scales);
//                ScaleBean.refreshScaleView(MainActivity.this, tvScale, mapView);
                tvScale.refreshScaleView();
            }
        });
        tvTaiShi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state ==0) {
                    aaaa=0;
                    startTaiShi();
                    tvTaiShi.setText("结束态势回放");
                    state=1;
                }else {
                    taishiLayer.removeAll();
                    gCurrentLayerPos.removeAll();
                    mCountDownTimer.cancel();
                    tvTaiShi.setText("开始态势回放");
                    state=0;
                }

            }
        });
//        getCurrentEntityInfo();
//        gpsLocationManager.start(new MyListener());

        //添加测试数据
        String data_start = (String) SPUtils.get(MainActivity.this, "data_start", "");
        if (data_start.equals("")) {
            addPicture();
            addlistMarkCorerBean();
            SPUtils.put(MainActivity.this,"data_start","0");
        }

    }


    private void drawOtherMarks() {
        gLayerPos.removeAll();

        Map<String, Entity> locationMap = DecodingLibrary.getEntityLocationMap();
        for (Map.Entry<String, Entity> entry : locationMap.entrySet()) {
            if (entry.getValue().sideCode.equals(Entity.SIDE_RED)) {
                markRedCircleLocation(entry.getValue().locationPoint);
            } else if (entry.getValue().sideCode.equals(Entity.SIDE_BLUE)) {
                markBlueCircleLocation(entry.getValue().locationPoint);
            }
        }
    }

    @SuppressLint({"MissingPermission", "SetTextI18n"})
    private void currentMarkLocation() {
        gCurrentLayerPos.removeAll();
//        drawTool.activate(DrawTool.POLYLINE);
//        double locx = location.getLongitude()+0.00635;//����
//        double locy = location.getLatitude()+0.00095;//γ��
        wgspoint = new Point(114.441115, 38.03241166666667);

        mapPoint = (Point) GeometryEngine.project(wgspoint, SpatialReference.create(4326), mapView.getSpatialReference());
//        tvX.setText("经度："+ wgspoint.getX());
//        tvY.setText("纬度："+ wgspoint.getY());
        Graphic graphic = new Graphic(mapPoint, currentLocationSymbol);
        //this.markerSymbol = new SimpleMarkerSymbol(Color.RED, 16,
        //SimpleMarkerSymbol.STYLE.CIRCLE);
        //Graphic graphic = new Graphic(mapPoint,markerSymbol);
        gCurrentLayerPos.addGraphic(graphic);
        //判断是否为跟随模式
//        if(chk_isfollow.isChecked()){
//            mapView.centerAt(mapPoint, true);
//        }
//        mapView.setScale(20000);

//        ScaleBean.refreshScaleView(MainActivity.this,tvScale,mapView);
//        mapView.setMaxResolution(300);
    }


    //红圈位置
    private void markRedCircleLocation(Point point) {
        mapPoint = (Point) GeometryEngine.project(point, SpatialReference.create(4326), mapView.getSpatialReference());
        this.markerSymbol = new SimpleMarkerSymbol(Color.RED, 16, SimpleMarkerSymbol.STYLE.CIRCLE);
        Graphic graphic = new Graphic(mapPoint, markerSymbol);
        gLayerPos.addGraphic(graphic);

    }

    //添加覆盖物
    private void markCover() {
//        databaseOperation.deleteMarkCorer();

       List<PictureBean> list= databaseOperation.query();
        Drawable drawable;
        Resources res = getResources();
        for (int i = 0; i < list.size(); i++) {
            Point point = new Point(list.get(i).getPoistion_x(), list.get(i).getPoistion_y());
            mapPoint = (Point) GeometryEngine.project(point, SpatialReference.create(4326), mapView.getSpatialReference());
            Bitmap bmp= BitmapFactory.decodeFile(list.get(i).getImg_path());
            //自定义图片大小
//            Bitmap newBmp = Bitmap.createScaledBitmap(bmp, 160, 160, true);
            //将bitmap转化为Drawable,这是新的方法,如果用过时的方法Drawable drawable  = new BitmapDrawable(newBmp),则会造成图片大小和原来图片大小不符的情况,当然这种情况发生在没有自定义大小的情况下
            if (BeanUtil.isNotEmpty(bmp)) {
                drawable  = new BitmapDrawable(res,bmp);
                pictureMarkerSymbol = new PictureMarkerSymbol(MainActivity.this, drawable);
//            pictureMarkerSymbol.setUrl();
                Graphic graphic = new Graphic(mapPoint, pictureMarkerSymbol);
                markCover.addGraphic(graphic);
            }else {
                Toast.makeText(MainActivity.this, "请检查"+FileUtil.getSDCardPath() + "/DCIM"+"是否有覆盖物图片",Toast.LENGTH_SHORT).show();
                break;
            }

        }

    }

    //蓝圈位置
    private void markBlueCircleLocation(Point point) {
        mapPoint = (Point) GeometryEngine.project(point, SpatialReference.create(4326), mapView.getSpatialReference());
        this.markerSymbol = new SimpleMarkerSymbol(Color.BLUE, 16,
                SimpleMarkerSymbol.STYLE.CIRCLE);
        Graphic graphic = new Graphic(mapPoint, markerSymbol);
        gLayerPos.addGraphic(graphic);
    }


    /**
     * 态势回放
     */
    private void startTaiShi(){
    // 假如回放时间从2018-04-24 12:03:00 到2018-04-24 12:11:00
    // 先取出这个时间段的位置数据 根据路线方案取出
        List<SituationBean> situationBeanArrayList= databaseOperation.querySituation("1");      //查询态势开始时间和结束时间
        final List<QoistionBean> list = databaseOperation.querySetTimePoistion(situationBeanArrayList.get(0).getStarttime(),situationBeanArrayList.get(0).getEndtime());
        final List<StaffPositionBean> staffPositionBeanList = databaseOperation.queryStaffposition(situationBeanArrayList.get(0).getStarttime(),situationBeanArrayList.get(0).getEndtime());
         final List<RouteNewsBean> listRouteNewsBean=databaseOperation.queryRouteNews();
        try {
            startTime = Long.parseLong(dateToStamp(situationBeanArrayList.get(0).getStarttime()));
             endTime = Long.parseLong(dateToStamp(situationBeanArrayList.get(0).getEndtime()));
            final long time= endTime-startTime;
            polygon = new Polyline();
            taishiLayer.removeAll();
            mCountDownTimer =   new CountDownTimer(time, 1000) {
                //当前任务每完成一次倒计时间隔时间时回调
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                public void onTick(long millisUntilFinished) {
                  String  times= convertDate2(startTime+=1000);
                    for (int i = 0; i <listRouteNewsBean.size() ; i++) {
                        if (listRouteNewsBean.get(i).getStartTime().equals(times)) {
                            if (BeanUtil.isEmpty(confirmDialogPopWindow)) {
                                confirmDialogPopWindow = new ConfirmDialogPopWindow(MainActivity.this, listRouteNewsBean.get(i).getPoistion());
                            } else {
                                confirmDialogPopWindow.dismiss();
                                confirmDialogPopWindow = new ConfirmDialogPopWindow(MainActivity.this, listRouteNewsBean.get(i).getPoistion());
                            }
                            confirmDialogPopWindow.setBtnQuXiaoOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    confirmDialogPopWindow.dismiss();
                                }
                            });
                            confirmDialogPopWindow.setBtnQueDingOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    confirmDialogPopWindow.dismiss();
                                }
                            });
                        }
                    }
                    for (int i = 0; i <staffPositionBeanList.size() ; i++) {
                        if (staffPositionBeanList.get(i).getTimes().equals(times)) {
                            locationMap.put(staffPositionBeanList.get(i).getUserID(), staffPositionBeanList.get(i));
                        }
                    }
                    gLayerPos.removeAll();
                    for (Map.Entry<String, StaffPositionBean> entry : locationMap.entrySet()) {
                            if (entry.getValue().getUserType().equals(Entity.SIDE_RED)) {
                                markRedCircleLocation(entry.getValue().getLocationPoint());
                            } else if (entry.getValue().getUserType().equals(Entity.SIDE_BLUE)) {
                                markBlueCircleLocation(entry.getValue().getLocationPoint());
                            }
                    }
                    for (int i = 0; i <list.size() ; i++) {
                        if (list.get(i).getPoistionTime().equals(times)) {
                            gCurrentLayerPos.removeAll();
                            wgspoint = new Point(Double.parseDouble(list.get(i).getPoistion_x()), Double.parseDouble(list.get(i).getPoistion_y()));
                            mapPoint = (Point) GeometryEngine.project(wgspoint, SpatialReference.create(4326), mapView.getSpatialReference());
//                            if (aaaa == 0) {
//                                polygon.startPath(new Point(mapPoint.getX(), mapPoint.getY()));
//                            } else {
//                                polygon.lineTo(new Point(mapPoint.getX(), mapPoint.getY()));
//                            }
//                            aaaa++;
//                            graphics = new Graphic(polygon, new SimpleLineSymbol(Color.RED, 2));
//                            taishiLayer.addGraphic(graphics);
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


    private void markenvelopeLocation(Point point1) {
        mapPoint = (Point) GeometryEngine.project(point1, SpatialReference.create(4326), mapView.getSpatialReference());
        Graphic graphic = new Graphic(mapPoint, locationSymbol);
        gLayerPos.addGraphic(graphic);
        /*this.fillSymbol = new SimpleFillSymbol(Color.GREEN);
        this.fillSymbol.setAlpha(90);

        envelope.setCoords(point1.getX(), point1.getY(),point2.getX(), point2.getY());
        Graphic graphic = new Graphic(this.envelope, this.fillSymbol);
        gLayerPos.addGraphic(graphic);*/
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    /**
     * 下拉弹框
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.point:
                drawTool.activate(DrawTool.POINT);
                break;
            case R.id.envelope:
                drawTool.activate(DrawTool.ENVELOPE);
                break;
            case R.id.polygon:
                drawTool.activate(DrawTool.POLYGON);
                break;
            case R.id.polyline:
                drawTool.activate(DrawTool.POLYLINE);
                break;
            case R.id.freehandpolygon:
                drawTool.activate(DrawTool.FREEHAND_POLYGON);
                break;
            case R.id.freehandpolyline:
                drawTool.activate(DrawTool.FREEHAND_POLYLINE);
                break;
            case R.id.obstacle:
                Intent intent=new Intent(MainActivity.this,ObstacleActivity.class);
                startActivity(intent);
                break;
            case R.id.circle:
                drawTool.activate(DrawTool.CIRCLE);
                break;
            case R.id.clear:
                this.drawLayer.removeAll();
                this.drawTool.deactivate();
                break;
        }
        return true;
    }

    public void handleDrawEvent(DrawEvent event) {
        this.drawLayer.addGraphic(event.getDrawGraphic());
    }


    /**
     * 判断SD卡
     *
     * @return
     */
    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }


    public String getCurrentLocation(Location location) {
        String locStr = Double.toString(location.getLongitude()) + "+" + Double.toString(location.getLatitude());
        return locStr;
    }

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            drawOtherMarks();
        }
    };

    public void getCurrentEntityInfo() {
        String entiyAddrCode = "";
        try {
            String path = Environment.getExternalStorageDirectory().toString();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            builder = builderFactory.newDocumentBuilder();
            InputStream is = new FileInputStream(path + "/ClientConfig.xml");
            Document document = builder.parse(is);
            Element element = document.getDocumentElement();
            entiyAddrCode = element.getElementsByTagName("EntityAddrCode").item(0).getTextContent();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MyMessage.MSG_ENTITYADDRCODE = entiyAddrCode;
        //MyMessage.MSG_BEIAN =  MyMessage.MSG_BEIAN.replace("{2}",entiyAddrCode);
        //MyMessage.MSG_BEIAN =  MyMessage.MSG_BEIAN.replace("{3}",entiyAddrCode);
    }


    /**
     * 获取屏幕PPI
     *
     * @return
     */
    @SuppressLint("NewApi")
    private double getPPIOfDevice() {
        android.graphics.Point point = new android.graphics.Point();
        getWindowManager().getDefaultDisplay().getRealSize(point);//获取屏幕的真实分辨率
        DisplayMetrics dm = getResources().getDisplayMetrics();
        double x = Math.pow(point.x / dm.xdpi, 2);//
        double y = Math.pow(point.y / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        Double ppi = Math.sqrt(Math.pow(point.x, 2) + Math.pow(point.y, 2)) / screenInches;
        return ppi;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        locationDisplayManager.stop();
//        databaseOperation.deletePoistion();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.unpause();
        mapView.setScale(1000);

    }

    /**
     * 权限管理
     * @return
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                showToast("权限已申请");
                //离线地图
//                String theOfflineTiledLayers = getSDPath() + "/DCIM/test.tpk";
//                ArcGISLocalTiledLayer tLayer = new ArcGISLocalTiledLayer(theOfflineTiledLayers);
//                this.mapView.addLayer(tLayer);
//                this.mapView.addLayer(new GraphicsLayer());
                locationDisplayManager = mapView.getLocationDisplayManager();//获取定位类
                locationDisplayManager.setShowLocation(true);
                locationDisplayManager.setAccuracyCircleOn(false);       //是否要圈getLocationListener
                locationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);//设置模式
                locationDisplayManager.setShowPings(true);


                try {
                    locationDisplayManager.setAccuracySymbol(new SimpleFillSymbol(Color.GREEN).setAlpha(20));
                    locationDisplayManager.setDefaultSymbol(new PictureMarkerSymbol(this.getResources().getDrawable(R.drawable.icon_openmap_mark)));
                    locationDisplayManager.setLocationAcquiringSymbol(new PictureMarkerSymbol(this.getResources().getDrawable(R.drawable.icon_openmap_mark)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                locationDisplayManager.start();//开始定位

            } else {
                showToast("权限已拒绝");
            }
        } else if (requestCode == MY_PERMISSIONS_REQUEST_CALL_CAMERA) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissions[i]);
                    if (showRequestPermission) {
                        showToast("权限未申请");
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    class MyListener implements GPSLocationListener {

        @Override
        public void UpdateLocation(Location location) {
            if (location != null) {
//                text_gps.setText("经度：" + location.getLongitude() + "\n纬度：" + location.getLatitude());
            }
        }

        @Override
        public void UpdateStatus(String provider, int status, Bundle extras) {
            if ("gps" == provider) {
//                Toast.makeText(MainActivity.this, "定位类型：" + provider, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void UpdateGPSProviderStatus(int gpsStatus) {
            switch (gpsStatus) {
                case GPSProviderStatus.GPS_ENABLED:
//                    Toast.makeText(MainActivity.this, "GPS开启", Toast.LENGTH_SHORT).show();
                    break;
                case GPSProviderStatus.GPS_DISABLED:
//                    Toast.makeText(MainActivity.this, "GPS关闭", Toast.LENGTH_SHORT).show();
                    break;
                case GPSProviderStatus.GPS_OUT_OF_SERVICE:
//                    Toast.makeText(MainActivity.this, "GPS不可用", Toast.LENGTH_SHORT).show();
                    break;
                case GPSProviderStatus.GPS_TEMPORARILY_UNAVAILABLE:
//                    Toast.makeText(MainActivity.this, "GPS暂时不可用", Toast.LENGTH_SHORT).show();
                    break;
                case GPSProviderStatus.GPS_AVAILABLE:
//                    Toast.makeText(MainActivity.this, "GPS可用啦", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
    private void showToast(String string) {
        Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show();

    }

    /**
     * 获取系统当前时间
     * @return
     */
    public static String getNowDate(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime=sdf.format(System.currentTimeMillis());
        return nowTime;
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
    /**
     * 添加临时数据
     */
    public  void addPicture(){
        list =new ArrayList<>();
        String sd_path = FileUtil.getSDCardPath() + "/DCIM";
        PictureBean pictureBean1 =new PictureBean("101021508002",sd_path+"/zhuangzhai.png","zhuangzhai.png");
        PictureBean pictureBean2 =new PictureBean("101021509002",sd_path+"/fangtankehao.png","fangtankehao.png");
        PictureBean pictureBean3 =new PictureBean("101021509012",sd_path+"/zujueqiang.png","zujueqiang.png");
        PictureBean pictureBean4 =new PictureBean("101021511002",sd_path+"/lanzhang.png","lanzhang.png");
        PictureBean pictureBean5 =new PictureBean("101021507002",sd_path+"/tiesiwang.png","tiesiwang.png");
        PictureBean pictureBean6 =new PictureBean("101021501002",sd_path+"/qianhao.png","qianhao.png");
        PictureBean pictureBean7 =new PictureBean("101021516002",sd_path+"/chuizhizhezhang.png","chuizhizhezhang.png");
        PictureBean pictureBean8 =new PictureBean("101021513001",sd_path+"/sanjiaozhuang.png","sanjiaozhuang.png");
        PictureBean pictureBean9 =new PictureBean("101021504001",sd_path+"/yezhangongshi.png","yezhangongshi.png");
        PictureBean pictureBean10 =new PictureBean("101021506001",sd_path+"/dibao.png","dibao.png");
        list.add(pictureBean1);
        list.add(pictureBean2);
        list.add(pictureBean3);
        list.add(pictureBean4);
        list.add(pictureBean5);
        list.add(pictureBean6);
        list.add(pictureBean7);
        list.add(pictureBean8);
        list.add(pictureBean9);
        list.add(pictureBean10);
        for (int i = 0; i <list.size() ; i++) {
            databaseOperation.addPicture(list.get(i));
        }
    }
    public void addlistMarkCorerBean() {
        listMarkCorerBean = new ArrayList<>();
        MarkCorerBean markCorerBean1 =new MarkCorerBean("1","101021508002",114.43282166664521,38.01868333335248);
        MarkCorerBean markCorerBean2 =new MarkCorerBean("1","101021509002",114.43601166666667,38.018355);
        MarkCorerBean markCorerBean3 =new MarkCorerBean("1","101021509012",114.44776166666666,38.020338333333335);
        MarkCorerBean markCorerBean4 =new MarkCorerBean("1","101021511002",114.44486166666665,38.025846666666666);
        MarkCorerBean markCorerBean5 =new MarkCorerBean("1","101021507002",114.43797166666667,38.03556666666667);
        MarkCorerBean markCorerBean6 =new MarkCorerBean("1","101021501002",114.41416166666667,38.019194999999996);
        MarkCorerBean markCorerBean7 =new MarkCorerBean("1","101021516002",114.403815,38.02699666666667);
        MarkCorerBean markCorerBean8 =new MarkCorerBean("1","101021513001",114.42700833333335,38.00969666666666);
        MarkCorerBean markCorerBean9 =new MarkCorerBean("1","101021504001",114.44149666666665,38.00085);
        MarkCorerBean markCorerBean10 =new MarkCorerBean("1","101021506001",114.46469,38.0075);
        listMarkCorerBean.add(markCorerBean1);
        listMarkCorerBean.add(markCorerBean2);
        listMarkCorerBean.add(markCorerBean3);
        listMarkCorerBean.add(markCorerBean4);
        listMarkCorerBean.add(markCorerBean5);
        listMarkCorerBean.add(markCorerBean6);
        listMarkCorerBean.add(markCorerBean7);
        listMarkCorerBean.add(markCorerBean8);
        listMarkCorerBean.add(markCorerBean9);
        listMarkCorerBean.add(markCorerBean10);
        for (int i = 0; i <listMarkCorerBean.size() ; i++) {
            databaseOperation.addmarkCorer(listMarkCorerBean.get(i));
        }
    }
}
