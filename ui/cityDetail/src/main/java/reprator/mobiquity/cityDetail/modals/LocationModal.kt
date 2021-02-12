package reprator.mobiquity.cityDetail.modals

data class LocationModal(
    val placeName: String,
    val weather: String,
    val weatherDate: String,

    val minTemperature: String,
    val maxTemperature: String,

    val pressure: String,
    val humidity: String,

    val windSpeed: String,
    val windDegree: String,

    val sunset: String,
    val sunrise: String
)