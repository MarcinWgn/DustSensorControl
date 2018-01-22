package com.wegrzyn.marcin.dustcontrol;

import java.util.Date;

/**
 * Created by wirea on 22.10.2017.
 */

public class SensorSDS011 {

    public static byte[] sleep = {(byte) 0xAA, (byte) 0xB4, 0x06, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x05, (byte) 0xAB};
    public static byte[] work = {(byte) 0xAA, (byte) 0xB4, 0x06, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x06, (byte) 0xAB};
    public static byte[] continious = {(byte) 0xAA, (byte) 0xB4, 0x08, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x07, (byte) 0xAB};
    public static byte[] query = {(byte) 0xAA, (byte) 0xB4, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x00, (byte) 0xAB};
    public static byte[] cycle1min = {(byte) 0xAA, (byte) 0xB4, 0x08, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x08, (byte) 0xAB};
    public static byte[] cycle10min = {(byte) 0xAA, (byte) 0xB4, 0x08, 0x01, 0x0A, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x11, (byte) 0xAB};
    public static byte[] cycle20min = {(byte) 0xAA, (byte) 0xB4, 0x08, 0x01, 0x14, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x1B, (byte) 0xAB};
    public static byte[] cycle30min = {(byte) 0xAA, (byte) 0xB4, 0x08, 0x01, 0x1E, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x25, (byte) 0xAB};

    private SensorData sensorData;


    private static byte checkSum(byte[] bytes) {
        byte sum = 0;
        for (int i = 2; i < bytes.length - 2; i++) {
            sum += Byte.toUnsignedInt(bytes[i]);
        }
        return (byte) sum;
    }


    SensorSDS011() {
        sensorData = new SensorData();
        sensorData.setDate(new Date());
    }

    float readPM2(byte[] bytes) {
        int[] data = toUnsigned(bytes);
        String s = "";
        float i = ((data[3] << 8) + data[2]);
        float o = i / 10;
        sensorData.setPM2(o);
        return sensorData.getPM2();
    }

    float readPM10(byte[] bytes) {
        int[] data = toUnsigned(bytes);
        String s = "";
        float i = ((data[5] << 8) + data[4]);
        float o = i / 10;
        sensorData.setPM10(o);
        return sensorData.getPM10();
    }

    private int[] toUnsigned(byte[] bytes) {
        int[] data = new int[10];
        for (int i = 0; i < bytes.length; i++) {
            data[i] = Byte.toUnsignedInt(bytes[i]);
        }
        return data;
    }

    SensorData getSensorData() {
        return sensorData;
    }

    String readData(byte[] bytes) {
        int[] data = toUnsigned(bytes);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            builder.append(Integer.toHexString(data[i]));
        }
        return builder.toString();
    }

    boolean isPmData(byte[] bytes) {
        return Byte.toUnsignedInt(bytes[1]) == 0xC0;
    }

    boolean isResponse(byte[] bytes) {
        return Byte.toUnsignedInt(bytes[1]) == 0xC5;
    }

    int getPeriodInfo(byte[] bytes) {
        return Byte.toUnsignedInt(bytes[4]);
    }

}
