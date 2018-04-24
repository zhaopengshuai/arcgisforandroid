package com.example.renshaole.testarcgis.helper;

import android.provider.BaseColumns;

/**
 * 描    述：数据库表
 * 作    者：ZhaoPengshuai
 * 时    间：2018/04/11 0001
 */

public  final  class DateSheet {
    private DateSheet(){}

    //轨迹经纬度
    public  static  abstract class poistion implements BaseColumns {
        public static  final String TABLE_NAME="poistion";
        public static  final String POISTION_X="poistion_x";
        public static  final String POISTION_Y="poistion_y";
        public static  final String ROUTE_TYPE="route_type";      //路线方案类型
        public static  final String POISTION_TIME="poistion_time";
    }

    //机动路线信息
    public static abstract class routeNews implements BaseColumns {
        public static  final String TABLE_NAME="routeNews";
        public static  final String POISTION="poistion";
        public static  final String STARTTIME="startTime";
        public static  final String ROUTE_TYPE="route_type";      //路线方案类型
    }

    //障碍物坐标
    public static abstract class markCorer implements BaseColumns {
        public static  final String TABLE_NAME="markCorer";
        public static  final String STATE="state";
        public static  final String TYPE="type";
        public static  final String POISTION_X="poistion_x";
        public static  final String POISTION_Y="poistion_y";
    }
    //资源表
    public static abstract class picture implements BaseColumns {
        public static  final String TABLE_NAME="picture";
        public static  final String TYPE="type";
        public static  final String IMGPATH="img_path";
        public static  final String IMGNAME="img_name";
    }
    //态势表
    public static abstract class situation implements BaseColumns {
        public static  final String TABLE_NAME="situation";  //表名
        public static  final String ROUTE_TYPE="route_type";      //路线方案类型
        public static  final String STARTTIME="starttime";  //态势开始时间
        public static  final String ENDTIME="endtime";  //态势结束时间
        public static  final String TIME="time";    // 接受态势信息时间
    }
    //所有人员位置信息表
    public static abstract class staffposition implements BaseColumns {
        public static  final String TABLE_NAME="staffposition"; //表名
        public static  final String ROUTE_TYPE="route_type";  //路线方案类型
        public static  final String USERTYPE="user_type";   //用户类型（红方，蓝方）
        public static  final String USERID="user_id";   //用户唯一标识
        public static  final String TIME="time";    //接受信息时间
        public static  final String POISTION_X="poistion_x";
        public static  final String POISTION_Y="poistion_y";
    }

}
