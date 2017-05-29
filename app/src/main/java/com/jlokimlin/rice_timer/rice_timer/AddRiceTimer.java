package com.jlokimlin.rice_timer.rice_timer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jlokimlin.rice_timer.R;
import com.jlokimlin.rice_timer.rice_timer.preferences.RiceTimerPreferencesActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class AddRiceTimer extends Activity {

    private static final String DEFAULT_BRING_RICE_TO_BOIL_MINUTES = "3.5f";
    private static final String DEFAULT_COOK_RICE_MINUTES = "11.5f";
    private static final String DEFAULT_REST_RICE_MINUTES = "10.0f";
    private static final int SLEEP_TIME_MILLIS = 1000;
    private static final int SECONDS_IN_MINUTE = 60;
    private ExecutorService singleThreadPool;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rice_timer);
        singleThreadPool = Executors.newFixedThreadPool(1);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    }

    public void startRice() {

        String sharedPrefBoilMinutes = sharedPref.getString(getString(R.string.boil_key),
                DEFAULT_BRING_RICE_TO_BOIL_MINUTES);
        String sharedPrefCookMinutes = sharedPref.getString(getString(R.string.cook_key),
                DEFAULT_COOK_RICE_MINUTES);
        String sharedPrefRestMinutes = sharedPref.getString(getString(R.string.rest_key),
                DEFAULT_REST_RICE_MINUTES);

        float timerBoilMinutes = Float.valueOf(sharedPrefBoilMinutes);
        float timerCookMinutes = timerBoilMinutes + Float.valueOf(sharedPrefCookMinutes);
        float timerRestMinutes = timerCookMinutes + Float.valueOf(sharedPrefRestMinutes);

        try {
            Log.v("Rice timer", "Started rice timer");
            // Set bring to boil rice timer
            setTimer(getString(R.string.boil_rice_timer_name), timerBoilMinutes);
            Thread.sleep(SLEEP_TIME_MILLIS);
            // Set cook rice timer
            setTimer(getString(R.string.cook_rice_timer_name), timerCookMinutes);
            Thread.sleep(SLEEP_TIME_MILLIS);
            // Set rest rice timer
            setTimer(getString(R.string.rest_rice_timer_name), timerRestMinutes);
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
            Intent i = new Intent(this, RiceTimerPreferencesActivity.class);
            startActivity(i);
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
