package com.mafuvadze.carsensor;


import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, AdapterView.OnItemClickListener
{
    Typeface roboto_thin, roboto_bold, roboto_italic;
    TextView temp, name, battery, ulimit, llimit;
    TextView bat_txt,ulimit_txt, llimit_txt, police_txt;
    RelativeLayout popo;
    CheckBox policeCB;
    SeekBar llimitSeek, ulimitSeek;
    BluetoothAdapter btAdapter;
    Set<BluetoothDevice> pairedDevices;
    BluetoothDevice selectedDevice;
    List<BluetoothDevice> listDevices;
    Dialog dialog;
    BluetoothSocket bluetoothSocket;
    public static final int BT_REQUEST = 1;
    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();
        initListeners();
        initBT();
    }

    private void initListeners()
    {
        popo.setOnClickListener(this);
        ulimitSeek.setOnSeekBarChangeListener(this);
        llimitSeek.setOnSeekBarChangeListener(this);
    }

    private void initUi() {
        roboto_thin = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Thin.ttf");
        roboto_bold = Typeface.createFromAsset(getAssets(),"fonts/RobotoCondensed-Bold.ttf");
        roboto_italic = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Italic.ttf");

        temp = (TextView) findViewById(R.id.temp);
        name = (TextView) findViewById(R.id.name);
        battery = (TextView) findViewById(R.id.battery_txt);
        ulimit = (TextView) findViewById(R.id.ulimit);
        llimit = (TextView) findViewById(R.id.llimit);
        police_txt = (TextView) findViewById(R.id.police_txt);
        bat_txt = (TextView) findViewById(R.id.bat_txt);
        ulimit_txt = (TextView) findViewById(R.id.ulimit_txt);
        llimit_txt = (TextView) findViewById(R.id.llimit_txt);
        popo = (RelativeLayout) findViewById(R.id.call_police);
        llimitSeek = (SeekBar) findViewById(R.id.llimit_seek);
        ulimitSeek = (SeekBar) findViewById(R.id.ulimit_seek);
        policeCB = (CheckBox) findViewById(R.id.call_police_CB);


        temp.setTypeface(roboto_thin);
        name.setTypeface(roboto_bold);
        ulimit.setTypeface(roboto_thin);
        llimit.setTypeface(roboto_thin);
        battery.setTypeface(roboto_thin);
        police_txt.setTypeface(roboto_italic);
        ulimit_txt.setTypeface(roboto_italic);
        llimit_txt.setTypeface(roboto_italic);
        bat_txt.setTypeface(roboto_italic);
        ulimitSeek.setMax(120);
        llimitSeek.setMax(120);
        name.setText("no device connected");
        temp.setText("--");
        battery.setText("--%");
        ulimitSeek.setProgress(85);
        llimitSeek.setProgress(25);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.call_police:
                String msg = getString(R.string.call_police_info);
                displayDialog(msg);
        }
    }

    private void displayDialog(String msg)
    {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(msg)
                .setTitle("Call Police")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(true)
                .create();

        dialog.show();

    }

    private void showToast(String msg, int duration)
    {
        Toast.makeText(this, msg, duration).show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId())
        {
            case R.id.ulimit_seek:
                ulimit.setText(Integer.toString(progress) + "°");
                break;
            case R.id.llimit_seek:
                llimit.setText(Integer.toString(progress) + "°");
                break;
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void initBT()
    {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!btAdapter.isEnabled())
        {
            showToast("Bluetooth is not enabled", Toast.LENGTH_LONG);
        }
    }

    private void showDevices()
    {
        dialog = new Dialog(this);
        dialog.setTitle("paired Bluetooth devices");
        dialog.setContentView(R.layout.paired_devices);

        ListView devices = (ListView) dialog.findViewById(R.id.devices);

        List<BluetoothDevice> pDevices = new ArrayList<>();
        for(BluetoothDevice device : pairedDevices)
        {
            pDevices.add(device);
        }
        listDevices = pDevices;
        devices.setAdapter(new PairListAdapter(this, R.layout.single_bt_row, listDevices));
        dialog.show();
        devices.setOnItemClickListener(this);
    }

    private boolean isBTConnected(BluetoothAdapter btAdapter)
    {
        if(btAdapter == null)
        {
            showToast("Bluetooth is not available", Toast.LENGTH_LONG);
            return false;
        }
        else
        {
            if(!btAdapter.isEnabled())
            {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, BT_REQUEST);
            }
            else
            {
                pairedDevices = btAdapter.getBondedDevices();
            }
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == BT_REQUEST && resultCode == RESULT_OK)
        {
            showToast("Bluetooth successfully enabled", Toast.LENGTH_LONG);
            pairedDevices = btAdapter.getBondedDevices();
        }
        else
        {
            showToast("Bluetooth could not be enabled", Toast.LENGTH_LONG);
        }
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.BTdevices)
        {
            if(btAdapter.isEnabled() && isBTConnected(btAdapter))
            {
                showDevices();
            }
            else
            {
                showToast("Bluetooth is not enabled", Toast.LENGTH_LONG);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedDevice = listDevices.get(position);
        name.setText(selectedDevice.getName());
        dialog.dismiss();
    }

    class PairListAdapter extends ArrayAdapter
    {
        List<BluetoothDevice> devices;
        int resource;
        public PairListAdapter(Context context, int resource, List<BluetoothDevice> devices) {
            super(context, resource);
            this.resource = resource;
            this.devices = devices;
        }

        @Override
        public int getCount() {
            return devices.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
            {
                LayoutInflater inflater = LayoutInflater.from(getBaseContext());
                convertView = inflater.inflate(resource, parent, false);
            }

            BluetoothDevice device = devices.get(position);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView ip = (TextView) convertView.findViewById(R.id.ip);

            name.setText(device.getName());
            ip.setText(device.getAddress());

            return convertView;
        }
    }

    class Connection extends AsyncTask
    {
        DataInputStream inputStream;
        @Override
        protected Object doInBackground(Object[] params) {
            BluetoothSocket bluetoothSocket = null;
            try
            {
                bluetoothSocket = selectedDevice.createRfcommSocketToServiceRecord(applicationUUID);
            } catch (IOException e)
            {
                Log.d("CONNECTTHREAD", "Could not create RFCOMM socket:" + e.toString());
                return null;
            }
            try
            {
                bluetoothSocket.connect();

            } catch(IOException e)
            {
                Log.d("CONNECTTHREAD","Could not connect: " + e.toString());
                try
                {
                    bluetoothSocket.close();
                } catch(IOException close)
                {
                    Log.d("CONNECTTHREAD", "Could not close connection:" + e.toString());
                    return null;
                }
            }

            try
            {
                InputStream inputStream = bluetoothSocket.getInputStream();
                this.inputStream = new DataInputStream(inputStream);
            } catch (IOException e)
            {
                Log.e("CONNECTTHREAD", "temp sockets not created", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if(inputStream != null)
            {
                while(true)
                {

                }
            }
        }
    }

}

/**
 *
 * public void run()
 {
 try
 {
 mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(applicationUUID);
 mBluetoothAdapter.cancelDiscovery();
 mBluetoothSocket.connect();
 mHandler.sendEmptyMessage(0);
 }
 catch (IOException eConnectException)
 {
 Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
 closeSocket(mBluetoothSocket);
 return;
 }
 }

 private void closeSocket(BluetoothSocket nOpenSocket)
 {
 try
 {
 nOpenSocket.close();
 Log.d(TAG, "SocketClosed");
 }
 catch (IOException ex)
 {
 Log.d(TAG, "CouldNotCloseSocket");
 }
 }

 private Handler mHandler = new Handler()
 {
 @Override
 public void handleMessage(Message msg)
 {
 mBluetoothConnectProgressDialog.dismiss();
 Toast.makeText(Main.this, "DeviceConnected", 5000).show();
 }
 };
 }
 */
