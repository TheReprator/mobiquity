package reprator.mobiquity.base.useCases

interface UseCase<Type, in Params> {
     suspend fun run(params: Params): MobiQuityResult<Type>
}