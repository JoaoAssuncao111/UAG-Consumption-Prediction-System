package uagpredictionsystem.api.models

data class InsertOrUpdateLevel(
    val date: String,
    val level: Double,
    val location: Int,
    val depositNumber: Int,
    val counter: Int,
    val consumption: Double
)
