package com.example.renshaole.testarcgis.helper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 描    述：数据库帮助类
 * 作    者：ZhaoPengshuai
 * 时    间：2018/04/11 0001
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "news.db";
    private static final int VERSION = 1;   //版本号
    private static final String CREATE_TABLE_POISTION = "CREATE TABLE poistion(_id INTEGER PRIMARY KEY AUTOINCREMENT," + "poistion_x TEXT,poistion_y TEXT)";  //创建表
    private static final String CREATE_TABLE_ROUTENEWS= "CREATE TABLE routeNews(_id INTEGER PRIMARY KEY AUTOINCREMENT," +"poistion TEXT,startTime TEXT)";  //创建表

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    //如果数据库不存在，那么会调用该方法
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //SQLiteDatabase 用于操作数据库的工具类
        sqLiteDatabase.execSQL(CREATE_TABLE_POISTION);
        sqLiteDatabase.execSQL(CREATE_TABLE_ROUTENEWS);

    }

    //数据库升级
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //更新列
    public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn){

    }
}
