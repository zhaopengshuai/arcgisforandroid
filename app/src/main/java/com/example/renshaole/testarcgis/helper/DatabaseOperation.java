package com.example.renshaole.testarcgis.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.renshaole.testarcgis.bean.MarkCorerBean;
import com.example.renshaole.testarcgis.bean.PictureBean;
import com.example.renshaole.testarcgis.bean.QoistionBean;
import com.example.renshaole.testarcgis.bean.RouteNewsBean;

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

    // 添加Poistion表的记录
    public void addPoistion(double X,double Y) {
        //获取操作数据库的工具类
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues valuse = new ContentValues();
            valuse.put(DateSheet.poistion.POISTION_X, X);
            valuse.put(DateSheet.poistion.POISTION_Y, Y);
            //参数(表名,可以为空了列名，ContentValues)
            db.insert(DateSheet.poistion.TABLE_NAME, DateSheet.poistion.POISTION_Y, valuse);
                       db.close();
    }


    // 添加RouteNews表的记录
    public void addRouteNews(String poistion,String time) {
        //获取操作数据库的工具类
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues valuse = new ContentValues();
        valuse.put(DateSheet.routeNews.POISTION, poistion);
        valuse.put(DateSheet.routeNews.STARTTIME, time);
        //参数(表名,可以为空了列名，ContentValues)
        db.insert(DateSheet.routeNews.TABLE_NAME, DateSheet.routeNews.POISTION, valuse);
        db.close();
    }
    // 添加markCorer表的记录
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
    // 添加Picture表的记录
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

    //删除Poistion表的记录
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

    //查询QoistionBean全部类型
    public ArrayList<QoistionBean> queryPoistion() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        //是否去重复记录，表名，要查询的列，查询条件，查询条件的值，分组条件，分组条件的值，排序，分页条件
        Cursor c = db.query(DateSheet.poistion.TABLE_NAME, null, null , null, null, null, DateSheet.poistion._ID + " DESC");
        ArrayList<QoistionBean> qoistionBeans = new ArrayList();
        QoistionBean qoistionBean = null;
        while (c.moveToNext()) {
            qoistionBean = new QoistionBean();
            qoistionBean.setPoistion_x(c.getString(c.getColumnIndexOrThrow(DateSheet.poistion.POISTION_X)));
            qoistionBean.setPoistion_y(c.getString(c.getColumnIndexOrThrow(DateSheet.poistion.POISTION_Y)));
            qoistionBeans.add(qoistionBean);
        }
        c.close();
        db.close();
        return qoistionBeans;
    }
    //查询RouteNews全部类型
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
