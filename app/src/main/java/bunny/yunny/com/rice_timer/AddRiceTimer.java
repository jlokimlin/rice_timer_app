package bunny.yunny.com.rice_timer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import shortbread.Shortbread;
import shortbread.Shortcut;


public class AddRiceTimer extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Shortbread.create(this);
        setContentView(R.layout.activity_add_rice_timer);
    }

    public void setTimer(final String message, final int timer_length) {
        new Thread(new Runnable() {
            public void run() {
                Intent met = new Intent(AlarmClock.ACTION_SET_TIMER);
                met.putExtra(AlarmClock.EXTRA_LENGTH, timer_length);
                met.putExtra(AlarmClock.EXTRA_MESSAGE, message);
                met.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                startActivity(met);
                System.out.println("Set timer of len " + timer_length + " seconds with message " + message);
            }
        }).start();
    }

    public void metronome(View w) {
        int three_minutes_in_seconds = 180;
        int timer_length = three_minutes_in_seconds;
        for (int i = 0; i < 16; i++) {
            try {
                Thread.sleep(1000);
                setTimer("Flip " + i, timer_length);
                timer_length = timer_length + three_minutes_in_seconds - 1;
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Shortcut(id = "startRiceId", icon = R.drawable.myshape, shortLabel = "Long grain rice")
    public void startRice() {
        try {
            Intent i = new Intent(AlarmClock.ACTION_SET_TIMER);
            i.putExtra(AlarmClock.EXTRA_LENGTH, 210);
            i.putExtra(AlarmClock.EXTRA_MESSAGE, "Bring rice to boil - 3.5 min");
            i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
            startActivity(i);
            Thread.sleep(1000);
            // Set the 15 min timer
            Intent j = new Intent(AlarmClock.ACTION_SET_TIMER);
            j.putExtra(AlarmClock.EXTRA_LENGTH, 900);
            j.putExtra(AlarmClock.EXTRA_MESSAGE, "Cook rice - 15 min");
            j.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
            startActivity(j);
            Thread.sleep(1000);
            // Rest the rice for 10 minutes
            Intent k = new Intent(AlarmClock.ACTION_SET_TIMER);
            k.putExtra(AlarmClock.EXTRA_LENGTH, 1500);
            k.putExtra(AlarmClock.EXTRA_MESSAGE, "Rest rice - 10 min");
            k.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
            startActivity(k);
        } catch (InterruptedException e) {
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
        }
        return super.onOptionsItemSelected(item);
    }
}
