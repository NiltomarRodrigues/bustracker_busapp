package pet.niltomar.bustracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * Essa Activity ainda não foi implementada, vai ser como um
 * menu de configurações do apliacativo
 *
 * @deprecated
 */
public class GPSTrackerPrefs extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference pref;

        pref = findPreference("pref_gps_updates");
        pref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int prefMaxRunTime = Integer.parseInt(preferences.getString("pref_max_run_time", "0"));
                int oldValue = Integer.parseInt(preferences.getString("pref_gps_updates", "0"));
                if (newValue == null
                        || newValue.toString().length() == 0
                        || !Pattern.matches("^\\d{1,5}$", newValue.toString())) {
                    Alert(getString(R.string.invalid_number));
                    return false;
                } else if (Integer.parseInt(newValue.toString()) > prefMaxRunTime * 3600) {
                    Alert(getString(R.string.pref_gps_updates_too_high));
                    return false;
                } else if (Integer.parseInt(newValue.toString()) < 5) {
                    Alert(getString(R.string.pref_gps_updates_too_low));
                    return false;
                } else if (Integer.parseInt(newValue.toString()) < 30) {
                    Alert(getString(R.string.pref_battery_drain));
                } else if (GPSTrackerService.isRunning
                        && Integer.parseInt(newValue.toString()) != oldValue) {
                    Alert(getString(R.string.toast_prefs_restart));
                }
                return true;
            }
        });

        pref = findPreference("pref_max_run_time");
        pref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int prefGpsUpdates = Integer.parseInt(preferences.getString("pref_gps_updates", "0"));
                int oldValue = Integer.parseInt(preferences.getString("pref_max_run_time", "0"));
                if (newValue == null
                        || newValue.toString().length() == 0
                        || !Pattern.matches("^\\d{1,5}$", newValue.toString())) {
                    Alert(getString(R.string.invalid_number));
                    return false;
                } else if (Integer.parseInt(newValue.toString()) * 3600 < prefGpsUpdates) {
                    Alert(getString(R.string.pref_max_run_time_too_low));
                    return false;
                } else if (GPSTrackerService.isRunning
                        && Integer.parseInt(newValue.toString()) != oldValue) {
                    Alert(getString(R.string.toast_prefs_restart));
                }
                return true;
            }
        });

        pref = findPreference("pref_timestamp");
        pref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                boolean oldValue = preferences.getBoolean("pref_timestamp", false);
                if (GPSTrackerService.isRunning
                        && (Boolean) newValue != oldValue) {
                    Alert(getString(R.string.toast_prefs_restart));
                }
                return true;
            }
        });
    }

    private void Alert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GPSTrackerPrefs.this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}