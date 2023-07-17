package uagpredictionsystem.api.models

data class ReadingInputModel(
    val startDate: String,
    val endDate: String,
    val location: Int
)