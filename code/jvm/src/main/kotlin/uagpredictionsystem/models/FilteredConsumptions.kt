package uagpredictionsystem.models

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.time.LocalDate


    data class FilteredConsumptions(
        @ColumnName("id")
        val id: Int,

        @ColumnName("date_hour")
        val date: LocalDate,


        @ColumnName("location")
        val location: Int,

        @ColumnName("deposit_number")
        val depositNumber: Int,

        @ColumnName("consumption")
        val consumption: Double
    )
