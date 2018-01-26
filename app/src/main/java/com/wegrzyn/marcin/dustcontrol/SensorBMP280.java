package com.wegrzyn.marcin.dustcontrol;

import android.util.Log;

import com.google.android.things.contrib.driver.bmx280.Bmx280;

import java.io.IOException;


/**
 * Created by wirea on 22.01.2018.
 */

class SensorBMP280 {

    private final static String TAG = SensorBMP280.class.getSimpleName();
    private final static String DEVI2CBUS1 = "I2C1";
    private Bmx280 bmx280;

    SensorBMP280() {
        try {
            bmx280 = new Bmx280(DEVI2CBUS1);
            bmx280.setMode(Bmx280.MODE_NORMAL);
            bmx280.setTemperatureOversampling(Bmx280.OVERSAMPLING_1X);
            bmx280.setPressureOversampling(Bmx280.OVERSAMPLING_1X);

        } catch (IOException e) {
            Log.e(TAG, "Error open bmp280: " + e);
        }
    }

    float readTemp() {
        try {
            return bmx280.readTemperature();
        } catch (IOException e) {
            Log.e(TAG, "Error Read Temp: " + e);
            return 0;
        }
    }

    float readPress() {
        try {
            return bmx280.readPressure();
        } catch (IOException e) {
            Log.e(TAG, "Error Read Press: " + e);
            return 0;
        }
    }

    void close() {
        try {
            bmx280.close();
        } catch (IOException e) {
            Log.e(TAG, "Error close bmp280: " + e);
        }
    }

}
