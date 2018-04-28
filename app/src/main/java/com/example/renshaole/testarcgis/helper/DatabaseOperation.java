package com.example.renshaole.testarcgis.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.esri.core.geometry.Point;
import com.example.renshaole.testarcgis.bean.MarkCorerBean;
import com.example.renshaole.testarcgis.bean.PictureBean;
import com.example.renshaole.testarcgis.bean.QoistionBean;
import com.example.renshaole.testarcgis.bean.RouteNewsBean;
import com.example.renshaole.testarcgis.bean.SituationBean;
import com.example.renshaole.testarcgis.bean.StaffPositionBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 描    述：数据库增删改查操作
 * 作    者：ZhaoPengshuai
 * 时    间：2018/04/11 0001
 */

public class DatabaseOperation {
    private DatabaseHelper databaseHelper;
    private Context context;

    public DatabaseOperation(Context context) {
        databaseHelper = new DatabaseHelper(context);
        this.context = context;
    }

    // 添加Poistion轨迹经纬度表的记录
    public void addPoistion(double X,double Y,String route,String time) {
        //获取操作数据库的工具类
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues valuse = new ContentValues();
            valuse.put(DateSheet.poistion.POISTION_X, X);
            valuse.put(DateSheet.poistion.POISTION_Y, Y);
            valuse.put(DateSheet.poistion.ROUTE_TYPE, route);
            valuse.put(DateSheet.poistion.POISTION_TIME, time);
            //参数(表名,可以为空了列名，ContentValues)
            db.insert(DateSheet.poistion.TABLE_NAME, DateSheet.poistion.POISTION_Y, valuse);
                       db.close();
    }


    // 添加RouteNews机动路线信息表的记录
    public void addRouteNews(String poistion,String time,String route) {
        //获取操作数据库的工具类
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues valuse = new ContentValues();
        valuse.put(DateSheet.routeNews.POISTION, poistion);
        valuse.put(DateSheet.routeNews.STARTTIME, time);
        valuse.put(DateSheet.routeNews.ROUTE_TYPE, route);
        //参数(表名,可以为空了列名，ContentValues)
        db.insert(DateSheet.routeNews.TABLE_NAME, DateSheet.routeNews.POISTION, valuse);
        db.close();
    }
    // 添加markCorer障碍物坐标表的记录
    public void addmarkCorer(MarkCorerBean markCorerBean) {
        //获取操作数据库的工具类
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.beginTransaction();
        ContentValues valuse = new ContentValues();
        valuse.put(DateSheet.markCorer.STATE, markCorerBean.getState());
        valuse.put(DateSheet.markCorer.TYPE, markCorerBean.getType());
        valuse.put(DateSheet.markCorer.POISTION_X, markCorerBean.getPoistion_x());
        valuse.put(DateSheet.markCorer.POISTION_Y, markCorerBean.getPoistion_y());
        //参数(表名,可以为空了列名，ContentValues)
        db.insert(DateSheet.markCorer.TABLE_NAME, DateSheet.markCorer.POISTION_X, valuse);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
    // 添加Picture资源表的记录
    public void addPicture(PictureBean pictureBean) {
        //获取操作数据库的工具类
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues valuse = new ContentValues();
        valuse.put(DateSheet.picture.TYPE, pictureBean.getTypes());
        valuse.put(DateSheet.picture.IMGNAME, pictureBean.getImg_name());
        valuse.put(DateSheet.picture.IMGPATH, pictureBean.getImg_path());
        //参数(表名,可以为空了列名，ContentValues)
        db.insert(DateSheet.picture.TABLE_NAME, DateSheet.picture.IMGPATH, valuse);
        db.close();
    }

    // 添加situation态势表的记录
    public void addSituation(SituationBean situationBean) {
        //获取操作数据库的工具类
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues valuse = new ContentValues();
        valuse.put(DateSheet.situation.ROUTE_TYPE, situationBean.getRoute_type());
        valuse.put(DateSheet.situation.STARTTIME, situationBean.getStarttime());
        valuse.put(DateSheet.situation.ENDTIME, situationBean.getEndtime());
        valuse.put(DateSheet.situation.TIME, situationBean.getTimes());
        //参数(表名,可以为空了列名，ContentValues)
        db.insert(DateSheet.situation.TABLE_NAME, DateSheet.staffposition.ROUTE_TYPE, valuse);
        db.close();
    }

    // 添加staffposition所有人员位置信息表的记录
    public void addStaffSosition(StaffPositionBean staffPositionBean) {
        //获取操作数据库的工具类
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues valuse = new ContentValues();
        valuse.put(DateSheet.staffposition.ROUTE_TYPE, staffPositionBean.getRoute_type());
        valuse.put(DateSheet.staffposition.USERTYPE, staffPositionBean.getUserType());
        valuse.put(DateSheet.staffposition.USERID, staffPositionBean.getUserID());
        valuse.put(DateSheet.staffposition.POISTION_X, staffPositionBean.getPoistion_x());
        valuse.put(DateSheet.staffposition.POISTION_Y, staffPositionBean.getPoistion_y());
        valuse.put(DateSheet.staffposition.TIME, staffPositionBean.getTimes());
        //参数(表名,可以为空了列名，ContentValues)
        db.insert(DateSheet.staffposition.TABLE_NAME, DateSheet.staffposition.TIME, valuse);
        db.close();
    }


    //删除Poistion轨迹经纬度表的记录
    public void deletePoistion() {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            //表名，条件，条件的值
            db.delete(DateSheet.poistion.TABLE_NAME, null, null);
            db.close();
        }

    //根据id删除RouteNews数据
    public void deleteRouteNews(long id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String whereClause = DateSheet.routeNews._ID + "=?";
        String[] whereArgs = {String.valueOf(id)};
        //表名，条件，条件的值
        db.delete(DateSheet.routeNews.TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    //删除MarkCorer表的记录
    public void deleteMarkCorer() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        //表名，条件，条件的值
        db.delete(DateSheet.markCorer.TABLE_NAME, null, null);
        db.close();
    }

    //查询Qoistion轨迹经纬度全部类型
    public ArrayList<QoistionBean> queryPoistion() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        //是否去重复记录，表名，要查询的列，查询条件，查询条件的值，分组条件，分组条件的值，排序，分页条件2018-04-24 12:03:00 到2018-04-24 12:11:00
        Cursor c = db.query(DateSheet.poistion.TABLE_NAME, null, null , null, null, null, DateSheet.poistion.POISTION_TIME + " ASC");
        ArrayList<QoistionBean> qoistionBeans = new ArrayList();
        QoistionBean qoistionBean = null;
        while (c.moveToNext()) {
            qoistionBean = new QoistionBean();
            qoistionBean.setPoistion_x(c.getString(c.getColumnIndexOrThrow(DateSheet.poistion.POISTION_X)));
            qoistionBean.setPoistion_y(c.getString(c.getColumnIndexOrThrow(DateSheet.poistion.POISTION_Y)));
            qoistionBean.setRouteType(c.getString(c.getColumnIndexOrThrow(DateSheet.poistion.ROUTE_TYPE)));
            qoistionBean.setPoistionTime(c.getString(c.getColumnIndexOrThrow(DateSheet.poistion.POISTION_TIME)));
            qoistionBeans.add(qoistionBean);
        }
        c.close();
        db.close();
        return qoistionBeans;
    }
    //根据时间查询staffposition所有人员位置信息表的记录
    public ArrayList<StaffPositionBean> queryStaffposition(String startTime,String endTime) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<StaffPositionBean> staffPositionBeanArrayList = new ArrayList();
        String sql = "select * from  staffposition where  time between '" + startTime + "'and '" + endTime + "'  order by time ASC ";
        Cursor c = db.rawQuery(sql, null);
        StaffPositionBean staffPositionBean = null;
        while (c.moveToNext()) {
            staffPositionBean = new StaffPositionBean();
            staffPositionBean.setPoistion_x(c.getString(c.getColumnIndexOrThrow(DateSheet.staffposition.POISTION_X)));
            staffPositionBean.setPoistion_y(c.getString(c.getColumnIndexOrThrow(DateSheet.staffposition.POISTION_Y)));
            staffPositionBean.setRoute_type(c.getString(c.getColumnIndexOrThrow(DateSheet.staffposition.ROUTE_TYPE)));
            staffPositionBean.setTimes(c.getString(c.getColumnIndexOrThrow(DateSheet.staffposition.TIME)));
            staffPositionBean.setUserID(c.getString(c.getColumnIndexOrThrow(DateSheet.staffposition.USERID)));
            staffPositionBean.setUserType(c.getString(c.getColumnIndexOrThrow(DateSheet.staffposition.USERTYPE)));
            Point point = new Point(Double.parseDouble(staffPositionBean.getPoistion_x()), Double.parseDouble(staffPositionBean.getPoistion_y()));
            staffPositionBean.setLocationPoint(point);
            staffPositionBeanArrayList.add(staffPositionBean);
        }
        c.close();
        db.close();
        return staffPositionBeanArrayList;
    }
    //根据时间查询Qoistion轨迹经纬度
    public ArrayList<QoistionBean> querySetTimePoistion(String startTime,String endTime) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<QoistionBean> qoistionBeans = new ArrayList();
        String sql = "select * from  poistion where  poistion_time between '" + startTime + "'and '" + endTime + "'  order by poistion_time ASC ";
        Cursor c = db.rawQuery(sql, null);
        QoistionBean qoistionBean = null;
        while (c.moveToNext()) {
            qoistionBean = new QoistionBean();
            qoistionBean.setPoistion_x(c.getString(c.getColumnIndexOrThrow(DateSheet.poistion.POISTION_X)));
            qoistionBean.setPoistion_y(c.getString(c.getColumnIndexOrThrow(DateSheet.poistion.POISTION_Y)));
            qoistionBean.setRouteType(c.getString(c.getColumnIndexOrThrow(DateSheet.poistion.ROUTE_TYPE)));
            qoistionBean.setPoistionTime(c.getString(c.getColumnIndexOrThrow(DateSheet.poistion.POISTION_TIME)));
            qoistionBeans.add(qoistionBean);
        }
        c.close();
        db.close();
        return qoistionBeans;
    }
    //根据状态查询situation态势表的记录 （按理说应该查询全部态势列表，然后根据选择哪一项进行态势回放）
    public ArrayList<SituationBean> querySituation(String type) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<SituationBean> situationBeanArrayList = new ArrayList();
        String sql = "select * from  situation where  route_type = '" + type + "'";
        Cursor c = db.rawQuery(sql, null);
        SituationBean situationBean;
        while (c.moveToNext()) {
            situationBean =new SituationBean();
            situationBean.setRoute_type(c.getString(c.getColumnIndexOrThrow(DateSheet.situation.ROUTE_TYPE)));
            situationBean.setStarttime(c.getString(c.getColumnIndexOrThrow(DateSheet.situation.STARTTIME)));
            situationBean.setEndtime(c.getString(c.getColumnIndexOrThrow(DateSheet.situation.ENDTIME)));
            situationBeanArrayList.add(situationBean);
        }
        c.close();
        db.close();
        return situationBeanArrayList;
    }
    //查询RouteNews机动路线信息表的记录
    public ArrayList<RouteNewsBean> queryRouteNews() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        //是否去重复记录，表名，要查询的列，查询条件，查询条件的值，分组条件，分组条件的值，排序，分页条件
        Cursor c = db.query(DateSheet.routeNews.TABLE_NAME, null, null , null, null, null, DateSheet.routeNews._ID + " DESC");
        ArrayList<RouteNewsBean> routeNewsBeanArrayList = new ArrayList();
        RouteNewsBean routeNewsBean = null;
        while (c.moveToNext()) {
            routeNewsBean = new RouteNewsBean();
            routeNewsBean.setId(c.getLong(c.getColumnIndexOrThrow(DateSheet.routeNews._ID)));
            routeNewsBean.setPoistion(c.getString(c.getColumnIndexOrThrow(DateSheet.routeNews.POISTION)));
            routeNewsBean.setStartTime(c.getString(c.getColumnIndexOrThrow(DateSheet.routeNews.STARTTIME)));
            routeNewsBean.setRoute_type(c.getString(c.getColumnIndexOrThrow(DateSheet.routeNews.ROUTE_TYPE)));
            routeNewsBeanArrayList.add(routeNewsBean);
        }
        c.close();
        db.close();
        return routeNewsBeanArrayList;
    }

    //查询markCorer全部类型
    public ArrayList<MarkCorerBean> queryMarkCorer(String start) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        //是否去重复记录，表名，要查询的列，查询条件，查询条件的值，分组条件，分组条件的值，排序，分页条件
        Cursor c = db.query(DateSheet.markCorer.TABLE_NAME, null, null , null, null, null, DateSheet.routeNews._ID + " DESC");
        ArrayList<MarkCorerBean> markCorerBeanArrayList = new ArrayList();
        MarkCorerBean markCorerBean = null;
        while (c.moveToNext()) {
            markCorerBean = new MarkCorerBean();
            markCorerBean.setState(c.getString(c.getColumnIndexOrThrow(DateSheet.markCorer.STATE)));
            markCorerBean.setType(c.getString(c.getColumnIndexOrThrow(DateSheet.markCorer.TYPE)));
            markCorerBean.setPoistion_x(Double.parseDouble(c.getString(c.getColumnIndexOrThrow(DateSheet.markCorer.POISTION_X))));
            markCorerBean.setPoistion_y(Double.parseDouble(c.getString(c.getColumnIndexOrThrow(DateSheet.markCorer.POISTION_Y))));
            markCorerBeanArrayList.add(markCorerBean);
        }
        c.close();
        db.close();
        return markCorerBeanArrayList;
    }

    //多表联合查询
    public List<PictureBean> query() {
        List<PictureBean> pictureBeanList =new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT picture.img_path, markCorer.poistion_x,markCorer.poistion_y FROM  picture,markCorer "+ "WHERE picture.type= markCorer.type",null);
        PictureBean pictureBean = null;
        while (c.moveToNext()) {
            pictureBean = new PictureBean();
            pictureBean.setImg_path(c.getString(c.getColumnIndexOrThrow(DateSheet.picture.IMGPATH)));
            pictureBean.setPoistion_x(Double.parseDouble(c.getString(c.getColumnIndexOrThrow(DateSheet.markCorer.POISTION_X))));
            pictureBean.setPoistion_y(Double.parseDouble(c.getString(c.getColumnIndexOrThrow(DateSheet.markCorer.POISTION_Y))));
            pictureBeanList.add(pictureBean);
        }
        c.close();
        db.close();
        return pictureBeanList;
    }
}
