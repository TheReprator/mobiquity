package reprator.mobiquity.base.useCases

sealed class MobiQuityResult<out T> {
    open fun get(): T? = null
}

data class Success<T>(val data: T, val responseModified: Boolean = true) : MobiQuityResult<T>() {
    override fun get(): T = data
}

data class ErrorResult(
    val throwable: Throwable? = null,
    val message: String? = null
) : MobiQuityResult<Nothing>()