package com.example.renshaole.testarcgis;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
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
import com.example.renshaole.testarcgis.bean.QoistionBean;
import com.example.renshaole.testarcgis.bean.ScaleBean;
import com.example.renshaole.testarcgis.decoding.DecodingLibrary;
import com.example.renshaole.testarcgis.helper.DatabaseHelper;
import com.example.renshaole.testarcgis.helper.DatabaseOperation;
import com.example.renshaole.testarcgis.pop.ConfirmDialogPopWindow;
import com.example.renshaole.testarcgis.utils.BeanUtil;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static com.example.renshaole.testarcgis.utils.util.CORER_ZHUANGZHAI;


public class MainActivity extends AdaptationActivity implements DrawEventListener {
    Polyline polygon ;
    Graphic graphics;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CALL_CAMERA = 2;
    String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    List<String> mPermissionList = new ArrayList<>();
    private   String network="gps";
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
    TextView tvScale;
    TextView tvX;
    TextView tvY;
    TextView tvRouteNews;
    ImageView iv_connect;
    GraphicsLayer gLayerPos;
    GraphicsLayer markCover;
    GraphicsLayer gCurrentLayerPos;
    GraphicsLayer line;
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
    private int pointCount=0;
    LocationDisplayManager locationDisplayManager;
    DatabaseOperation databaseOperation;
    double x = 0;
    double y=0;
    boolean isSelected=true;
    private  App app;
   private ConfirmDialogPopWindow confirmDialogPopWindow;
    private PictureMarkerSymbol pictureMarkerSymbol;
    private  List<MarkCorerBean> markCorerBeanList;
    private CheckBox chkbox_Corer;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint({"MissingPermission", "HandlerLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app= (App) getApplication();
        mPermissionList.clear();
        handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0x11) {
//                    locMag.requestLocationUpdates(network, 2000, 10, locationListener);
                    Bundle bundle=msg.getData();
                    String position=bundle.getString("position");
                    String time=bundle.getString("time");
                    if (BeanUtil.isEmpty(confirmDialogPopWindow)) {
                        confirmDialogPopWindow =new ConfirmDialogPopWindow(MainActivity.this,position);
                        databaseOperation.addRouteNews(position,time);
                    }else {
                        confirmDialogPopWindow.dismiss();
                        confirmDialogPopWindow =new ConfirmDialogPopWindow(MainActivity.this,position);
                        databaseOperation.addRouteNews(position,time);
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

        databaseOperation=new DatabaseOperation(this);

        iv_connect = this.findViewById(R.id.iv_connnect);
        tvScale=findViewById(R.id.tvScale);
        tvX=findViewById(R.id.tvX);
        tvY=findViewById(R.id.tvY);
        tvRouteNews=findViewById(R.id.tvRouteNews);
        chk_isfollow = this.findViewById(R.id.chkbox_isFollow);
        chkbox_Corer = this.findViewById(R.id.chkbox_Corer);
        chkbox_isGuiJi = this.findViewById(R.id.chkbox_isGuiJi);
        chkbox_isLookGuiJi = this.findViewById(R.id.chkbox_isLookGuiJi);
        GPS_btn = findViewById(R.id.GPS_btn);
        mapView = this.findViewById(R.id.map);

//        MarkCorerBean markCorerBean =new MarkCorerBean();
//        markCorerBean.setStart("1");
//        markCorerBean.setType("10");
//        markCorerBean.setPoistion_x(114.46469);
//        markCorerBean.setPoistion_y(38.0075);
//        databaseOperation.addmarkCorer(markCorerBean);
        tvRouteNews.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RouteNewsActivity.class);
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
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
            //离线地图
//            String theOfflineTiledLayers = getSDPath() +  "/DCIM/test.tpk";
//            ArcGISLocalTiledLayer tLayer = new ArcGISLocalTiledLayer(theOfflineTiledLayers);
//            this.mapView.addLayer(tLayer);
//            this.mapView.addLayer(new GraphicsLayer());
            locationDisplayManager =  mapView.getLocationDisplayManager();//获取定位类
            locationDisplayManager.setShowLocation(true);
            locationDisplayManager.setAccuracyCircleOn(false);       //是否要圈
            locationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);//设置模式
            locationDisplayManager.setShowPings(true);
            try {
                locationDisplayManager.setAccuracySymbol(new SimpleFillSymbol(Color.GREEN).setAlpha(20));
                locationDisplayManager.setDefaultSymbol  (new PictureMarkerSymbol(this.getResources().getDrawable(R.drawable.icon_openmap_mark)));
                locationDisplayManager.setLocationAcquiringSymbol (new PictureMarkerSymbol(this.getResources().getDrawable(R.drawable.icon_openmap_mark)));
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
        locationSymbol = new PictureMarkerSymbol(this.getResources().getDrawable(R.drawable.location));
        currentLocationSymbol = new PictureMarkerSymbol(this.getResources().getDrawable(R.mipmap.ic_launcher));
        locMag = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        GPS_btn.setOnClickListener(new OnClickListener() {
            @SuppressLint("MissingPermission")
            public void onClick(View v) {
                // TODO Auto-generated method stub
                locationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);//设置模式
                locationDisplayManager.start();
            }

        });



        //第一个点startPath,后面的点用lineTo
        polygon = new Polyline();
        locationDisplayManager.setLocationListener(new LocationListener() {
            DecimalFormat df = new DecimalFormat("###.000000");
            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationChanged(Location location) {
                double lon = location.getLongitude();
                double lat = location.getLatitude();
                
                Log.d("AAA", "Lon:" + df.format(lon) + " , " + "Lat:" + df.format(lat));
                wgspoint = new Point(lon, lat);
                tvX.setText("纬度："+ wgspoint.getX());
                tvY.setText("经度："+ wgspoint.getY());
                String locStr = getCurrentLocation(location);
                MyMessage.MSG_ENTITYLOCXY = locStr;
//                dataHelper.addTask(wgspoint.getX(),wgspoint.getY());
//                dataHelper.GetUserList();
                mapPoint = (Point) GeometryEngine.project(wgspoint,SpatialReference.create(4326),mapView.getSpatialReference());

                //判断是否开启轨迹
                if (chkbox_isGuiJi.isChecked()) {
                        if (pointCount == 0) {
                                polygon.startPath(new Point(mapPoint.getX(),mapPoint.getY()));
                            databaseOperation.addPoistion(wgspoint.getX(),wgspoint.getY());    //保存经纬度
                                x=wgspoint.getX();
                                y=wgspoint.getY();
                        } else {
                            if (chkbox_isLookGuiJi.isChecked()) {
                                if (isSelected ) {
                                    polygon.lineTo(new Point(mapPoint.getX(),mapPoint.getY()));
                                }
                                if (x==wgspoint.getX()||y==wgspoint.getY()) {
                                }else {
                                    databaseOperation.addPoistion(wgspoint.getX(),wgspoint.getY());    //保存经纬度
                                }
                                x=wgspoint.getX();
                                y=wgspoint.getY();
                            }else {
                                if (x==wgspoint.getX()||y==wgspoint.getY()) {
                                }else {
                                    databaseOperation.addPoistion(wgspoint.getX(),wgspoint.getY());    //保存经纬度
                                }
                                x=wgspoint.getX();
                                y=wgspoint.getY();
                            }

                    }
                    if (pointCount == 0) {
                        long  scale= (long) SPUtils.get(MainActivity.this,"scale",(long)-1);
                        if (scale == -1) {
                            mapView.setScale(500000);   //设置初始比例为5公里
                        }else {
                            mapView.setScale(scale*100);
                        }
                        ScaleBean.refreshScaleView(MainActivity.this,tvScale,mapView);
                    }

                    if ( !chkbox_isLookGuiJi.isChecked()) {

                    }else {
                        graphics = new Graphic(polygon,new SimpleLineSymbol(Color.RED, 2));
                        line.addGraphic(graphics);
                    }
                }
                //判断是否为跟随模式
                if(chk_isfollow.isChecked()){
                    mapView.centerAt(mapPoint, true);
                }
                pointCount++;

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
                      isSelected=false;
                      polygon = new Polyline();
                      line.removeAll();
                     int a=0;
                      List<QoistionBean> list= databaseOperation.queryPoistion();
                      for (int i = 0; i <list.size() ; i++) {
                          wgspoint = new Point(Double.parseDouble(list.get(i).getPoistion_x()),Double.parseDouble(list.get(i).getPoistion_y()));
                          mapPoint = (Point) GeometryEngine.project(wgspoint,SpatialReference.create(4326),mapView.getSpatialReference());
                          if (a == 0) {
                              polygon.startPath(new Point(mapPoint.getX(),mapPoint.getY()));
                          }else {
                              polygon.lineTo(new Point(mapPoint.getX(),mapPoint.getY()));
                          }
                          a++;
                          graphics = new Graphic(polygon,new SimpleLineSymbol(Color.RED, 2));
                          line.addGraphic(graphics);
                      }
                       }else {
                      isSelected=true;
                      line.removeAll();
                  }
         }
    });
        chkbox_Corer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    markCover();
                }else {
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
                double scales=mapView.getScale()/100;//结果单位米，表示图上1厘米代表*米
                SPUtils.put(MainActivity.this,"scale",(long)scales);
                ScaleBean.refreshScaleView(MainActivity.this,tvScale,mapView);
            }
        });

//        getCurrentEntityInfo();
    }



    private void drawOtherMarks()
    {
        gLayerPos.removeAll();
        Map<String, Entity> locationMap = DecodingLibrary.getEntityLocationMap();
        for (Map.Entry<String, Entity> entry : locationMap.entrySet()) {
            if(entry.getValue().sideCode.equals(Entity.SIDE_RED) ) {
                markRedCircleLocation(entry.getValue().locationPoint);
            }
            else if (entry.getValue().sideCode.equals(Entity.SIDE_BLUE)){
                markBlueCircleLocation(entry.getValue().locationPoint);
            }
        }
    }

    @SuppressLint({"MissingPermission", "SetTextI18n"})
    private void currentMarkLocation()
    {
        gCurrentLayerPos.removeAll();
//        drawTool.activate(DrawTool.POLYLINE);
//        double locx = location.getLongitude()+0.00635;//����
//        double locy = location.getLatitude()+0.00095;//γ��
        wgspoint = new Point(114.441115,38.03241166666667);

        mapPoint = (Point) GeometryEngine.project(wgspoint,SpatialReference.create(4326),mapView.getSpatialReference());
//        tvX.setText("经度："+ wgspoint.getX());
//        tvY.setText("纬度："+ wgspoint.getY());
        Graphic graphic = new Graphic(mapPoint,currentLocationSymbol);
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
    private void markRedCircleLocation(Point point)
    {
        mapPoint = (Point) GeometryEngine.project(point,SpatialReference.create(4326),mapView.getSpatialReference());
        this.markerSymbol = new SimpleMarkerSymbol(Color.RED, 16,
                SimpleMarkerSymbol.STYLE.CIRCLE);
        Graphic graphic = new Graphic(mapPoint,markerSymbol);
        gLayerPos.addGraphic(graphic);
    }

    //添加覆盖物
    private void markCover()
    {
        markCorerBeanList =databaseOperation.queryMarkCorer("1");
        Drawable drawable = null;
        for (int i = 0; i <markCorerBeanList.size() ; i++) {
            String type=markCorerBeanList.get(i).type;
            Point point =new Point(markCorerBeanList.get(i).getPoistion_x(),markCorerBeanList.get(i).getPoistion_y());
            mapPoint = (Point) GeometryEngine.project(point,SpatialReference.create(4326),mapView.getSpatialReference());
            if (type.equals(util.CORER_ZHUANGZHAI)) {
                 drawable = getResources().getDrawable(R.mipmap.zhuangzhai);
            }else if (type.equals(util.CORER_TANKEHAO)){
                drawable = getResources().getDrawable(R.mipmap.fangtankehao);
            }else if (type.equals(util.CORER_ZUJUETIAN)){
                drawable = getResources().getDrawable(R.mipmap.zujueqiang);
            }else if (type.equals(util.CORER_LANZHANG)){
                drawable = getResources().getDrawable(R.mipmap.lanzhang);
            }else if (type.equals(util.CORER_TIESIWANG)){
                drawable = getResources().getDrawable(R.mipmap.tiesiwang);
            }else if (type.equals(util.CORER_QIANHAO)){
                drawable = getResources().getDrawable(R.mipmap.qianhao);
            }else if (type.equals(util.CORER_ZHEZHANG)){
                drawable = getResources().getDrawable(R.mipmap.chuizhizhezhang);
            }else if (type.equals(util.CORER_JUMA)){
                drawable = getResources().getDrawable(R.mipmap.juma);
            }else if (type.equals(util.CORER_SANJIAOZHUANG)){
                drawable = getResources().getDrawable(R.mipmap.sanjiaozhuang);
            }else if (type.equals(util.CORER_YEZHANGONGSHI)){
                drawable = getResources().getDrawable(R.mipmap.yezhangongshi);
            }else if (type.equals(util.CORER_DIBAO)){
                drawable = getResources().getDrawable(R.mipmap.dibao);
            }
            pictureMarkerSymbol = new PictureMarkerSymbol(MainActivity.this, drawable);
            Graphic graphic = new Graphic(mapPoint, pictureMarkerSymbol);
            markCover.addGraphic(graphic);
        }

    }

    //蓝圈位置
    private void markBlueCircleLocation(Point point)
    {
        mapPoint = (Point) GeometryEngine.project(point,SpatialReference.create(4326),mapView.getSpatialReference());
        this.markerSymbol = new SimpleMarkerSymbol(Color.BLUE, 16,
                SimpleMarkerSymbol.STYLE.CIRCLE);
        Graphic graphic = new Graphic(mapPoint,markerSymbol);
        gLayerPos.addGraphic(graphic);
    }



    private void markenvelopeLocation(Point point1)
    {
        mapPoint = (Point) GeometryEngine.project(point1,SpatialReference.create(4326),mapView.getSpatialReference());
        Graphic graphic = new Graphic(mapPoint,locationSymbol);
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
     *下拉弹框
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
     * @return
     */
    public String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if(sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }


    public  String getCurrentLocation(Location location) {
        String locStr = Double.toString(location.getLongitude()) + "+" + Double.toString(location.getLatitude());
        return  locStr;
    }

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            drawOtherMarks();
        }
    };

    public  void getCurrentEntityInfo() {
        String entiyAddrCode = "";
        try {
            String path = Environment.getExternalStorageDirectory().toString();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            builder = builderFactory.newDocumentBuilder();
            InputStream is= new FileInputStream(path + "/ClientConfig.xml");
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
     * @return
     */
    @SuppressLint("NewApi")
    private double getPPIOfDevice() {
        android.graphics.Point point = new android.graphics.Point();
        getWindowManager().getDefaultDisplay().getRealSize(point);//获取屏幕的真实分辨率
        DisplayMetrics dm = getResources().getDisplayMetrics();
        double x = Math.pow(point.x/ dm.xdpi, 2);//
        double y = Math.pow(point.y / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        Double ppi=Math.sqrt(Math.pow(point.x, 2)+Math.pow(point.y, 2))/screenInches;
        return ppi;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationDisplayManager.stop();
        databaseOperation.deletePoistion();
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
    @Override 	protected void onResume() {
        super.onResume();
        mapView.unpause();
        mapView.setScale(1000);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                showToast("权限已申请");
                //离线地图
//                String theOfflineTiledLayers = getSDPath() +  "/DCIM/test.tpk";
//                ArcGISLocalTiledLayer tLayer = new ArcGISLocalTiledLayer(theOfflineTiledLayers);
//                this.mapView.addLayer(tLayer);
//                this.mapView.addLayer(new GraphicsLayer());
                locationDisplayManager =  mapView.getLocationDisplayManager();//获取定位类
                locationDisplayManager.setShowLocation(true);
                locationDisplayManager.setAccuracyCircleOn(false);       //是否要圈
                locationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);//设置模式
                locationDisplayManager.setShowPings(true);


                try {
                    locationDisplayManager.setAccuracySymbol(new SimpleFillSymbol(Color.GREEN).setAlpha(20));
                    locationDisplayManager.setDefaultSymbol  (new PictureMarkerSymbol(this.getResources().getDrawable(R.drawable.icon_openmap_mark)));
                    locationDisplayManager.setLocationAcquiringSymbol (new PictureMarkerSymbol(this.getResources().getDrawable(R.drawable.icon_openmap_mark)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                locationDisplayManager.start();//开始定位

            } else {
                showToast("权限已拒绝");
            }
        }else if (requestCode == MY_PERMISSIONS_REQUEST_CALL_CAMERA){
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

    private void showToast(String string){
        Toast.makeText(MainActivity.this,string,Toast.LENGTH_LONG).show();
    }

}
