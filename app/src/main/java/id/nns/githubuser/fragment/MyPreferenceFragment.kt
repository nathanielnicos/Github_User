package id.nns.githubuser.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import id.nns.githubuser.R
import id.nns.githubuser.alarm.AlarmReceiver

class MyPreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var reminder: String
    private lateinit var isReminderOn: SwitchPreference
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()
        setSummary()
    }

    private fun init() {
        reminder = resources.getString(R.string.key_reminder)
        isReminderOn = findPreference<SwitchPreference>(reminder) as SwitchPreference
    }

    private fun setSummary() {
        val sp = preferenceManager.sharedPreferences
        isReminderOn.isChecked = sp.getBoolean(reminder, false)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences, p1: String?) {
        if (p1 == reminder) {
            isReminderOn.isChecked = p0.getBoolean(reminder, false)

            alarmReceiver = AlarmReceiver()

            if (isReminderOn.isChecked) {
                alarmReceiver.setRepeatingAlarm(requireContext(), "The alarm is ringing!")
            } else {
                alarmReceiver.cancelAlarm(requireContext())
            }
        }
    }

}
