package com.mafuvadze.carsensor;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener
{
    Typeface roboto_thin, roboto_bold, roboto_italic;
    TextView temp, name, battery, ulimit, llimit;
    TextView bat_txt,ulimit_txt, llimit_txt, police_txt;
    RelativeLayout popo;
    CheckBox policeCB;
    SeekBar llimitSeek, ulimitSeek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();
        initListeners();

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

        return super.onOptionsItemSelected(item);
    }

}
