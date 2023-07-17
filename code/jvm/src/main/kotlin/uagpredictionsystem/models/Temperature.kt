package uagpredictionsystem.models

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.time.LocalDate

data class Temperature(
    @ColumnName("id")
    val id: Int,
    @ColumnName("date_hour")
    val dateHour: LocalDate,

    @ColumnName("location")
    val location: Int,

    @ColumnName("prediction_id")
    val predictionId: Int,

    @ColumnName("min_value")
    val minValue: Double,

    @ColumnName("max_value")
    val maxValue: Double
)

