package com.example.renshaole.testarcgis.bean;

/**
 * Created by admin on 2018/4/23.
 */

public class SituationBean {
    public  String route_type;      //路线方案类型
    public  String starttime;  //态势开始时间
    public  String endtime;  //态势结束时间
    public  String times;    // 接受态势信息时间

    public SituationBean() {

    }

    public String getRoute_type() {
        return route_type;
    }

    public void setRoute_type(String route_type) {
        this.route_type = route_type;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
