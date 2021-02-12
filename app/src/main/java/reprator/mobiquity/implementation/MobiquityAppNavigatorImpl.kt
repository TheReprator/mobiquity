package reprator.mobiquity.implementation

import androidx.navigation.NavController
import reprator.mobiquity.navigation.AppNavigator
import reprator.mobiquity.saveCity.ui.SaveCityFragmentDirections
import javax.inject.Inject

class MobiquityAppNavigatorImpl @Inject constructor() : AppNavigator {

    override fun navigateToCityDetailScreen(
        navController: NavController,
        latLng: String,
        title: String
    ) {
        val direction =
            SaveCityFragmentDirections.navigationHomeBookmarkLocationsToNavigationCityDetail(
                latLng,
                title
            )
        navController.navigate(direction)
    }

    override fun savedStateHandleCurrentBackStackEntry(navController: NavController, key: String, value: Any) {
        navController.currentBackStackEntry!!.savedStateHandle.set(key, value)
    }

    override fun navigateToBack(navController: NavController) {
        navController.navigateUp()
    }
}