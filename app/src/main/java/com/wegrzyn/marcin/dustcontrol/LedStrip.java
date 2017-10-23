package com.wegrzyn.marcin.dustcontrol;

import android.util.Log;

import com.google.android.things.contrib.driver.apa102.Apa102;

import java.io.IOException;

/**
 * Created by wirea on 07.10.2017.
 */

public class LedStrip {

    private static final String TAG = LedStrip.class.getSimpleName();
    private Apa102 apa102;
    private int[] strip = new int[7];
    private static final int LEDSTRIP_BRIGHTNESS = 1;
    private static final String SpiName = "SPI3.1";

    public LedStrip() {

        for (int i =0 ; i< strip.length; i++)
            strip[i] = 0;
        try {
            apa102 = new Apa102(SpiName, Apa102.Mode.BGR);
            apa102.setBrightness(LEDSTRIP_BRIGHTNESS);
        } catch (IOException e) {
            apa102 = null; // Led strip is optional.
            Log.e(TAG,"Error open apa102"+e);
        }
    }
    public void setColor(int[] colorlist){
        strip = colorlist;
    }

    public void setOneColor(int oneColor){
        for (int i =0 ; i < strip.length ; i++){
            strip[i] = oneColor;
        }
        show();
    }
    public void modOneColor(int id, int color){
        strip[id] = color;
        show();
    }

    public void show(){
        try {
            apa102.write(strip);
        } catch (IOException e) {
            Log.e(TAG, "Error setting ledstrip", e);
        }
    }

    public void close(){
        if (apa102 != null) {
            try {
                apa102.setBrightness(0);
                apa102.write(new int[7]);
                apa102.close();
            } catch (IOException e) {
                Log.e(TAG, "Error disabling ledstrip", e);
            } finally {
                apa102 = null;
            }
        }
    }

}
