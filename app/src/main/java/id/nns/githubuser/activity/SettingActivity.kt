package id.nns.githubuser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.nns.githubuser.R
import id.nns.githubuser.fragment.MyPreferenceFragment

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportActionBar?.title = "Setting"

        supportFragmentManager.beginTransaction()
            .add(R.id.setting_holder, MyPreferenceFragment())
            .commit()
    }
}