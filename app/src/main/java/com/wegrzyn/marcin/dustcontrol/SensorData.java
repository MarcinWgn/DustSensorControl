package com.wegrzyn.marcin.dustcontrol;

import java.util.Date;

/**
 * Created by wirea on 22.10.2017.
 */

public class SensorData {

    private float PM2;
    private float PM10;
    private Date date;

    public SensorData(float PM2, float PM10, Date date) {
        this.PM2 = PM2;
        this.PM10 = PM10;
        this.date = date;
    }

    public SensorData() {
        PM10 = 0;
        PM2 = 0;
    }

    public float getPM2() {
        return PM2;
    }

    public float getPM10() {
        return PM10;
    }

    public Date getDate() {
        return date;
    }

    public void setPM2(float PM2) {
        this.PM2 = PM2;
    }

    public void setPM10(float PM10) {
        this.PM10 = PM10;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
