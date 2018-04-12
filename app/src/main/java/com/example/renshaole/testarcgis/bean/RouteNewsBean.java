package com.example.renshaole.testarcgis.bean;

/**
 * Created by admin on 2018/4/11.
 */

public class RouteNewsBean {
    public long id;
    public String poistion;
    public String startTime;

    public RouteNewsBean() {
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
