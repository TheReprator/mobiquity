package reprator.mobiquity.cityDetail.datasource.remote.modal

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
    "coord",
    "weather",
    "base",
    "main",
    "visibility",
    "wind",
    "clouds",
    "dt",
    "sys",
    "timezone",
    "id",
    "name",
    "cod"
)
class TodayLocationEntity(

    @JsonProperty("coord") val coord: CoordEntity,
    @JsonProperty("weather") val weather: List<WeatherEntity> = emptyList(),
    @JsonProperty("base") val base: String,
    @JsonProperty("main")
    val main: MainEntity,
    @JsonProperty("visibility")
    val visibility: Int,
    @JsonProperty("wind")
    val wind: WindEntity,
    @JsonProperty("clouds")
    val clouds: CloudEntity,
    @JsonProperty("dt")
    val dt: Int,
    @JsonProperty("sys")
    val sys: SysEntity,
    @JsonProperty("timezone")
    val timezone: Int,
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("cod")
    val cod: Int
)

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
    "all"
)
class CloudEntity(@JsonProperty("all") val all: Int)

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
    "lon",
    "lat"
)
class CoordEntity(
    @JsonProperty("lon") val lon: Double,
    @JsonProperty("lat") val lat: Double
)

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
    "temp",
    "feels_like",
    "temp_min",
    "temp_max",
    "pressure",
    "humidity",
    "sea_level",
    "grnd_level"
)
class MainEntity(
    @JsonProperty("temp")
    val temp: Double,
    @JsonProperty("feels_like")
    val feelsLike: Double,
    @JsonProperty("temp_min")
    val tempMin: Double,
    @JsonProperty("temp_max")
    val tempMax: Double,
    @JsonProperty("pressure")
    val pressure: Int,
    @JsonProperty("humidity")
    val humidity: Int,
    @JsonProperty("sea_level")
    val seaLevel: Int,
    @JsonProperty("grnd_level")
    val grndLevel: Int
)

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
    "country",
    "sunrise",
    "sunset"
)
class SysEntity(

    @JsonProperty("country")
    val country: String = "",
    @JsonProperty("sunrise")
    val sunrise: Int = 0,
    @JsonProperty("sunset")
    val sunset: Int = 0
)

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
    "id",
    "main",
    "description",
    "icon"
)
class WeatherEntity(
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("main")
    val main: String,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("icon")
    val icon: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
    "speed",
    "deg"
)
class WindEntity(
    @JsonProperty("speed")
    val speed: Double,
    @JsonProperty("deg")
    val deg: Int
)
