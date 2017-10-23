package com.wegrzyn.marcin.dustcontrol;

import com.google.android.things.pio.UartDeviceCallback;

import java.sql.Time;

/**
 * Created by wirea on 22.10.2017.
 */

public class SensorSDS011 {

    private SDS011Data sds011Data;

    public SensorSDS011() {
        sds011Data = new SDS011Data();
    }

     String readPM2(byte[]bytes){
        int[] data = toUnsigned(bytes);
        String s = "";
        float i = ((data[3]<<8)+data[2]);
        float o = i/10;
        sds011Data.setPM25str(String.valueOf(o));
        return sds011Data.getPM25str();
    }

     String readPM10(byte[]bytes){
        int[] data = toUnsigned(bytes);
        String s = "";
        float i = ((data[5]<<8) + data[4]);
        float o = i/10;
        sds011Data.setPM10str(String.valueOf(o));
        return sds011Data.getPM10str();
    }

    private int[] toUnsigned(byte[] bytes){
        int[] data = new int[10];
        for (int i =0 ; i<bytes.length;i++){
            data[i] = Byte.toUnsignedInt(bytes[i]);
        }
        return data;
    }

    public SDS011Data getSds011Data() {
        return sds011Data;
    }

    String readData(byte[] bytes){
        int[] data = toUnsigned(bytes);
        String out = "";

        for(int i = 0 ; i<bytes.length ; i++){
            out +=" "+Integer.toHexString(data[i]);
        }
        return out;
    }

}
