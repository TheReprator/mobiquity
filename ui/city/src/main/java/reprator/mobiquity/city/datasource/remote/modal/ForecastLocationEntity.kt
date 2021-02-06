package reprator.mobiquity.city.datasource.remote.modal

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
    "cod",
    "message",
    "cnt",
    "list",
    "city"
)
class ForecastLocationEntity(

    @JsonProperty("cod")
    val cod: String,
    @JsonProperty("message")
    val message: Int,
    @JsonProperty("cnt")
    val cnt: Int,
    @JsonProperty("list")
    val list: List<ListEntity> = emptyList(),
    @JsonProperty("city")
    val city: CityEntity
)

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
    "id",
    "name",
    "coord",
    "country",
    "population",
    "timezone",
    "sunrise",
    "sunset"
)
class CityEntity(

    @JsonProperty("id")
    val id: Int,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("coord")
    val coord: CoordEntity,
    @JsonProperty("country")
    val country: String,
    @JsonProperty("population")
    val population: Int,
    @JsonProperty("timezone")
    val timezone: Int,
    @JsonProperty("sunrise")
    val sunrise: Int,
    @JsonProperty("sunset")
    val sunset: Int
)


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
    "dt",
    "main",
    "weather",
    "clouds",
    "wind",
    "visibility",
    "pop",
    "sys",
    "dt_txt"
)
class ListEntity(
    @JsonProperty("dt")
    val dt: Int,
    @JsonProperty("main")
    val main: MainEntity,
    @JsonProperty("weather")
    val weather: List<WeatherEntity> = emptyList(),
    @JsonProperty("clouds")
    val clouds: CloudEntity,
    @JsonProperty("wind")
    val wind: WindEntity,
    @JsonProperty("visibility")
    val visibility: Int,
    @JsonProperty("pop")
    val pop: Int,
    @JsonProperty("sys")
    val sys: SysEntity,
    @JsonProperty("dt_txt")
    val dtTxt: String
)
