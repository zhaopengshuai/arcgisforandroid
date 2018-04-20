package com.example.renshaole.testarcgis.bean;

/**
 * Created by admin on 2018/4/13.
 */

public class MarkCorerBean {
    public String state;
    public String type;
    public double poistion_x;
    public  double poistion_y;

    public MarkCorerBean() {
    }

    public MarkCorerBean(String state, String type, double poistion_x, double poistion_y) {
        this.state = state;
        this.type = type;
        this.poistion_x = poistion_x;
        this.poistion_y = poistion_y;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public double getPoistion_x() {
        return poistion_x;
    }

    public void setPoistion_x(double poistion_x) {
        this.poistion_x = poistion_x;
    }

    public double getPoistion_y() {
        return poistion_y;
    }

    public void setPoistion_y(double poistion_y) {
        this.poistion_y = poistion_y;
    }



}
