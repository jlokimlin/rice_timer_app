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

    private static final int DEFAULT_REST_RICE = 25;
    private static final int COOK_RICE = 15;
    private static final float BRING_RICE_TO_BOIL = 3.5f;
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
        Shortbread.create(this);
        setContentView(R.layout.activity_add_rice_timer);
        singleThreadPool = Executors.newFixedThreadPool(1);
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        intervalTextView = (TextView)findViewById(R.id.intervalTextView);
        numberOfTimesTextView = (TextView)findViewById(R.id.numberOfTimesTextView);
        SeekBar intervalSeekBar = (SeekBar)findViewById(R.id.intervalMinutesSeekBar);
        SeekBar numberOfTimesSeekBar = (SeekBar)findViewById(R.id.numberOfTimesSeekBar);
        intervalSeekBar.setOnSeekBarChangeListener(this);
        numberOfTimesSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("ActivityResult", "Timer started. Request Code: " + requestCode + " Result: " + resultCode);
    }

    public void metronome(View v) {
        int metronomeInterval = sharedPref.getInt(getString(R.string.metronome_interval), DEFAULT_METRONOME_INTERVAL_MINUTES);
        float numberOfTimers = sharedPref.getInt(getString(R.string.metronome_count), DEFAULT_METRONOME_COUNT);
        int timerLengthMinutes = metronomeInterval;
        for (int i = 0; i < numberOfTimers; i++) {
            try {
                Thread.sleep(SLEEP_TIME_MILLIS);
                setTimer("Flip " + i, timerLengthMinutes);
                timerLengthMinutes = timerLengthMinutes + metronomeInterval;
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Shortcut(id = "startRiceId", icon = R.drawable.myshape, shortLabel = "Long grain rice")
    public void startRice() {

//        int defaultValue = getResources().getInteger(R.string.saved_high_score_default);
        long riceRestMinutes = sharedPref.getInt(getString(R.string.rest_key), DEFAULT_REST_RICE);
        float bringRiceToBoilMinutes = sharedPref.getFloat(getString(R.string.boil_key), BRING_RICE_TO_BOIL);
        long cookRiceMinutes = sharedPref.getInt(getString(R.string.cook_key), COOK_RICE);
        try {
            Log.v("Rice timer", "Started rice timer");
            setTimer("Boil rice", BRING_RICE_TO_BOIL);
            Thread.sleep(SLEEP_TIME_MILLIS);
            // Set the 15 min timer
            setTimer("Cook rice", COOK_RICE);
            Thread.sleep(SLEEP_TIME_MILLIS);
            // Rest rice
            setTimer("Rest rice", riceRestMinutes);
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
        Log.v("Timer App", "On Progress Changed: " + progress);
        int seekBarId = seekBar.getId();
        if (seekBarId == R.id.intervalMinutesSeekBar) {
            sharedPref.edit().putInt(getString(R.string.metronome_interval), progress).apply();
            intervalTextView.setText("Every " + progress  + " minutes");
        } else if (seekBarId == R.id.numberOfTimesSeekBar) {
            sharedPref.edit().putInt(getString(R.string.metronome_count), progress).apply();
            numberOfTimesTextView.setText(progress  + " times" );
        }
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
