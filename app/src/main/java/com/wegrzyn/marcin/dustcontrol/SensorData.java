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

    SensorData() {
        PM10 = 0;
        PM2 = 0;
    }

    float getPM2() {
        return PM2;
    }

    float getPM10() {
        return PM10;
    }

    Date getDate() {
        return date;
    }

    void setPM2(float PM2) {
        this.PM2 = PM2;
    }

    void setPM10(float PM10) {
        this.PM10 = PM10;
    }

    void setDate(Date date) {
        this.date = date;
    }
}
