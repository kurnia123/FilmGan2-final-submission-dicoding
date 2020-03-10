package com.example.filmgan2.settings;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.example.filmgan2.Notification.AlarmReceiver;
import com.example.filmgan2.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat{
        SwitchPreferenceCompat preferencesReminder;
        SwitchPreferenceCompat preferenceNewFilm;
        AlarmReceiver alarmReceiver;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            preferencesReminder = findPreference(getString(R.string.switch_preference_reminder_notification));
            preferenceNewFilm = findPreference(getString(R.string.switch_preference_notification_new_film));

            alarmReceiver = new AlarmReceiver();
        }

        @Override
        public void onResume() {
            super.onResume();
            preferencesReminder.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.d("SETTINGS","REMINDER : " + String.valueOf(!preferencesReminder.isChecked()));
                    if (!preferencesReminder.isChecked()){
                        alarmReceiver.setRepeatingALarm(getContext(),AlarmReceiver.TYPE_REPEATING_REMINDER,"FIlmGan menunggumu, cek film terbaru",7);
                    } else {
                        alarmReceiver.cancelAlarm(getContext(),AlarmReceiver.TYPE_REPEATING_REMINDER);
                    }
                    return true;
                }
            });

            preferenceNewFilm.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.d("SETTINGS","NEW FILM : " + String.valueOf(!preferenceNewFilm.isChecked()));
                    if (!preferenceNewFilm.isChecked()){
                        alarmReceiver.setRepeatingALarm(getContext(),AlarmReceiver.TYPE_REPEATING_NEW_FILM,"MESSAGE NEW FILM",8);
                    } else {
                        alarmReceiver.cancelAlarm(getContext(),AlarmReceiver.TYPE_REPEATING_NEW_FILM);
                    }
                    return true;
                }
            });
        }
    }
}