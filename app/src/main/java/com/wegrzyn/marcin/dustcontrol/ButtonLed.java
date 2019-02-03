package com.wegrzyn.marcin.dustcontrol;

import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;

/**
 * Created by wirea on 05.10.2017.
 */

class ButtonLed {

    private Gpio gpioLed;
    private static final String TAG = ButtonLed.class.getSimpleName();

    static final String LedRed = "GPIO2_IO02";
    static final String LedGreen = "GPIO2_IO00";
    static final String LedBlue = "GPIO2_IO05";

    static final int LedA = 1;
    static final int LedB = 2;
    static final int LedC = 3;



    ButtonLed(String selectGpioLed) {
        try {

            PeripheralManager peripheralManager = PeripheralManager.getInstance();

            gpioLed = peripheralManager.openGpio(selectGpioLed);
            gpioLed.setEdgeTriggerType(Gpio.EDGE_NONE);
            gpioLed.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            gpioLed.setActiveType(Gpio.ACTIVE_HIGH);

        } catch (IOException e) {
            Log.e(TAG, "Error open Led", e);
        }
    }


    void setLed(boolean light){
        try {
            gpioLed.setValue(light);
        } catch (IOException e) {
            Log.e(TAG,"Error set state led"+e);
        }
    }

    void close() {
        if (gpioLed != null) {
            try {
                gpioLed.setValue(false);
                gpioLed.close();
            } catch (IOException e) {
                Log.e(TAG, "Error disabling led", e);
            } finally {
                gpioLed = null;
            }
        }
    }

}
