package reprator.mobiquity.navigation

import androidx.navigation.NavController

const val DATA_CONSTANT = "sendDataToHost"
const val HOME_DATA_CONSTANT = "homeSendDataToHost"

interface AppNavigator : SavedCityNavigator, CityDetailNavigator

interface SavedCityNavigator : SendDataToParentWithSavedStateHandle {
    fun navigateToCityDetailScreen(
        navController: NavController,
        latLng: String, title: String
    )
}

interface CityDetailNavigator : BackNavigator, SendDataToParentWithSavedStateHandle

interface BackNavigator {
    fun navigateToBack(navController: NavController)
}

interface SendDataToParentWithSavedStateHandle {
    fun savedStateHandleCurrentBackStackEntry(navController: NavController, key: String, value: Any)
}