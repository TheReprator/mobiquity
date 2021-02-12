package reprator.mobiquity.database

object DBFakeData {

    fun getFakeDataList(): List<LocationEntity> {

        return listOf(
            LocationEntity(
                "35.23,76.23", "Hajipur, Bihar"
            ),
            LocationEntity(
                "29.23,74.13", "Mohali, Punjab"
            )
        )
    }
}