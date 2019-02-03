package com.wegrzyn.marcin.dustcontrol;

import java.util.Date;

/**
 * Created by wirea on 22.10.2017.
 */

public class SensorData {

    private long PosixTime;
    private float PM2;
    private float PM10;
    private float Temp;
    private float Press;


    public SensorData(float PM2, float PM10, float Press, float Temp, long PosixTime) {
        this.PM2 = PM2;
        this.PM10 = PM10;
        this.Press = Press;
        this.Temp = Temp;
        this.PosixTime = PosixTime;
    }

    SensorData() {
        PM10 = 0;
        PM2 = 0;
        Press = 0;
        Temp = 0;
    }

    float getPM2() {
        return PM2;
    }

    float getPM10() {
        return PM10;
    }

    float getTemp() {
        return Temp;
    }

    float getPress() {
        return Press;
    }

    long getPosixTime() {return PosixTime; }

    void setPosixTime(long posixTime) { PosixTime = posixTime; }

    void setPM2(float PM2) {
        this.PM2 = PM2;
    }

    void setPM10(float PM10) {
        this.PM10 = PM10;
    }

    void setTemp(float temp) {
        Temp = temp;
    }

    void setPress(float press) {
        Press = press;
    }



    @Override
    public String toString() {
        return "PM 2.5: " +
                String.valueOf(PM2) +
                "\n" +
                "PM 10: " +
                String.valueOf(PM10) +
                "\n" +
                "Temp: " +
                String.valueOf(Temp) +
                "\n" +
                "Press: " +
                String.valueOf(Press) +
                "\n" +
                new Date(PosixTime).toString();
    }
}
