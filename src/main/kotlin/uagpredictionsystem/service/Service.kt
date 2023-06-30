package uagpredictionsystem.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.stereotype.Component
import uagpredictionsystem.invokeTrainingAlgorithm
import uagpredictionsystem.models.FilteredConsumptions
import uagpredictionsystem.models.Location
import uagpredictionsystem.models.TemperatureAndConsumption
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
                "humidity" -> repository.getHumidity(newStartDate, newEndDate, id)
                "levels" -> repository.getLevels(newStartDate, newEndDate, id)
                else ->
                    mutableListOf()
            }

        }
    }

    fun getTempAndCons(startDate: String, endDate: String, id: Int): TemperatureAndConsumption {
        return transactionManager.run {
            val repository = it.repository
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val newStartDate = LocalDate.parse(startDate, formatter)
            val newEndDate = LocalDate.parse(endDate, formatter)
            val temperatures = repository.getTemperature(newStartDate, newEndDate, id)
            val consumptions = repository.get9PastLevels(newStartDate, newEndDate, id)
            val filteredConsumptions: List<FilteredConsumptions> = consumptions.map { level ->
                FilteredConsumptions(
                    id = level.id,
                    date = level.date,
                    location = level.location,
                    consumption = level.consumption,
                    depositNumber = level.depositNumber
                )
            }
            TemperatureAndConsumption(temperatures, filteredConsumptions)
        }
    }

    fun getTraining(startDate: String, endDate: String): List<Location> {

        val objectMapper = ObjectMapper().apply {
            registerModule(JavaTimeModule()) // Register the JavaTimeModule for LocalDate support
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false) // Optional: Configure to write dates as ISO 8601 strings
        }

        return transactionManager.run {
            val repository = it.repository
            val uags = repository.getUags()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val newStartDate = LocalDate.parse(startDate, formatter)
            val newEndDate = LocalDate.parse(endDate, formatter)
            for (uag in uags) {
                val temperatures = repository.getTemperature(newStartDate, newEndDate, uag.id)
                val consumptions = repository.getLevels(newStartDate, newEndDate, uag.id)
                if (temperatures.isEmpty() || consumptions.isEmpty()) {
                    continue
                }

                val temperaturesJson = objectMapper.writeValueAsString(temperatures)
                val consumptionsJson = objectMapper.writeValueAsString(consumptions)

                val trainingOutputJson = invokeTrainingAlgorithm(temperaturesJson, consumptionsJson)

                if(trainingOutputJson != "") repository.updateTraining(uag.id, trainingOutputJson)

            }
            repository.getUags()
        }
    }
}
