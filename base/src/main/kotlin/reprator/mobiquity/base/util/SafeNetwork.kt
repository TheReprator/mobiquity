package reprator.mobiquity.base.util

import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.MobiQuityResult

suspend fun <T : Any> safeApiCall(
    call: suspend () -> MobiQuityResult<T>,
    errorMessage: String? = null
): MobiQuityResult<T> {
    return try {
        call()
    } catch (e: Exception) {
        println(e.printStackTrace())
        ErrorResult(message = errorMessage ?: e.message)
    }
}