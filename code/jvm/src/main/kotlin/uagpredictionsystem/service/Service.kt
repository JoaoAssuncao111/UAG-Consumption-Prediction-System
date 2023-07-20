package uagpredictionsystem.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.stereotype.Component
import uagpredictionsystem.functions.*
import uagpredictionsystem.invokePredictionAlgorithm
import uagpredictionsystem.invokeTrainingAlgorithm
import uagpredictionsystem.models.*
import uagpredictionsystem.repository.TransactionManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter


sealed class UagGetError {
    object UagsNotFound : UagGetError()
}

sealed class UagPostError {
    object UagNameAlreadyExists : UagPostError()
}

@Component
class Service(private val transactionManager: TransactionManager) {

    fun getUags(): List<Location> {
        return transactionManager.run {
            val repository = it.repository
            repository.getLocations()
        }

    }

    fun getReading(startDate: String, endDate: String, id: Int, readingType: String): List<Any> {
        return transactionManager.run {
            val repository = it.repository
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val newStartDate = LocalDate.parse(startDate, formatter)
            val newEndDate = LocalDate.parse(endDate, formatter)
            when (readingType) {
                "humidity" -> repository.getHumidity(newStartDate, newEndDate, id)
                "temperature" -> {
                    val list = repository.getTemperatures(newStartDate, newEndDate, id)
                    val result = mutableListOf<TemperatureWithPredictionType>()
                    for (temperature in list) {
                        result.add(
                            TemperatureWithPredictionType(
                                temperature.id,
                                temperature.dateHour,
                                temperature.location,
                                temperature.predictionId,
                                getPrediction(temperature.predictionId),
                                temperature.minValue,
                                temperature.maxValue
                            )
                        )
                    }
                    result
                }

                "levels" -> {
                    val list = repository.getLevelsAndConsumptions(newStartDate, newEndDate, id)
                    val result = mutableListOf<LevelWithPredictionType>()
                    for (level in list) {
                        result.add(
                            LevelWithPredictionType(
                                level.id,
                                level.date,
                                level.predictionId,
                                getPrediction(level.predictionId),
                                level.gasLevel,
                                level.location,
                                level.depositNumber,
                                level.counter,
                                level.consumption
                            )
                        )
                    }
                    result
                }

                else ->
                    mutableListOf()
            }

        }
    }

    fun getPrediction(id: Int): String {
        return transactionManager.run {
            val repository = it.repository
            repository.getPrediction(id) ?: ""
        }
    }

    fun fetchIpmaData() {
        return transactionManager.run {
            val minTemperatures =
                extractTemperature("https://api.ipma.pt/open-data/observation/climate/temperature-min/", "mtnmn")
            val maxTemperatures =
                extractTemperature("https://api.ipma.pt/open-data/observation/climate/temperature-max/", "mtxmx")
            val ipmaLocations = extractIpmaLocations()
            val ipmaStations = extractIpmaStations()

            val temperaturePredictions = extractTemperaturePredictions(ipmaLocations)
            val humidityEntries = extractHumidity(ipmaStations)

            insertTemperatures(minTemperatures, maxTemperatures)
            insertTemperaturePredictionData(temperaturePredictions)
            insertHumidityData(humidityEntries)
        }
    }

    fun getTempAndCons(startDate: String, endDate: String, id: Int): TemperatureAndConsumption {
        return transactionManager.run {
            val repository = it.repository
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val newStartDate = LocalDate.parse(startDate, formatter)
            val newEndDate = LocalDate.parse(endDate, formatter)
            val temperatures = repository.getRealestTemperatures(newStartDate, newEndDate, id)
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

    fun getDeliveries(startDate: String, endDate: String, id: Int): List<Delivery> {
        return transactionManager.run {
            val repository = it.repository
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val newStartDate = LocalDate.parse(startDate, formatter)
            val newEndDate = LocalDate.parse(endDate, formatter)
            val deliveries = repository.getDeliveries(newStartDate, newEndDate, id)
            deliveries
        }
    }

    fun getConsumptionPrediction(startDate: String, endDate: String): List<ConsumptionPrediction> {
        val objectMapper = ObjectMapper().apply {
            registerModule(JavaTimeModule()) // Register the JavaTimeModule for LocalDate support
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        }
        return transactionManager.run {

            val repository = it.repository
            val uags = repository.getLocations()
            val consumptionPredictionList = mutableListOf<ConsumptionPrediction>()
            for (uag in uags) {
                println(uag.id)
                val numberOfDeposits = repository.getNumberOfDeposits(uag.id)
                val training = repository.getTraining(uag.id) ?: continue
                val trainingJsonObject = JSONObject(training)
                val coefficientsJSONArray = trainingJsonObject.getJSONArray("Coefficients")

                val coefficients = mutableListOf<Double>()

                for (i in 0 until coefficientsJSONArray.length()) {
                    coefficients.add(coefficientsJSONArray.getDouble(i))
                }

                val intercept = trainingJsonObject.getDouble("Intercept")
                val temperatureAndConsumptions = getTempAndCons(startDate, endDate, uag.id)

                val temperaturesJson = objectMapper.writeValueAsString(temperatureAndConsumptions.temperatures)
                val consumptionsJson = objectMapper.writeValueAsString(temperatureAndConsumptions.consumptions)

                val consumptionPredictionsString =
                    invokePredictionAlgorithm(temperaturesJson, consumptionsJson, coefficients, intercept)
                val consumptionPredictionJSON = JSONArray(consumptionPredictionsString)


                for (i in 0 until consumptionPredictionJSON.length()) {
                    val jsonObject = consumptionPredictionJSON.getJSONObject(i)
                    val date = LocalDate.parse(jsonObject.getString("Data"))
                    val consumption = jsonObject.getDouble("Consumo")
                    consumptionPredictionList.add(ConsumptionPrediction(date, consumption))
                    for (j in 1..numberOfDeposits) {
                        if (!repository.checkIfConsumptionEntryExists(date.minusDays(1), uag.id, j)) continue
                        val lastConsumption = repository.getConsumptionByDate(date.minusDays(1), uag.id, j)

                        val newLevel = lastConsumption.gasLevel + consumption
                        repository.insertLevel(date, 11 + i, newLevel, uag.id, j, 0, consumption)
                    }

                }

            }
            consumptionPredictionList
        }

    }

    fun getTraining(startDate: String, endDate: String): List<Location> {

        val objectMapper = ObjectMapper().apply {
            registerModule(JavaTimeModule()) // Register the JavaTimeModule for LocalDate support
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        }

        return transactionManager.run {
            val repository = it.repository
            val uags = repository.getLocations()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val newStartDate = LocalDate.parse(startDate, formatter)
            val newEndDate = LocalDate.parse(endDate, formatter)
            for (uag in uags) {
                val temperatures = repository.getRealestTemperatures(newStartDate, newEndDate, uag.id)
                val consumptions = repository.getLevelsAndConsumptions(newStartDate, newEndDate, uag.id)

                if (temperatures.isEmpty() || consumptions.isEmpty()) {
                    continue
                }

                val temperaturesJson = objectMapper.writeValueAsString(temperatures)
                val consumptionsJson = objectMapper.writeValueAsString(consumptions)

                val trainingOutputJson = invokeTrainingAlgorithm(temperaturesJson, consumptionsJson)

                if (trainingOutputJson != "") repository.updateTraining(uag.id, trainingOutputJson)

            }
            repository.getLocations()
        }
    }

    fun insertUag(observation: String, name: String, distance: Double, latitude: Double, longitude: Double) {
        return transactionManager.run {
            val repository = it.repository
            if (repository.getLocationByName(name) == null) {
                repository.insertUag(observation, name, distance, latitude, longitude)
            } else {
                UagPostError.UagNameAlreadyExists
            }
        }

    }

    fun getLocationByName(name: String): Location? {
        return transactionManager.run {
            val repository = it.repository
            val location = repository.getLocationByName(name)
            location

        }
    }

    fun deleteLocationByName(name: String): Boolean {
        return transactionManager.run {
            val repository = it.repository
            val location = repository.getLocationByName(name)
            if (location != null) {
                repository.deleteLocationByName(name)
                true
            } else {
                false
            }

        }
    }
}
