package reprator.mobiquity.navigation

import androidx.navigation.NavController

const val DATA_CONSTANT = "sendDataToHost"

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
    fun savedStateHandle(navController: NavController, key: String, value: Any)
}