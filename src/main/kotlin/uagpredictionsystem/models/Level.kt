package uagpredictionsystem.models

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.time.LocalDate

data class Level(
    @ColumnName("id")
    val id: Int,

    @ColumnName("date_hour")
    val date: LocalDate,

    @ColumnName("prediction_id")
    val predictionId: Int,

    @ColumnName("gas_level")
    val gasLevel: Double,
    @ColumnName("location")
    val location: Int,
    @ColumnName("deposit_number")
    val depositNumber: Int,
    @ColumnName("location")
    val counter: Long,
    @ColumnName("consumption")
    val consumption: Double

)
