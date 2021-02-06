package reprator.mobiquity.city.modals

class LocationRequestModal(
    val latitude: String,
    val longitude: String,
    val unit: String = "standard",
    val count: Int = 5

)