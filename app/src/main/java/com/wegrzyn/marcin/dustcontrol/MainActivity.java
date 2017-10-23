package com.wegrzyn.marcin.dustcontrol;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.things.pio.UartDevice;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DisplHt16k33 displHt16k33;

    private ButtonLed ledA;
    private ButtonLed ledB;
    private ButtonLed ledC;

    private LedStrip ledStrip;

    private HadrwareBtn btnC;
    private HadrwareBtn btnB;
    private HadrwareBtn btnA;

    UartToSDS011 uartToSDS011;

    SensorSDS011 sds011;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        displHt16k33 = new DisplHt16k33(1);
        displHt16k33.display("Test");

        ledA = new ButtonLed(ButtonLed.LedRed);
        ledA.setLed(false);

        ledB = new ButtonLed(ButtonLed.LedGreen);
        ledB.setLed(false);

        ledC = new ButtonLed(ButtonLed.LedBlue);
        ledC.setLed(false);

        ledStrip = new LedStrip();
        ledStrip.setOneColor(Color.BLACK);

        btnA = new HadrwareBtn(HadrwareBtn.TouchA, KeyEvent.KEYCODE_A);
        btnB = new HadrwareBtn(HadrwareBtn.TouchB, KeyEvent.KEYCODE_B);
        btnC = new HadrwareBtn(HadrwareBtn.TouchC, KeyEvent.KEYCODE_C);

        sds011 = new SensorSDS011();

        uartToSDS011 = new UartToSDS011() {
            @Override
            public void updateBuffer(byte[] bytes) {
                Log.d(TAG," data: "+ sds011.readData(bytes));
                Log.d(TAG," pm2.5: "+ sds011.readPM2(bytes));
                Log.d(TAG," pm10: "+ sds011.readPM10(bytes));
                Log.d(TAG, new Date().toString());
            }
        };
    }



    @Override
    protected void onStart() {
        uartToSDS011.registerCallback();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        uartToSDS011.unregisterCallback();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_A:
                ledA.setLed(true);
                displHt16k33.display(sds011.getSds011Data().getPM25str());
                return true;
            case KeyEvent.KEYCODE_B:
                ledB.setLed(true);
                displHt16k33.display(sds011.getSds011Data().getPM10str());
                return true;
            case KeyEvent.KEYCODE_C:
                ledC.setLed(true);
                displHt16k33.clear();
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_A:
                ledA.setLed(false);
                return true;
            case KeyEvent.KEYCODE_B:
                ledB.setLed(false);
                return true;
            case KeyEvent.KEYCODE_C:
                ledC.setLed(false);
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        displHt16k33.close();

        ledA.close();
        ledB.close();
        ledC.close();

        ledStrip.close();

        btnA.close();
        btnB.close();
        btnC.close();

        uartToSDS011.close();
    }

}
