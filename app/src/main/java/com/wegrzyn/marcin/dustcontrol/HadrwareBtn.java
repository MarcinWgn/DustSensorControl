package com.wegrzyn.marcin.dustcontrol;

import android.util.Log;

import com.google.android.things.contrib.driver.button.ButtonInputDriver;

import java.io.IOException;

import static com.google.android.things.contrib.driver.button.Button.LogicState.PRESSED_WHEN_LOW;

/**
 * Created by wirea on 07.10.2017.
 */

public class HadrwareBtn {

    private ButtonInputDriver mButtonInputDriver;
    static final String TouchA = "GPIO6_IO14";
    static final String TouchB = "GPIO6_IO15";
    static final String TouchC = "GPIO2_IO07";
    private static final String TAG = HadrwareBtn.class.getSimpleName();


    HadrwareBtn(String port, int keycode) {

        try {
            mButtonInputDriver = new ButtonInputDriver(port,
                    PRESSED_WHEN_LOW, keycode);
            mButtonInputDriver.register();

        } catch (IOException e) {
            throw new RuntimeException("Error initializing GPIO button", e);
        }

    }

    void close() {

        if (mButtonInputDriver != null) {
            mButtonInputDriver.unregister();
            try {
                mButtonInputDriver.close();
            } catch (IOException e) {
                Log.e(TAG, "Error close driver");
            } finally {
                mButtonInputDriver = null;
            }
        }
    }

}
