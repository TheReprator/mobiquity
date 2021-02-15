package reprator.mobiquity.implementation

import androidx.navigation.NavController
import dagger.hilt.android.scopes.ActivityScoped
import reprator.mobiquity.navigation.AppNavigator
import reprator.mobiquity.navigation.HIDE_TOOLBAR
import reprator.mobiquity.navigation.HOME_HIDE_BOTTOM_NAVIGATION_VIEW
import reprator.mobiquity.saveCity.ui.SaveCityFragmentDirections
import javax.inject.Inject

@ActivityScoped
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

    override fun showBottomNavigationView(navController: NavController) {
        toggleBottomNavigationView(navController, true)
    }

    override fun hideBottomNavigationView(navController: NavController) {
        toggleBottomNavigationView(navController, false)
    }

    private fun toggleBottomNavigationView(navController: NavController, isShow: Boolean) {
        savedStateHandleCurrentBackStackEntry(
            navController,
            HOME_HIDE_BOTTOM_NAVIGATION_VIEW,
            isShow
        )
    }

    override fun hideToolbar(navController: NavController) {
        toggleToolBar(navController, true)
    }

    override fun showToolbar(navController: NavController) {
        toggleToolBar(navController, false)
    }

    private fun toggleToolBar(navController: NavController, isShow: Boolean) {
        savedStateHandleCurrentBackStackEntry(navController, HIDE_TOOLBAR, isShow)
    }

    override fun savedStateHandleCurrentBackStackEntry(
        navController: NavController,
        key: String,
        value: Any
    ) {
        navController.currentBackStackEntry!!.savedStateHandle.set(key, value)
    }

    override fun navigateToBack(navController: NavController) {
        navController.navigateUp()
    }
}