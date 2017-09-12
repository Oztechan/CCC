package mustafaozhan.github.com.mycurrencies.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import mustafaozhan.github.com.mycurrencies.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    override fun onBackPressed() {
        finish()
    }
}
