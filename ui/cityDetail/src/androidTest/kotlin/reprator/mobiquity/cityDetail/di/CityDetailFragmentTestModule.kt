package reprator.mobiquity.cityDetail.di

import androidx.navigation.NavController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.testing.TestInstallIn
import reprator.mobiquity.navigation.CityDetailNavigator
import timber.log.Timber

@Module
@TestInstallIn(
    components = [FragmentComponent::class],
    replaces = [CityDetailFragmentModule::class]
)
class CityDetailFragmentTestModule {

    @Provides
    fun provideCityDetailNavigator(
    ): CityDetailNavigator {
        return CityDetailNavigatorImpl()
    }

}

class CityDetailNavigatorImpl : CityDetailNavigator {
    override fun showToolbar(navController: NavController) {
        Timber.e("showToolBar")
    }

    override fun hideToolbar(navController: NavController) {
        Timber.e("hideToolbar")
    }

    override fun showBottomNavigationView(navController: NavController) {
        Timber.e("showBottomNavigationView")
    }

    override fun navigateToBack(navController: NavController) {
        Timber.e("navigateToBack")
    }

}