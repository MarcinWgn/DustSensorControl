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
    private UartDeviceCallback deviceCallback;
    private PeripheralManager manager;

    UartToSDS011() {
        manager = PeripheralManager.getInstance();
        List<String> deviceList = manager.getUartDeviceList();
             Log.d(TAG, "Device list: "+ deviceList.toString());
        if (deviceList.isEmpty()) {
            Log.i(TAG, "No UART port available on this device.");
        } else {
            String uartName = deviceList.get(0);
            open(uartName);
        }
        deviceCallback = new UartDeviceCallback() {
            @Override
            public boolean onUartDeviceDataAvailable(UartDevice uart) {
                try {
                    readUartBuffer(uart);
                    Log.i(TAG, "read buffer uart");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        };
    }

    private void readUartBuffer(UartDevice uart) throws IOException {

        final int maxCount = 10;
        byte[] buffer = new byte[maxCount];

        int count;
        while ((count = uart.read(buffer, buffer.length)) > 0) {
            updateBuffer(buffer);
        }
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


    private void configureUartFrame(UartDevice uart) throws IOException {
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

    void registerCallback() {
        try {
            uartDevice.registerUartDeviceCallback(deviceCallback);
            Log.d(TAG, "Register callback");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void unregisterCallback()
    {
        uartDevice.unregisterUartDeviceCallback(deviceCallback);
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
