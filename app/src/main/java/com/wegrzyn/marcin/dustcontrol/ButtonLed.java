package com.wegrzyn.marcin.dustcontrol;

import android.util.Log;
import android.util.MutableBoolean;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

/**
 * Created by wirea on 05.10.2017.
 */

public class ButtonLed {

    private Gpio gpioLed;
    private static final String TAG = ButtonLed.class.getSimpleName();
    static final String LedRed = "GPIO_34";
    static final String LedGreen = "GPIO_32";
    static final String LedBlue = "GPIO_37";


    public ButtonLed(String selectGpioLed) {
        try {
            PeripheralManagerService peripheralManagerService = new PeripheralManagerService();
            gpioLed = peripheralManagerService.openGpio(selectGpioLed);
            gpioLed.setEdgeTriggerType(Gpio.EDGE_NONE);
            gpioLed.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            gpioLed.setActiveType(Gpio.ACTIVE_HIGH);

        } catch (IOException e) {
            Log.e(TAG, "Error open Led", e);
        }
    }


    public void setLed(boolean light){
        try {
            gpioLed.setValue(light);
        } catch (IOException e) {
            Log.e(TAG,"Error set state led"+e);
        }
    }

    public void close() {
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
