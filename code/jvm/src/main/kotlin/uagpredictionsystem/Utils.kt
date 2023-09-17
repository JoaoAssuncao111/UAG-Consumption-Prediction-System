package uagpredictionsystem

import uagpredictionsystem.models.Location
import java.io.BufferedReader
import java.io.InputStreamReader
import java.sql.Connection
import java.text.Normalizer
import kotlin.math.*

fun replaceAccentedCharacters(text: String): String {
    val normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD)
    val withoutAccents = normalizedText
        .replace("[^\\p{ASCII}]".toRegex(), "")
        .replace("[^A-Za-z0-9 ]".toRegex(), "")
        .replace("\\s+".toRegex(), " ")
    return withoutAccents.toLowerCase()
}

fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val earthRadius = 6371 // Radius of the Earth in kilometers

    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)

    val a = sin(dLat / 2).pow(2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    //Return in meters
    return earthRadius * c
}

fun getAllLocationsFromDb(connection: Connection): HashMap<String, Location> {
    val locations = hashMapOf<String, Location>()
    val statement = connection.createStatement()
    val resultSet =
        statement.executeQuery("SELECT * FROM LOCATION WHERE latitude IS NOT NULL AND longitude IS NOT NULL; ")
    while (resultSet.next()) {
        val id = resultSet.getInt("id")
        val observation = resultSet.getString("observation")
        val name = resultSet.getString("name")
        val distance = resultSet.getDouble("distance")
        val latitude = resultSet.getDouble("latitude")
        val longitude = resultSet.getDouble("longitude")
        val key = replaceAccentedCharacters(name)
        locations[key] = Location(id, observation, name, distance, latitude, longitude)
    }
    return locations
}

fun invokeTrainingAlgorithm(temperatures: String, consumptions: String): String {
    //val pythonScript = "E:\\ISEL\\Projeto\\uag-prediction-system\\code\\jvm\\python_scripts\\ModuloTreino.py"
    val pythonScript = "/app/python_scripts/ModuloTreino.py"
    //val escapedTemperatures = temperatures.replace("\"", "\\\"")
    //val escapedConsumptions = consumptions.replace("\"", "\\\"")

    val processBuilder = ProcessBuilder("python", pythonScript, temperatures, consumptions)

    processBuilder.redirectErrorStream(true)
    val process = processBuilder.start()

    val reader = BufferedReader(InputStreamReader(process.inputStream))
    var line: String?
    var lastLine: String? = null
    while (reader.readLine().also { line = it } != null) {
        lastLine = line
    }

    val exitCode = process.waitFor()
    if (exitCode == 0) {
        println("Python script executed successfully.")
    } else {
        println("Python script encountered an error. Exit code: $exitCode")
    }

    return lastLine ?: "{}"
}

fun invokePredictionAlgorithm(
    temperatures: String,
    consumptions: String,
    coefs: List<Double>,
    intercept: Double
): String {
    //val pythonScript = "E:\\ISEL\\Projeto\\uag-prediction-system\\code\\jvm\\python_scripts\\PrevisaoResultados.py"
    val pythonScript = "/app/python_scripts/PrevisaoResultados.py"
    val escapedTemperatures = temperatures.replace("\"", "\\\"")
    val escapedConsumptions = consumptions.replace("\"", "\\\"")


    val processBuilder = ProcessBuilder(
        "python",
        pythonScript,
        temperatures,
        consumptions,
        coefs.toString(),
        intercept.toString()
    )

    processBuilder.redirectErrorStream(true)
    val process = processBuilder.start()

    val reader = BufferedReader(InputStreamReader(process.inputStream))
    var line: String?
    var lastLine: String? = null
    while (reader.readLine().also { line = it } != null) {
        println(line)
        lastLine = line
    }

    val exitCode = process.waitFor()
    if (exitCode == 0) {
        println("Python script executed successfully.")
    } else {
        println("Python script encountered an error. Exit code: $exitCode")
    }

    return lastLine ?: "{}"
}

fun invokePythonTest(): String{
    //val pythonScript = "E:\\ISEL\\Projeto\\uag-prediction-system\\code\\jvm\\python_scripts\\PythonTest.py"
    val pythonScript = "/app/python_scripts/PythonTest.py"
    val processBuilder = ProcessBuilder(
        "python",
        pythonScript
    )

    processBuilder.redirectErrorStream(true)
    val process = processBuilder.start()

    val reader = BufferedReader(InputStreamReader(process.inputStream))
    var line: String?
    var lastLine: String? = null
    while (reader.readLine().also { line = it } != null) {
        println(line)
        lastLine = line
    }

    val exitCode = process.waitFor()
    if (exitCode == 0) {
        println("Python script executed successfully.")
    } else {
        println("Python script encountered an error. Exit code: $exitCode")
    }

    return lastLine ?: "{}"
}

