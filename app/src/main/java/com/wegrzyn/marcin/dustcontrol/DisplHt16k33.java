package com.wegrzyn.marcin.dustcontrol;

import android.util.Log;

import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay;

import java.io.IOException;

/**
 * Created by wirea on 05.10.2017.
 */

public class DisplHt16k33 {

    private final static String TAG = DisplHt16k33.class.getSimpleName();
    private final String i2c_port = "I2C1";
    private AlphanumericDisplay alphanumericDisplay;

    public DisplHt16k33() {

        try {
            alphanumericDisplay = new AlphanumericDisplay(i2c_port);
            alphanumericDisplay.setBrightness(1);
            alphanumericDisplay.setEnabled(true);
        } catch (IOException e) {
            Log.e(TAG, "Error open i2c" + e);
        }
    }

    /**
     * @param brightness 1 - 15
     */
    DisplHt16k33(int brightness) {

        try {
            alphanumericDisplay = new AlphanumericDisplay(i2c_port);
            alphanumericDisplay.setEnabled(true);
            alphanumericDisplay.setBrightness(brightness);
        } catch (IOException e) {
            Log.e(TAG, "Error set brithness" + e);
        }
    }


    public void display(int data) {

        try {
            alphanumericDisplay.clear();
            alphanumericDisplay.display(data);

        } catch (IOException e) {
            Log.e(TAG, "Error conf display" + e);
        }

    }

    void display(String data) {

        try {
            alphanumericDisplay.clear();
            alphanumericDisplay.display(data);

        } catch (IOException e) {
            Log.e(TAG, "Error conf display" + e);
        }

    }

    public void clear() {
        try {
            alphanumericDisplay.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void close() {
        if (alphanumericDisplay != null) {
            try {
                alphanumericDisplay.close();
            } catch (IOException e) {
                Log.e(TAG, "Error close func");
            }
        }
    }

}
