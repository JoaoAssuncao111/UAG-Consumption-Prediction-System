package uagpredictionsystem.models

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.time.LocalDateTime

data class Humidity(
    @ColumnName("id")
    val id: Int,
    @ColumnName("date_hour")
    val dateHour: LocalDateTime,

    @ColumnName("location")
    val location: Int,

    @ColumnName("prediction_id")
    val predictionId: Int,

    @ColumnName("value")
    val value: Double
)
