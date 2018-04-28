package com.example.renshaole.testarcgis.bean;

/**
 * Created by admin on 2018/4/11.
 */

public class RouteNewsBean {
    public long id;
    public String poistion;
    public String startTime;
    public  String route_type;      //路线方案类型

    public RouteNewsBean() {
    }

    public String getRoute_type() {
        return route_type;
    }

    public void setRoute_type(String route_type) {
        this.route_type = route_type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPoistion() {
        return poistion;
    }

    public void setPoistion(String poistion) {
        this.poistion = poistion;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
