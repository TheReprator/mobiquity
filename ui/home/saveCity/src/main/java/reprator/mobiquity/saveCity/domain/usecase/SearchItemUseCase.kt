package reprator.mobiquity.saveCity.domain.usecase

import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.MobiQuityResult
import reprator.mobiquity.base.useCases.Success
import reprator.mobiquity.saveCity.modal.LocationModal
import javax.inject.Inject

class SearchItemUseCase @Inject constructor() {

    operator fun invoke(
        itemList: List<LocationModal>?,
        query: String
    ): MobiQuityResult<List<LocationModal>> {

        if (itemList.isNullOrEmpty()) {
            return ErrorResult(message = "No record exist")
        }

        if (query.isEmpty() || query.trim().isEmpty()) {
            return Success(itemList)
        }

        val querySmall = query.toLowerCase().trimStart()
        val data = itemList.filter {
            it.address.toLowerCase().contains(querySmall) || it.latLong.toLowerCase().contains(querySmall)
        }

        return Success(data)
    }
}