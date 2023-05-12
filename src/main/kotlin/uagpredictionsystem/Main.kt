
import functions.*
import uagpredictionsystem.functions.extractDeliveryData
import uagpredictionsystem.functions.extractLevelData
import uagpredictionsystem.functions.extractLevelLocations
import uagpredictionsystem.functions.extractObservations
import java.io.File
import java.nio.charset.Charset

val excelFile = File("E:\\ISEL\\Projeto\\untitled1\\UAG_ Planeamento & Controlo.txt")
const val url = "jdbc:postgresql://localhost:5432/postgres"
const val user = "postgres"
const val password = "joaopedro123"
val lines = excelFile.readLines(Charset.forName("ISO-8859-1"))
val headerLocations = lines[2].split(Regex("\\t+")).toMutableList()
val observations = extractObservations(lines)



fun main() {
    headerLocations.removeFirst(); headerLocations.removeLast()
    insertLocationData(url, user, password, headerLocations, observations)
    val levelLocations = extractLevelLocations()
    val levelData = extractLevelData(levelLocations[0], levelLocations[2])
    insertLevelData(levelData)
    val deliveryData = extractDeliveryData(levelLocations[0],levelLocations[1])
    insertDeliveryData(deliveryData)
}













