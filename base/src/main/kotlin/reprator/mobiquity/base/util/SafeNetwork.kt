package reprator.mobiquity.base.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import reprator.mobiquity.base.useCases.ErrorResult
import reprator.mobiquity.base.useCases.MobiQuityResult

suspend fun <T : Any> safeApiCall(
    call: suspend () -> Flow<MobiQuityResult<T>>,
    errorMessage: String? = null
): Flow<MobiQuityResult<T>> {
    return try {
        call()
    } catch (e: Exception) {
        println(e.printStackTrace())
        flow {
            emit(ErrorResult(message = errorMessage ?: e.message))
        }
    }
}