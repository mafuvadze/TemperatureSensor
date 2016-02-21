package com.mafuvadze.carsensor;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements TempDisplay.OnFragmentInteractionListener, DeviceStateHistory.OnFragmentInteractionListener, Settings.OnFragmentInteractionListener {

    ViewPager pager;
    SlidingTabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();

    }

    private void initUi() {
        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (SlidingTabLayout) findViewById(R.id.tab_host);

        Fragment[] tabList = new Fragment[]{TempDisplay.newInstance("1", "1"), DeviceStateHistory.newInstance("2", "2"), Settings.newInstance("3", "3")};
        android.support.v4.view.PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), this, tabList);
        pager.setAdapter(adapter);

        tabs.setDistributeEvenly(true);
        tabs.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tabs.setSelectedIndicatorColors(getResources().getColor(R.color.Red));
        tabs.setCustomTabView(R.layout.custom_tab, R.id.tabText);
        tabs.setViewPager(pager);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
