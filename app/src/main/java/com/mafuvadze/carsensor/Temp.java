package com.mafuvadze.carsensor;

import android.app.Dialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.IBinder;
import android.widget.ListView;
import android.widget.Toast;

import java.util.UUID;

public class Temp extends Service
{
    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    int device_index;
    public Temp() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        device_index = intent.getExtras().getInt("index");
        Thread connectThread = new Thread(new Connect());
        return START_STICKY;
    }

    class Connect implements Runnable {

        @Override
        public void run() {

        }
    }

}
