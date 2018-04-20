package com.example.renshaole.testarcgis.bean;

/**
 * Created by admin on 2018/4/19.
 */

public class PictureBean {
    public    String types;
    public    String img_path;
    public    String img_name;
    public double poistion_x;
    public  double poistion_y;
    public PictureBean() {
    }

    public PictureBean(String types, String img_path, String img_name) {
        this.types = types;
        this.img_path = img_path;
        this.img_name = img_name;
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

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getImg_name() {
        return img_name;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
    }
}
