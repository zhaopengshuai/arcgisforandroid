package com.example.renshaole.testarcgis.helper;

import android.provider.BaseColumns;

/**
 * 描    述：数据库表
 * 作    者：ZhaoPengshuai
 * 时    间：2018/04/11 0001
 */

public  final  class DateSheet {
    private DateSheet(){}

    //轨迹经纬度保存
    public  static  abstract class poistion implements BaseColumns {
        public static  final String TABLE_NAME="poistion";
        public static  final String POISTION_X="poistion_x";
        public static  final String POISTION_Y="poistion_y";
    }

    //机动路线信息保存
    public static abstract class routeNews implements BaseColumns {
        public static  final String TABLE_NAME="routeNews";
        public static  final String POISTION="poistion";
        public static  final String STARTTIME="startTime";
    }

}
