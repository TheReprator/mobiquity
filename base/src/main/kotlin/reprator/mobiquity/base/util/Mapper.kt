package reprator.mobiquity.base.util

interface Mapper<in InputModal, out OutputModal> {
    suspend fun map(from: InputModal): OutputModal
}