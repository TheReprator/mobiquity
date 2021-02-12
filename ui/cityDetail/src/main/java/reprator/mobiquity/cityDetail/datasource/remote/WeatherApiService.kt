package reprator.mobiquity.cityDetail.datasource.remote

import reprator.mobiquity.cityDetail.datasource.remote.modal.ForecastLocationEntity
import reprator.mobiquity.cityDetail.datasource.remote.modal.TodayLocationEntity
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


    @GET("forecast/daily")
    suspend fun foreCastWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") appid: String = "5ad27815dc7fc065c028d64233764409",
        @Query("cnt") cnt: Int,
    ): Response<ForecastLocationEntity>
}
