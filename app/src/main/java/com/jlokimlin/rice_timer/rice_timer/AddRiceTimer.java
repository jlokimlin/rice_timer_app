package com.jlokimlin.rice_timer.rice_timer;

import android.app.Activity;
import android.app.IntentService;
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

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * A simple rice timer app, which sets three user-configurable timers for making perfect rice.
 */
public class AddRiceTimer extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rice_timer);
    }

    public void startRice(View v) {
        Intent intent = new Intent(this, RiceTimerService.class);
        startService(intent);
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

    public static class RiceTimerService extends IntentService {
        private static final String DEFAULT_BRING_RICE_TO_BOIL_MINUTES = "3.5f";
        private static final String DEFAULT_COOK_RICE_MINUTES = "11.5f";
        private static final String DEFAULT_REST_RICE_MINUTES = "10.0f";
        private static final int SECONDS_IN_MINUTE = 60;

        private SharedPreferences sharedPref;

        public RiceTimerService() {
            super("RiceTimerService");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            startRiceTimer();
        }

        private void startRiceTimer() {
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
                Intent[] intents = new Intent[3];
                Log.v("Rice timer", "Starting rice timer");
                // Set bring to boil rice timer
                intents[0] = createTimerIntent(getString(R.string.boil_rice_timer_name),
                        timerBoilMinutes);
                // Set cook rice timer
                intents[1] = createTimerIntent(getString(R.string.cook_rice_timer_name),
                        timerCookMinutes);
                // Set rest rice timer
                intents[2] = createTimerIntent(getString(R.string.rest_rice_timer_name),
                        timerRestMinutes);
                this.startActivities(intents);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private Intent createTimerIntent(final String message, final float minutes) {
            int seconds = getMinutesInSeconds(minutes);
            Intent setTimerIntent = new Intent(AlarmClock.ACTION_SET_TIMER);
            setTimerIntent.putExtra(AlarmClock.EXTRA_LENGTH, seconds);
            setTimerIntent.putExtra(AlarmClock.EXTRA_MESSAGE, message);
            setTimerIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
            setTimerIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            return setTimerIntent;
        }

        private int getMinutesInSeconds(float minutes) {
            return (int)(minutes * SECONDS_IN_MINUTE);
        }
    }
}
