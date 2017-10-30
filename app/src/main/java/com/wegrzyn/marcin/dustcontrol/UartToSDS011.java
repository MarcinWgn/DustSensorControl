package com.wegrzyn.marcin.dustcontrol;

import android.util.Log;

import com.google.android.things.pio.PeripheralManagerService;
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
    private String uartName;
    private UartDeviceCallback deviceCallback;
    private PeripheralManagerService manager;
    private List<String> deviceList;


    public UartToSDS011() {
        manager = new PeripheralManagerService();
        deviceList = manager.getUartDeviceList();
        if (deviceList.isEmpty()) {
            Log.i(TAG, "No UART port available on this device.");
        } else {
            uartName = deviceList.get(0);
            open(uartName);
        }
        deviceCallback = new UartDeviceCallback() {
            @Override
            public boolean onUartDeviceDataAvailable(UartDevice uart) {
                try {
                    readUartBuffer(uart);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return super.onUartDeviceDataAvailable(uart);
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

    public void open(String name) {
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

    public void writeData(byte[] bytes) {
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

    public void registerCallback() {
        try {
            uartDevice.registerUartDeviceCallback(deviceCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unregisterCallback() {
        uartDevice.unregisterUartDeviceCallback(deviceCallback);
    }

    public void close() {
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
