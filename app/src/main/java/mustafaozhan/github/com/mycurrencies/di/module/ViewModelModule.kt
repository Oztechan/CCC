package mustafaozhan.github.com.mycurrencies.di.module

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:48 PM on Arch Linux wit Love <3.
 */
@Module
class ViewModelModule(private val viewModel: ViewModel) {

    @Provides
    internal fun providesViewModel(): ViewModel {
        return viewModel
    }
}
