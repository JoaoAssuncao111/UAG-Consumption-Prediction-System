import java.io.File
import java.nio.charset.Charset
import java.sql.*
import java.text.SimpleDateFormat
import functions.*

val excelFile = File("D:\\ISEL\\Projeto\\untitled1\\UAG_ Planeamento & Controlo.txt")
const val url = "jdbc:postgresql://localhost:5432/postgres"
const val user = "postgres"
const val password = "joaopedro123"
val lines = excelFile.readLines(Charset.forName("ISO-8859-1"))
val headerLocations = lines[2].split(Regex("\\t+")).toMutableList()
val observations = extractObservations(lines)

data class LevelEntry(
    var date: String,
    var location: String,
    var deposit: Int,
    var hour: String,
    var level: Float,
    var counter: Long
)

data class DeliveryEntry(
    var company: String,
    var loadAmount: Float,
    var location: String,
    var timeOfDay: String,
    var date: String
)

fun main() {
    headerLocations.removeFirst(); headerLocations.removeLast()
    insertLocationData(url, user, password, headerLocations, observations)
    val levelLocations = extractLevelLocations()
    val levelData = extractLevelData(levelLocations[0], levelLocations[2])
    insertLevelData(levelData)
    val deliveryData = extractDeliveryData(levelLocations[0],levelLocations[1])
    insertDeliveryData(deliveryData)
}











