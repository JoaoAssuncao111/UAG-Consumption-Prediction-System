package uagpredictionsystem.api.models

import java.time.LocalDate

data class Temperature (val date: LocalDate, val location: Int, val predictionId: Int, val min: Double, val max: Double)