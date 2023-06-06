
import org.junit.jupiter.api.Test
import uagpredictionsystem.calculateDistance
import uagpredictionsystem.functions.extractIpmaLocations
import uagpredictionsystem.functions.extractTemperaturePredictions
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
}
