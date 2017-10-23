package com.wegrzyn.marcin.dustcontrol;

import java.util.Date;

/**
 * Created by wirea on 22.10.2017.
 */

public class SDS011Data {

    private String PM25str;
    private String PM10str;
    private Date date;

    public SDS011Data(String PM25str, String PM10str, Date date) {
        this.PM25str = PM25str;
        this.PM10str = PM10str;
        this.date = date;
    }

    public SDS011Data() {
        PM10str= "---";
        PM25str= "---";
    }

    public String getPM25str() {
        return PM25str;
    }

    public String getPM10str() {
        return PM10str;
    }

    public Date getDate() {return date; }

    public void setPM25str(String PM25str) {
        this.PM25str = PM25str;
    }

    public void setPM10str(String PM10str) {
        this.PM10str = PM10str;
    }

    public void setDate(Date date) {this.date = date; }
}
