package uagpredictionsystem.models

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.time.LocalDate

data class Delivery(
    @ColumnName("load_amount")
    val load: Double,
    @ColumnName("date_hour")
    val date: LocalDate,
    @ColumnName("location_id")
    val location: Int
)