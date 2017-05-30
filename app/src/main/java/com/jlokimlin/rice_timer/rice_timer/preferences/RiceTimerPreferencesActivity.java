package com.jlokimlin.rice_timer.rice_timer.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.jlokimlin.rice_timer.R;

/**
 * A preferences activity to set timer lengths in minutes.
 */

public class RiceTimerPreferencesActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new RiceTimerPreferencesFragment()).commit();
    }

    public static class RiceTimerPreferencesFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }

}
