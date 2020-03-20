package mustafaozhan.github.com.mycurrencies.ui.splash

import android.content.Intent
import android.os.Bundle
import com.github.mustafaozhan.basemob.activity.BaseActivity
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.ui.main.activity.MainActivity
import mustafaozhan.github.com.mycurrencies.ui.slider.SliderActivity
import javax.inject.Inject

/**
 * Created by Mustafa Ozhan on 2018-07-20.
 */
class SplashActivity : BaseActivity() {

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(
            Intent(
                this,
                if (preferencesRepository.loadMainData().sliderShown == true) {
                    MainActivity::class.java
                } else {
                    SliderActivity::class.java
                }
            )
        )

        finish()
    }
}
