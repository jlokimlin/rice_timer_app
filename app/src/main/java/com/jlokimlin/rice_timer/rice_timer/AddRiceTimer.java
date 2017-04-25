package com.jlokimlin.rice_timer.rice_timer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bunny.yunny.com.rice_timer.R;
import shortbread.Shortbread;
import shortbread.Shortcut;

/**
 *
 */
public class AddRiceTimer extends Activity implements SeekBar.OnSeekBarChangeListener{

    private static final int REST_RICE = 25;
    private static final int COOK_RICE = 15;
    private static final float BRING_RICE_TO_BOIL = 3.5f;
    public static final int SLEEP_TIME_MILLIS = 1000;
    public static final int SECONDS_IN_MINUTE = 60;
    private ExecutorService singleThreadPool;
    private Map<Integer, String> timers = new HashMap<>();
    private TextView intervalTextView;
    public static final int THREE_MINUTES = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Shortbread.create(this);
        setContentView(R.layout.activity_add_rice_timer);
        singleThreadPool = Executors.newFixedThreadPool(1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("ActivityResult", "Timer started. Request Code: " + requestCode + " Result: " + resultCode);
    }

    public void metronome(View v) {
        int timer_length = THREE_MINUTES;
        for (int i = 0; i < 16; i++) {
            try {
                Thread.sleep(SLEEP_TIME_MILLIS);
                setTimer("Flip " + i, timer_length);
                timer_length = timer_length + THREE_MINUTES;
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Shortcut(id = "startRiceId", icon = R.drawable.myshape, shortLabel = "Long grain rice")
    private void startRice() {
        try {
            Log.v("Rice timer", "Started rice timer");
            setTimer("Boil rice", BRING_RICE_TO_BOIL);
            Thread.sleep(SLEEP_TIME_MILLIS);
            // Set the 15 min timer
            setTimer("Cook rice", COOK_RICE);
            Thread.sleep(SLEEP_TIME_MILLIS);
            // Rest rice
            setTimer("Rest rice", REST_RICE);
            Thread.sleep(SLEEP_TIME_MILLIS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startRice(View v) {
        // Set the 3.5 min timer
        startRice();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_rice_timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        intervalTextView.setText("Every " + progress / 15 + " minutes");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void setTimer(final String message, final float minutes) {
        singleThreadPool.submit(new Runnable() {
            public void run() {
                int seconds = getMinutesInSeconds(minutes);
                Intent setTimerIntent = new Intent(AlarmClock.ACTION_SET_TIMER);
                setTimerIntent.putExtra(AlarmClock.EXTRA_LENGTH, seconds);
                setTimerIntent.putExtra(AlarmClock.EXTRA_MESSAGE, message);
                setTimerIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                startActivityForResult(setTimerIntent, seconds);
                Log.v("Rice timer", "Set timer of len " + seconds + " seconds with message " + message);
            }
        });
    }

    private int getMinutesInSeconds(float minutes) {
        return (int)(minutes * SECONDS_IN_MINUTE);
    }
}
