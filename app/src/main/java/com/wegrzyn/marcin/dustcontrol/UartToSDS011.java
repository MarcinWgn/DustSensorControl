package com.wegrzyn.marcin.dustcontrol;

import android.util.Log;

import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.UartDevice;
import com.google.android.things.pio.UartDeviceCallback;

import java.io.IOException;
import java.util.List;

/**
 * Created by wirea on 23.10.2017.
 */

public abstract class UartToSDS011 {

    private final static String TAG = UartToSDS011.class.getSimpleName();

    private UartDevice uartDevice;
    private PeripheralManager manager;
    private UartDeviceCallback uartDeviceCallback;

    UartToSDS011(){
        manager = PeripheralManager.getInstance();
        List<String> deviceList = manager.getUartDeviceList();
        if (deviceList.isEmpty()) {
            Log.i(TAG, "No UART port available on this device.");
        } else {
            Log.i(TAG, "List of available devices: " + deviceList);
        }

        try {
            uartDevice = manager.openUartDevice(deviceList.get(0));
            configureUartFrame(uartDevice);
        } catch (IOException e) {
            Log.w(TAG, "Unable to access UART device", e);
        }

        uartDeviceCallback = new UartDeviceCallback() {
            @Override
            public boolean onUartDeviceDataAvailable(UartDevice uartDevice) {
                Log.d(TAG,"jakies dane" );
                try {
                    readUartBuffer(uartDevice);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        };
        try {
            uartDevice.registerUartDeviceCallback(uartDeviceCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readUartBuffer(UartDevice uart) throws IOException {

        final int maxCount = 10;
        byte[] buffer = new byte[maxCount];

        int count;
        while ((count = uart.read(buffer, buffer.length)) > 0) {
            updateBuffer(buffer);
            Log.d(TAG," count: "+ String.valueOf(count));
        }
        Log.d(TAG," count: "+ String.valueOf(count));
    }

    private void open(String name) {
        try {
            uartDevice = manager.openUartDevice(name);
            configureUartFrame(uartDevice);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public abstract void updateBuffer(byte[] bytes);


     static void configureUartFrame(UartDevice uart) throws IOException {
        // Configure the UART port
        uart.setBaudrate(9600);
        uart.setDataSize(8);
        uart.setParity(UartDevice.PARITY_NONE);
        uart.setStopBits(1);
    }

    void writeData(byte[] bytes) {
        byte[] buffer = new byte[1024];
        buffer = bytes;
        int count = 0;
        try {
            count = uartDevice.write(buffer, buffer.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Wrote " + count + " bytes to peripheral");
    }

    void unregisterCallback()
    {
        uartDevice.unregisterUartDeviceCallback(uartDeviceCallback);
        Log.d(TAG, "Unregister callback");
    }

    void close() {
        if (uartDevice != null) {
            try {
                uartDevice.close();
                uartDevice = null;
            } catch (IOException e) {
                Log.w(TAG, "Unable to close UART device", e);
            }
        }
    }
}
