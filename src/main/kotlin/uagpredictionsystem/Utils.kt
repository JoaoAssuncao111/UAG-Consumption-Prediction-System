package uagpredictionsystem

import uagpredictionsystem.models.Location
import java.sql.Connection
import java.text.Normalizer
import kotlin.math.*


sealed class Either<out L, out R> {
    data class Left<out L>(val value: L) : Either<L, Nothing>()
    data class Right<out R>(val value: R) : Either<Nothing, R>()
}

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

fun getAllLocationsFromDb(connection: Connection): HashMap<String,Location>{
    val locations = hashMapOf<String,Location>()
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
