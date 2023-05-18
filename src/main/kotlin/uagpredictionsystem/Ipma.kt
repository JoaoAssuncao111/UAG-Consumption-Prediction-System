package uagpredictionsystem

import uagpredictionsystem.functions.*

data class TemperatureEntry(
    val location: String,
    val date: String,
    val min: Double,
    val max: Double,
    val range: Double,
    val mean: Double,
    val std: Double
)

fun main() {
    val maxTemperatures =
        extractTemperature("https://api.ipma.pt/open-data/observation/climate/temperature-max/", "mtxmx")
    val minTemperature =
        extractTemperature("https://api.ipma.pt/open-data/observation/climate/temperature-min/", "mtnmn")
    val ipmaLocations = extractIpmaLocations()

    val ipmaStations = extractIpmaStations()

    val temperaturePredictions = extractTemperaturePredictions(ipmaLocations)
    val humidityEntries = extractHumidity(ipmaStations)

    for (element in humidityEntries) {
        println(element)
    }
}

