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

