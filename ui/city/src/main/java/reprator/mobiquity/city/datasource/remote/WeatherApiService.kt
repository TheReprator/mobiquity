package reprator.mobiquity.city.datasource.remote

import reprator.mobiquity.city.datasource.remote.modal.ForecastLocationEntity
import reprator.mobiquity.city.datasource.remote.modal.TodayLocationEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    companion object {
        private const val CURRENCY_API_KEY = "fae7190d7e6433ec3a45285ffcf55c86"
    }

    @GET("weather")
    suspend fun todayWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") appid: String = CURRENCY_API_KEY,
    ): Response<TodayLocationEntity>

    //http://api.openweathermap.org/data/2.5/weather?lat=30.70&lon=76.71&appid=fae7190d7e6433ec3a45285ffcf55c86

    @GET("forecast")
    suspend fun foreCastWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") appid: String = CURRENCY_API_KEY,
        @Query("cnt") cnt: Int = 2,
    ): Response<ForecastLocationEntity>
}

//http://api.openweathermap.org/data/2.5/forecast?lat=30.70&lon=76.71&cnt=2&appid=fae7190d7e6433ec3a45285ffcf55c86&units=metric