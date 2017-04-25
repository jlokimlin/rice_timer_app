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
import java.util.concurrent.TimeUnit;

import bunny.yunny.com.rice_timer.R;
import shortbread.Shortbread;
import shortbread.Shortcut;


public class AddRiceTimer extends Activity implements SeekBar.OnSeekBarChangeListener{

    public static final int REST_RICE = 25;
    public static final int COOK_RICE = 15;
    public static final float BRING_RICE_TO_BOIL = 3.5f;
    private ExecutorService singleThreadPool;
    private Map<Integer, String> timers = new HashMap<>();
    private TextView intervalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Shortbread.create(this);
        setContentView(R.layout.activity_add_rice_timer);
        singleThreadPool = Executors.newFixedThreadPool(1);
    }

    public void setTimer(final String message, final float minutes) {
        singleThreadPool.submit(new Runnable() {
            public void run() {
                //TimeUnit.SECONDS.convert(minutes, TimeUnit.MINUTES);
                int seconds = (int)(60 * minutes);
                Intent met = new Intent(AlarmClock.ACTION_SET_TIMER);
                met.putExtra(AlarmClock.EXTRA_LENGTH, seconds);
                met.putExtra(AlarmClock.EXTRA_MESSAGE, message);
                met.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                startActivityForResult(met,seconds);
                Log.v("Rice timer", "Set timer of len " + seconds + " seconds with message " + message);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("ActivityResult", "Timer started. Request Code: " + requestCode + " Result: " + resultCode);
    }

    public void metronome(View w) {
        int three_minutes = 3;
        int timer_length = three_minutes;
        for (int i = 0; i < 16; i++) {
            try {
                Thread.sleep(1000);
                setTimer("Flip " + i, timer_length);
                timer_length = timer_length + three_minutes;
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Shortcut(id = "startRiceId", icon = R.drawable.myshape, shortLabel = "Long grain rice")
    public void startRice() {
        try {
            Log.v("Rice timer", "Started rice timer");
            setTimer("Boil rice", BRING_RICE_TO_BOIL);
            Thread.sleep(1000);
            // Set the 15 min timer
            setTimer("Cook rice", COOK_RICE);
            Thread.sleep(1000);
            // Rest rice
            setTimer("Rest rice", REST_RICE);
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startRice(View v) {
        // Set the 3.5 min timer
        startRice();
    }
/*    public void startJasmine(View v) {
        Intent i =new Intent(AlarmClock.ACTION_SET_TIMER);
        i.putExtra(AlarmClock.EXTRA_LENGTH,240);
        i.putExtra(AlarmClock.EXTRA_MESSAGE,"Jasmine rice - bring to boil - 4 min");
        i.putExtra(AlarmClock.EXTRA_SKIP_UI,true);
        startActivity(i);
        // Set the 15 min timer
        Intent j =new Intent(AlarmClock.ACTION_SET_TIMER);
        j.putExtra(AlarmClock.EXTRA_LENGTH,840);
        j.putExtra(AlarmC;lock.EXTRA_MESSAGE,"Jasmine rice 14 min");
        j.putExtra(AlarmClock.EXTRA_SKIP_UI,true);
        startActivity(j);
        // Rest the rice for 10 minutes
        Intent k =new Intent(AlarmClock.ACTION_SET_TIMER);
        k.putExtra(AlarmClock.EXTRA_LENGTH,1440);
        k.putExtra(AlarmClock.EXTRA_MESSAGE,"Jasmine rice resting period");
        k.putExtra(AlarmClock.EXTRA_SKIP_UI,true);
        startActivity(k);
    }
    public void startBasmati(View v) {
        Intent i =new Intent(AlarmClock.ACTION_SET_TIMER);
        i.putExtra(AlarmClock.EXTRA_LENGTH,300);
        i.putExtra(AlarmClock.EXTRA_MESSAGE,"Basmati rice - bring to boil - 5 min");
        i.putExtra(AlarmClock.EXTRA_SKIP_UI,true);
        startActivity(i);
        // Set the 16 min timer
        Intent j =new Intent(AlarmClock.ACTION_SET_TIMER);
        j.putExtra(AlarmClock.EXTRA_LENGTH,960);
        j.putExtra(AlarmClock.EXTRA_MESSAGE,"Basmati rice 16 min");
        j.putExtra(AlarmClock.EXTRA_SKIP_UI,true);
        startActivity(j);
        // Rest the rice for 10 minutes
        Intent k =new Intent(AlarmClock.ACTION_SET_TIMER);
        k.putExtra(AlarmClock.EXTRA_LENGTH,1560);
        k.putExtra(AlarmClock.EXTRA_MESSAGE,"Basmati rice resting period");
        k.putExtra(AlarmClock.EXTRA_SKIP_UI,true);
        startActivity(k);
    }*/

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
        intervalTextView.setText("Every " + (int)(progress / 15) + " minutes");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
