package com.jlokimlin.rice_timer.rice_timer;

import android.content.Intent;
import android.provider.AlarmClock;

/**
 * Created by jlokimlin on 4/22/17.
 */

public class Timer {
    private String message;
    private long lengthInSeconds;
    private long timeSet;
    private boolean isActive;

    public Timer(String message, long lengthInSeconds) {
        this.message = message;
        this.lengthInSeconds = lengthInSeconds;
    }

    public Intent getStartIntent() {
        Intent intent = new Intent();
        intent.putExtra(AlarmClock.EXTRA_LENGTH, lengthInSeconds);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, message);
        intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        return intent;
    }

}
