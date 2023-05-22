package uagpredictionsystem.service

import org.springframework.stereotype.Component
import uagpredictionsystem.models.Location
import uagpredictionsystem.repository.TransactionManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed class UagGetError {
    object UagsNotFound : UagGetError()
}

@Component
class Service(private val transactionManager: TransactionManager) {

    fun getUags(): List<Location> {
        return transactionManager.run {
            val repository = it.repository
            repository.getUags()
        }

    }

    fun getReading(startDate: String, endDate: String, id: Int, readingType: String): List<Any> {
        return transactionManager.run {
            val repository = it.repository
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val newStartDate = LocalDate.parse(startDate, formatter)
            val newEndDate = LocalDate.parse(endDate, formatter)
            when (readingType) {
                "temperature" -> repository.getTemperature(newStartDate, newEndDate, id)
                "humidity" -> repository.getHumidity(newStartDate,newEndDate,id)
                "levels" -> repository.getLevels(newStartDate,newEndDate,id)
                else ->
                    mutableListOf()
            }
        }
    }
}
