package com.wegrzyn.marcin.dustcontrol;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.things.pio.UartDevice;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;


public class MainActivity extends Activity {

    public static final String SENSOR_DATA = "SensorData";
    private static final String TAG = MainActivity.class.getSimpleName();
    private DisplHt16k33 displHt16k33;

    private ButtonLed ledA;
    private ButtonLed ledB;
    private ButtonLed ledC;

    private LedStrip ledStrip;

    private HadrwareBtn btnC;
    private HadrwareBtn btnB;
    private HadrwareBtn btnA;

    private SensorBMP280 bmp280;
    private SensorData sensorData;
    private UartToSDS011 uartToSDS011;
    private SensorSDS011 sds011;

    private TextView PM2;
    private TextView PM10;
    private TextView mode;
    private TextView time;
    private TextView press;
    private TextView temp;

    private DatabaseReference databaseReference;
    private static FirebaseDatabase firebaseDatabase;

    //    true display PM10 false Pm2.5
    private boolean whatDsp = true;

    //    workMode 0==sleep 1-30 == time period
    private int workMode;

    private int selectMode = 1;


//    ***************************************************************

    SeekBar seekBar;
    int brightness = 155;



//    ***************************************************************

    UartDevice mDevice = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActionBar().hide();

        PM2 = findViewById(R.id.TV_PM2);
        PM10 = findViewById(R.id.TV_PM10);
        mode = findViewById(R.id.TV_mode);
        time = findViewById(R.id.TV_time_mode);
        press = findViewById(R.id.TV_press);
        temp = findViewById(R.id.TV_temp);

        mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uartToSDS011.writeData(SensorSDS011.sleep);
                ledB.setLed(true);

                Log.d(TAG, "click mode");
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMode();
                ledC.setLed(true);

                Log.d(TAG, "select mode");
            }
        });

        setBrightness();
        init();

//        Set period measure time sds sensor
        uartToSDS011.writeData(SensorSDS011.cycle30min);


    }


    private void setBrightness() {


        seekBar = findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void init() {

        if(firebaseDatabase==null){
            firebaseDatabase= FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
        }

        databaseReference = firebaseDatabase.getReference(SENSOR_DATA);

        displHt16k33 = new DisplHt16k33(1);
        displHt16k33.display(getString(R.string.wait));

        ledA = new ButtonLed(ButtonLed.LedRed);
        if (!whatDsp) ledA.setLed(true);
        else ledA.setLed(false);

        ledB = new ButtonLed(ButtonLed.LedGreen);
        if (whatDsp) ledB.setLed(true);
        else ledB.setLed(false);

        ledC = new ButtonLed(ButtonLed.LedBlue);
        ledC.setLed(false);

        ledStrip = new LedStrip();
        ledStrip.setOneColor(Color.BLACK);

        btnA = new HadrwareBtn(HadrwareBtn.TouchA, KeyEvent.KEYCODE_A);
        btnB = new HadrwareBtn(HadrwareBtn.TouchB, KeyEvent.KEYCODE_B);
        btnC = new HadrwareBtn(HadrwareBtn.TouchC, KeyEvent.KEYCODE_C);

        bmp280 = new SensorBMP280();
        sds011 = new SensorSDS011();
        sensorData = new SensorData();

        updateUart();
    }

    private void updateUart() {
        uartToSDS011 = new UartToSDS011() {

            @Override
            public void updateBuffer(byte[] bytes) {


                Log.d(TAG, "SDS011_data_read=" + sds011.readData(bytes));

                if (sds011.isPmData(bytes)) {

                    sensorData.setPM2(sds011.readPM2(bytes));
                    sensorData.setPM10(sds011.readPM10(bytes));
                    sensorData.setPress(bmp280.readPress());
                    sensorData.setTemp(bmp280.readTemp());
                    sensorData.setPosixTime(new Date().getTime());
                    
                    Log.d(TAG, sensorData.toString());

                    pushData(sensorData);

                    displayPM();
                    displayBigScr();

                } else if (sds011.isResponse(bytes)) {
                    Log.d(TAG, "SDS011_workmode: " +
                            String.valueOf(workMode = sds011.getPeriodInfo(bytes)));
                    if (workMode == 0) {
                        displHt16k33.display("SLP");
                        mode.setText("sleep");
                    } else if (workMode == 1) {
                        displHt16k33.display("WORK");
                        mode.setText("work");
                    } else {
                        displHt16k33.display("TM" + workMode);
                        time.setText("time "+ String.valueOf(workMode));
                    }
                }
            }
        };
    }

    /**
     * Push sensor data to Firebase
     *
     * @param sensorData
     */
    private void pushData(SensorData sensorData) {
        databaseReference.push().setValue(sensorData);
    }

    @Override
    protected void onStop() {
        super.onStop();
       uartToSDS011.unregisterCallback();
    }

    private void displayPM() {
        if (whatDsp) {
            displHt16k33.display(String.valueOf(sensorData.getPM10()));
            ledA.setLed(false);
            ledB.setLed(true);
        } else {
            displHt16k33.display(String.valueOf(sensorData.getPM2()));
            ledA.setLed(true);
            ledB.setLed(false);
        }
    }

    private void displayBigScr(){
        PM10.setText(String.valueOf(sensorData.getPM10()));
        PM10.setTextColor(Utils.setPm10Color(this,sensorData.getPM10()));
        PM2.setText(String.valueOf(sensorData.getPM2()));
        PM2.setTextColor(Utils.setPm2Color(this,sensorData.getPM2()));
        press.setText(Utils.numberFormat(sensorData.getPress()));
        temp.setText(Utils.numberFormat(sensorData.getTemp()));
    }

    private void selectMode() {
        if (selectMode < 4) {
            selectMode++;
        } else selectMode = 1;

        if (selectMode == 1) uartToSDS011.writeData(SensorSDS011.cycle1min);
        else if (selectMode == 2) uartToSDS011.writeData(SensorSDS011.cycle10min);
        else if (selectMode == 3) uartToSDS011.writeData(SensorSDS011.cycle20min);
        else if (selectMode == 4) uartToSDS011.writeData(SensorSDS011.cycle30min);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_A:
                whatDsp = !whatDsp;
                displayPM();
                return true;
            case KeyEvent.KEYCODE_B:
                uartToSDS011.writeData(SensorSDS011.sleep);
                ledB.setLed(true);
                return true;
            case KeyEvent.KEYCODE_C:
                selectMode();
                ledC.setLed(true);
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_A:
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

        bmp280.close();

        uartToSDS011.close();
    }

}
