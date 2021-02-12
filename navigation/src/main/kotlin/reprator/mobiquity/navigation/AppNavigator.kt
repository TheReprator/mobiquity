package reprator.mobiquity.navigation

import androidx.navigation.NavController

const val HIDE_TOOLBAR = "hideToolBar"
const val HOME_HIDE_BOTTOM_NAVIGATION_VIEW = "homeHideBottomNavigationView"

interface AppNavigator : SavedCityNavigator, CityDetailNavigator, SendDataToParentWithSavedStateHandle

interface SavedCityNavigator  {
    fun navigateToCityDetailScreen(
        navController: NavController,
        latLng: String, title: String
    )

    fun showBottomNavigationView(navController: NavController)
    fun hideBottomNavigationView(navController: NavController)
}

interface CityDetailNavigator : BackNavigator {
    fun showToolbar(navController: NavController)
    fun hideToolbar(navController: NavController)
    fun showBottomNavigationView(navController: NavController)
}

interface BackNavigator {
    fun navigateToBack(navController: NavController)
}

interface SendDataToParentWithSavedStateHandle {
    fun savedStateHandleCurrentBackStackEntry(navController: NavController, key: String, value: Any)
}