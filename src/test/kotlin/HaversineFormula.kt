
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.junit.jupiter.api.Test
import uagpredictionsystem.calculateDistance
import uagpredictionsystem.functions.extractIpmaLocations
import uagpredictionsystem.functions.extractTemperaturePredictions
import uagpredictionsystem.invokeTrainingAlgorithm
import uagpredictionsystem.models.Level
import uagpredictionsystem.models.Temperature
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Tests {
    @Test
    fun testHaversineFormula(): Unit {
        val temperatures = extractTemperaturePredictions(extractIpmaLocations())
        var minDistance: Double = Double.MAX_VALUE
        for (temperature in temperatures) {
            val distance = calculateDistance(
                37.152778,
                -8.525,
                temperature.key.latitude,
                temperature.key.longitude
            )
            if (distance < minDistance) minDistance = distance
            println("${temperature.key.name} distance: $distance")
        }
    }

    @Test
    fun invokeTest() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val temperatureList = mutableListOf(
            Temperature(1, LocalDate.parse("2023-04-01", formatter),1,0,13.0,20.0),
            Temperature(1, LocalDate.parse("2023-04-02", formatter),1,0,13.0,20.0),
            Temperature(1, LocalDate.parse("2023-04-03", formatter),1,0,13.0,20.0),
            Temperature(1, LocalDate.parse("2023-04-04", formatter),1,0,13.0,20.0),
            Temperature(1, LocalDate.parse("2023-04-05", formatter),1,0,13.0,20.0),
            Temperature(1, LocalDate.parse("2023-04-06", formatter),1,0,13.0,20.0),
            Temperature(1, LocalDate.parse("2023-04-07", formatter),1,0,13.0,20.0),
        )

        val consumptionsList = mutableListOf(
            Level(1,LocalDate.parse("2023-04-01", formatter),0,1.0,1,1,100,-10.0),
            Level(1,LocalDate.parse("2023-04-01", formatter),0,1.0,1,1,100,-10.0),
            Level(1,LocalDate.parse("2023-04-01", formatter),0,1.0,1,1,100,-10.0),
            Level(1,LocalDate.parse("2023-04-01", formatter),0,1.0,1,1,100,-10.0),
            Level(1,LocalDate.parse("2023-04-01", formatter),0,1.0,1,1,100,-10.0),
            Level(1,LocalDate.parse("2023-04-01", formatter),0,1.0,1,1,100,-10.0),
            Level(1,LocalDate.parse("2023-04-01", formatter),0,1.0,1,1,100,-10.0),

        )
        val objectMapper = ObjectMapper().apply {
            registerModule(JavaTimeModule()) // Register the JavaTimeModule for LocalDate support
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false) // Optional: Configure to write dates as ISO 8601 strings
        }

        val temperaturesJson = objectMapper.writeValueAsString(temperatureList)
        val consumptionsJson = objectMapper.writeValueAsString(consumptionsList)

        val result = invokeTrainingAlgorithm(temperaturesJson, consumptionsJson)
        println("Result JSON:\n$result")
    }
    @Test
    fun toList(){
        val list = mutableListOf(0.0,1.1,2.2,3.3)
        println(list.toString())
    }
}
