package com.jlokimlin.rice_timer.rice_timer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jlokimlin.rice_timer.R;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class AddRiceTimer extends Activity {

    private static final int DEFAULT_REST_RICE = 25;
    private static final int DEFAULT_COOK_RICE_MINUTES = 15;
    private static final float BRING_RICE_TO_BOIL_MINUTES = 3.5f;
    private static final int SLEEP_TIME_MILLIS = 1000;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int DEFAULT_METRONOME_INTERVAL_MINUTES = 2;
    private static final int DEFAULT_METRONOME_COUNT = 6;
    private ExecutorService singleThreadPool;
    private Map<Integer, String> timers = new HashMap<>();
    private TextView intervalTextView;
    private TextView numberOfTimesTextView;
    private static final int THREE_MINUTES = 3;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rice_timer);
        singleThreadPool = Executors.newFixedThreadPool(1);
        sharedPref = getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("ActivityResult", "Timer started. Request Code: " + requestCode + " Result: " + resultCode);
    }

    public void startRice() {

        long riceRestMinutes = sharedPref.getInt(getString(R.string.rest_key), DEFAULT_REST_RICE);
        float bringRiceToBoilMinutes = sharedPref.getFloat(getString(R.string.boil_key), BRING_RICE_TO_BOIL_MINUTES);
        long cookRiceMinutes = sharedPref.getInt(getString(R.string.cook_key), DEFAULT_COOK_RICE_MINUTES);
        try {
            Log.v("Rice timer", "Started rice timer");
            // Set bring to boil rice timer
            setTimer("Boil rice", BRING_RICE_TO_BOIL_MINUTES);
            Thread.sleep(SLEEP_TIME_MILLIS);
            // Set cook rice timer
            setTimer("Cook rice", DEFAULT_COOK_RICE_MINUTES);
            Thread.sleep(SLEEP_TIME_MILLIS);
            // Set rest rice timer
            setTimer("Rest rice", riceRestMinutes);
            Thread.sleep(SLEEP_TIME_MILLIS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startRice(View v) {
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
