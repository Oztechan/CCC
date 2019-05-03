package mustafaozhan.github.com.mycurrencies.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import mustafaozhan.github.com.mycurrencies.main.activity.MainActivity

/**
 * Created by Mustafa Ozhan on 2018-07-20.
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}