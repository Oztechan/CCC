package mustafaozhan.github.com.mycurrencies.splash

import android.content.Intent
import android.os.Bundle
import mustafaozhan.github.com.mycurrencies.base.BaseMvvmActivity
import mustafaozhan.github.com.mycurrencies.main.activity.MainActivity
import mustafaozhan.github.com.mycurrencies.slider.SliderActivity

/**
 * Created by Mustafa Ozhan on 2018-07-20.
 */
class SplashActivity : BaseMvvmActivity<SplashActivityViewModel>() {
    override fun getViewModelClass(): Class<SplashActivityViewModel> = SplashActivityViewModel::class.java
    override fun getLayoutResId(): Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(
            Intent(
                this,
                if (viewModel.isSliderShown() == true) {
                    MainActivity::class.java
                } else {
                    SliderActivity::class.java
                }
            )
        )

        finish()
    }
}
