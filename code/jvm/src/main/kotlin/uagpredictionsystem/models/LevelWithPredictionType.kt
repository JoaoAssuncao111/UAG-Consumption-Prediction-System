package uagpredictionsystem.models

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.time.LocalDate

data class LevelWithPredictionType(
    @ColumnName("id")
    val id: Int,

    @ColumnName("date_hour")
    val date: LocalDate,

    @ColumnName("prediction_id")
    val predictionId: Int,

    val predictionType: String,
    @ColumnName("gas_level")
    val gasLevel: Double,
    @ColumnName("location")
    val location: Int,
    @ColumnName("deposit_number")
    val depositNumber: Int,
    @ColumnName("counter")
    val counter: Long,
    @ColumnName("consumption")
    val consumption: Double


)
