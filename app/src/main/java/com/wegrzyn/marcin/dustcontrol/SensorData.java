package com.wegrzyn.marcin.dustcontrol;

import java.util.Date;

/**
 * Created by wirea on 22.10.2017.
 */

public class SensorData {

    private float PM2;
    private float PM10;
    private float Temp;
    private float Press;
    private Date date;

    public SensorData(float PM2, float PM10, float Press, float Temp, Date date) {
        this.PM2 = PM2;
        this.PM10 = PM10;
        this.Press = Press;
        this.Temp = Temp;
        this.date = date;
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

    Date getDate() {
        return date;
    }

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

    void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        builder.append("PM 2.5: ");
        builder.append(String.valueOf(PM2));
        builder.append("\n");
        builder.append("PM 10: ");
        builder.append(String.valueOf(PM10));
        builder.append("\n");
        builder.append("Temp: ");
        builder.append(String.valueOf(Temp));
        builder.append("\n");
        builder.append("Press: ");
        builder.append(String.valueOf(Press));
        builder.append("\n");
        builder.append(date.toString());
        return builder.toString();
    }
}
