package com.example.renshaole.testarcgis.bean;

import com.esri.core.geometry.Point;

/**
 * Created by admin on 2018/4/23.
 */

public class StaffPositionBean {
    public  String route_type;  //路线方案类型
    public  String userType;   //用户类型（红方，蓝方）
    public  String userID;   //用户唯一标识
    public  String times;    //接受信息时间
    public  String poistion_x;
    public  String poistion_y;
    public Point locationPoint;
    public StaffPositionBean() {
    }

    public Point getLocationPoint() {
        return locationPoint;
    }

    public void setLocationPoint(Point locationPoint) {
        this.locationPoint = locationPoint;
    }

    public String getRoute_type() {
        return route_type;
    }

    public void setRoute_type(String route_type) {
        this.route_type = route_type;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getPoistion_x() {
        return poistion_x;
    }

    public void setPoistion_x(String poistion_x) {
        this.poistion_x = poistion_x;
    }

    public String getPoistion_y() {
        return poistion_y;
    }

    public void setPoistion_y(String poistion_y) {
        this.poistion_y = poistion_y;
    }
}
