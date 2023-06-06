package uagpredictionsystem.functions

import headerLocations
import lines
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import uagpredictionsystem.TemperatureEntry

data class LevelEntry(
    var date: String,
    var location: String,
    var deposit: Int,
    var hour: String,
    var level: Float,
    var counter: Long,
    var consumption: Double
)

data class DeliveryEntry(
    var company: String,
    var loadAmount: Float,
    var location: String,
    var timeOfDay: String,
    var date: String
)

data class ConsumptionUpdateEntry(
    var consumption: Double,
    var location: String,
    var date: String
)

data class IpmaLocation(
    var globalId: Int,
    var name: String,
    var latitude: Double,
    var longitude: Double

)

data class TemperaturePredictionEntry(
    val tMin: Double,
    val tMax: Double,
    val date: String,
    val predictedDay: Int
)

//Prediction is always real (0)
data class HumidityEntry(
    val value: Double,
    val date: String
)

fun extractObservations(lines: List<String>): List<String> {
    var rawObservations = ""
    for (i in 3..18) {
        rawObservations += lines[i]
    }
    var trimmedObservations = rawObservations.split(Regex("Obs:")).toMutableList()
    trimmedObservations.removeFirst()
    trimmedObservations = trimmedObservations.map { it.replace("\t", "") }.toMutableList()
    return trimmedObservations
}

fun extractLevelLocations(): List<MutableList<String>> {
    val rawLevelLocations1 = lines[20].split(Regex("\\t+"))
        .toMutableList(); rawLevelLocations1.removeFirst();
    val rawLevelLocations2 = lines[19].split(Regex("\\t+")).toMutableList(); rawLevelLocations2.removeFirst()
    val locationDeposits = mutableListOf<String>()
    val depositNumbers = mutableListOf<String>()
    val locations1 = mutableListOf<String>()
    val locations2 = mutableListOf<String>()

    for (value in rawLevelLocations1) {
        if ((!value.contains("Nível")) && !value.matches("[^a-zA-Z]+".toRegex())) {
            locations1.add(value)
            locationDeposits.add(value)
        } else if (value.contains("Nível")) {
            depositNumbers.add(value)
        } else if (depositNumbers.isNotEmpty()) break
    }

    for (value in rawLevelLocations2) {
        if (!value.contains("Carga")) {
            locations2.add(value)
        } else break
    }

    locations2.removeFirst()
    var prevLocation = locations2.last()
    var deposit = 0

    val formattedLocations2 = depositNumbers.mapIndexed { index, level ->
        if (level.endsWith("1")) {
            prevLocation = locations2[(locations2.indexOf(prevLocation) + 1) % locations2.size]
            deposit = 1
        } else {
            deposit++
        }
        "$prevLocation - Depósito $deposit"
    }
    for (location in formattedLocations2) {
        locationDeposits.add(location)
    }
    return listOf(locations1, formattedLocations2.toMutableList(), locationDeposits)
}


fun extractLevelData(
    locations1: MutableList<String>,
    locationDeposits: MutableList<String>
): MutableList<LevelEntry> {

    val levelData: MutableList<LevelEntry> = mutableListOf()
    var linesIdx = 871 //22

    while (linesIdx < lines.size) {
        var locationOffset = 3
        var hasCounter = true
        val currentLine = lines[linesIdx].split(Regex("\\t")).toMutableList()
        for (i in 0..locationDeposits.size) {

            val entry = LevelEntry(currentLine[1], "", 1, "", 0.0f, 0, 0.0)

            if (hasCounter) {
                if ((currentLine[locationOffset + 1] == "0.00" || currentLine[locationOffset + 1] == "") && i < locations1.size) {
                    locationOffset += 3
                    continue
                    //No readings for this location, skip
                } else {
                    if (currentLine[locationOffset] == "") {
                        locationOffset += 3
                        hasCounter = false;
                        continue
                    }
                    //Location and deposit
                    if (locationDeposits[i].contains("-")) {
                        entry.location = locationDeposits[i].split("-")[0]
                        entry.deposit = locationDeposits[i].substringAfterLast(" ").trim().toInt()
                    } else {
                        entry.location = locationDeposits[i]
                    }
                    //Hour
                    entry.hour = currentLine[locationOffset].split(" ")[1]
                    //Level
                    entry.level = currentLine[locationOffset + 1].toFloat()
                    //Counter
                    if (currentLine[locationOffset + 2] == "") entry.counter = 0
                    else
                        entry.counter =
                            currentLine[locationOffset + 2].replace(",", "").replace("\"", "").trim().toLong()

                    levelData.add(entry)
                }
                locationOffset += 3
                //has no counter
            } else {
                if ((!currentLine[locationOffset].contains("-")) && currentLine[locationOffset] != "") {
                    entry.location = locationDeposits[i - 1]
                    entry.level = currentLine[locationOffset].toFloat()
                    levelData.add(entry)
                }
                locationOffset += 2
            }
        }
        linesIdx++
    }
    return levelData
}

//Also extracts consumption level, not optimal maybe fix later
fun extractDeliveryData(
    locations1: List<String>,
    locations2: List<String>
): Pair<List<DeliveryEntry>, List<ConsumptionUpdateEntry>> {
    val deliveryData = mutableListOf<DeliveryEntry>()
    val consumptionList = mutableListOf<ConsumptionUpdateEntry>()
    var linesIdx = 22//22
    //fix magic numbers via excel reading library if possible
    val portimaoOffset = 9
    val doubleDepositOffset = 10
    val singleDepositOffset = 6
    while (linesIdx < lines.size) {
        var deliveryIdx = 7 + locations1.size * 3 + locations2.size * 2
        val currentLine = lines[linesIdx].split(Regex("\\t")).toMutableList()
        val date = currentLine[1]

        if (!currentLine[1].contains("/")) break
        for (i in 0 until headerLocations.size) {
            val location = headerLocations[i]
            //Contains a Delivery
            deliveryIdx += if (currentLine[deliveryIdx].contains("C")) {
                val company = currentLine[deliveryIdx + 2]
                val load =
                    lines[20].split(Regex("\\t"))[deliveryIdx].split("%")[0].toFloat() * currentLine[deliveryIdx].split(
                        "C"
                    )[0].toFloat()
                val timeOfDay = currentLine[deliveryIdx + 1]
                val entry = DeliveryEntry(company, load, location, timeOfDay, date)
                deliveryData.add(entry)

                if (currentLine[deliveryIdx + 6].matches(Regex("\\d{2}")) && headerLocations[i] == "Portimão") {
                    val number = currentLine[deliveryIdx + 8].toDoubleOrNull()
                    if (number != null) {
                        consumptionList.add(
                            ConsumptionUpdateEntry(
                                currentLine[deliveryIdx + 8].toDouble(),
                                location,
                                date
                            )
                        )
                    }
                    portimaoOffset
                } else if (currentLine[deliveryIdx + 6].matches(Regex("\\d{2}")) && headerLocations[i] != "Portimão") {
                    val number = currentLine[deliveryIdx + 7].toDoubleOrNull()
                    if (number != null) {
                        consumptionList.add(
                            ConsumptionUpdateEntry(
                                currentLine[deliveryIdx + 7].toDouble(),
                                location,
                                date
                            )
                        )
                    }
                    doubleDepositOffset
                } else {
                    val number = currentLine[deliveryIdx + 5].toDoubleOrNull()
                    if (number != null) {
                        consumptionList.add(
                            ConsumptionUpdateEntry(
                                currentLine[deliveryIdx + 5].toDouble(),
                                location,
                                date
                            )
                        )
                    }
                    singleDepositOffset
                }
            } else if (currentLine[deliveryIdx + 4].matches(Regex("\\d{2}")) && headerLocations[i] == "Portimão") {
                val number = currentLine[deliveryIdx + 8].toDoubleOrNull()
                if (number != null) {
                    consumptionList.add(
                        ConsumptionUpdateEntry(
                            currentLine[deliveryIdx + 8].toDouble(),
                            location,
                            date
                        )
                    )
                }
                portimaoOffset
            } else if (currentLine[deliveryIdx + 4].matches(Regex("\\d{2}")) && headerLocations[i] != "Portimão") {

                val number = currentLine[deliveryIdx + 7].toDoubleOrNull()
                if (number != null) {
                    consumptionList.add(
                        ConsumptionUpdateEntry(
                            currentLine[deliveryIdx + 7].toDouble(),
                            location,
                            date
                        )
                    )
                }
                doubleDepositOffset
            } else {
                val number = currentLine[deliveryIdx + 5].toDoubleOrNull()
                if (number != null) {
                    consumptionList.add(
                        ConsumptionUpdateEntry(
                            currentLine[deliveryIdx + 5].toDouble(),
                            location,
                            date
                        )
                    )
                }
                singleDepositOffset
            }
        }
        println(deliveryData.last())
        linesIdx++
    }

    for (entry in deliveryData) {
        println(entry)
    }
    return Pair(deliveryData, consumptionList)
}

fun extractTemperature(temperatureUrl: String, temperatureSignature: String): MutableList<TemperatureEntry> {

    val client = OkHttpClient()
    val districts = listOf(
        "aveiro", "beja", "braga", "braganca", "castelo-branco", "coimbra", "evora", "faro", "guarda",
        "leiria", "lisboa", "portalegre", "porto", "santarem", "setubal", "viana-do-castelo", "vila-real", "viseu"
    )
    //Key -> location name
    //Value -> .csv file endpoint
    val files = mutableMapOf<String, String>()
    val temperatures = mutableListOf<TemperatureEntry>()
    for (district in districts) {
        val url = temperatureUrl + district
        val fileLinkExtractionRequest =
            Request.Builder().url(url)
                .build()

        client.newCall(fileLinkExtractionRequest).execute().use { response ->
            if (!response.isSuccessful) {
                throw Exception("Unexpected code $response")
            } else {
                val lines = response.body?.string()?.split("\n")
                val filesLine = lines?.filter { it.contains(temperatureSignature) }
                if (filesLine != null) {
                    for (line in filesLine) {
                        val fileName = line.split("href=\"")[1].replace("\"", "").split(">")[0]
                        val pattern = Regex("\\d{4}-(.*)")
                        val key = pattern.find(fileName)?.groupValues?.get(1)?.replace(".csv", "")?.replace("-", " ")
                        if (key != null) files[key] = "$url/$fileName"
                    }
                }
            }

        }
    }

    for (file in files) {
        val location = file.key
        val fileReadRequest = Request.Builder().url(file.value).build()
        client.newCall(fileReadRequest).execute().use { response ->
            if (!response.isSuccessful) {
                throw Exception("Unexpected code $response")
            } else {
                val lines = response.body?.string()?.split("\n")?.toMutableList()
                lines?.removeFirst()
                lines?.removeLast()
                if (lines != null) {
                    for (line in lines) {
                        val values = line.split(",")
                        val date = values[0]
                        val min = values[1].toDouble()
                        val max = values[2].toDouble()
                        val range = values[3].toDouble()
                        val mean = values[4].toDouble()
                        val std = values[5].toDouble()
                        temperatures.add(TemperatureEntry(location, date, min, max, range, mean, std))
                    }
                }
            }
        }

    }
    return temperatures
}

//temperature related
fun extractIpmaLocations(): HashMap<String, IpmaLocation> {
    val client = OkHttpClient()
    val request = Request.Builder().url("https://api.ipma.pt/open-data/distrits-islands.json").build()
    val result = hashMapOf<String, IpmaLocation>()

    val response = client.newCall(request).execute()
    val jsonData = response.body?.string()
    val json = JSONObject(jsonData)
    val data = json.getJSONArray("data")
    for (i in 0 until data.length()) {
        val locationObj = data.getJSONObject(i)
        val name = locationObj.getString("local")
        val latitude = locationObj.getDouble("latitude")
        val longitude = locationObj.getDouble("longitude")
        val globalId = locationObj.getInt("globalIdLocal")
        result[name] = IpmaLocation(globalId, name, latitude, longitude)
    }
    return result
}

//humidity related
fun extractIpmaStations(): HashMap<Int, IpmaLocation> {
    val client = OkHttpClient()
    val request =
        Request.Builder().url("https://api.ipma.pt/open-data/observation/meteorology/stations/stations.json").build()
    val result = hashMapOf<Int, IpmaLocation>()

    val response = client.newCall(request).execute()
    val jsonData = response.body?.string()
    val json = JSONArray(jsonData)
    for (i in 0 until json.length()) {
        val jsonObject = json.getJSONObject(i)
        val longitude = jsonObject.getJSONObject("geometry").getJSONArray("coordinates").getDouble(0)
        val latitude = jsonObject.getJSONObject("geometry").getJSONArray("coordinates").getDouble(1)
        val localEstacao = jsonObject.getJSONObject("properties").getString("localEstacao")
        val idEstacao = jsonObject.getJSONObject("properties").getInt("idEstacao")
        result[idEstacao] = (IpmaLocation(idEstacao, localEstacao, latitude, longitude))

    }
    return result
}

fun extractTemperaturePredictions(locations: HashMap<String, IpmaLocation>): HashMap<IpmaLocation, List<TemperaturePredictionEntry>> {
    val client = OkHttpClient()
    val baseUrl = "http://api.ipma.pt/open-data/forecast/meteorology/cities/daily/"
    val result = hashMapOf<IpmaLocation, List<TemperaturePredictionEntry>>()
    for (location in locations) {
        val request =
            Request.Builder()
                .url("${baseUrl}${location.value.globalId}.json")
                .build()
        val response = client.newCall(request).execute()
        val jsonData = response.body?.string()
        val json = JSONObject(jsonData)
        val data = json.getJSONArray("data")
        val temperaturePredictions = mutableListOf<TemperaturePredictionEntry>()

        for (i in 0 until data.length()) {
            val jsonObject = data.getJSONObject(i)
            val minTemperature = jsonObject.getDouble("tMin")
            val maxTemperature = jsonObject.getDouble("tMax")
            val date = jsonObject.getString("forecastDate")
            temperaturePredictions.add(TemperaturePredictionEntry(minTemperature, maxTemperature, date, i))
        }
        result[location.value] = temperaturePredictions

    }
    return result

}

fun extractHumidity(stations: HashMap<Int, IpmaLocation>): HashMap<IpmaLocation, MutableList<HumidityEntry>> {
    val humidityMap = hashMapOf<IpmaLocation, MutableList<HumidityEntry>>()
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://api.ipma.pt/open-data/observation/meteorology/stations/observations.json")
        .build()

    val response = client.newCall(request).execute()

    if (response.isSuccessful) {
        val jsonData = response.body?.string()
        val data = JSONObject(jsonData)

        for (timestampKey in data.keys()) {
            val timestampObject = data.getJSONObject(timestampKey)

            for (numberKey in timestampObject.keys()) {
                val value = timestampObject.get(numberKey)
                if (value is JSONObject) {
                    val numberObject = value as JSONObject
                    val humidity = numberObject.getDouble("humidade")
                    val station = stations[numberKey.toInt()]
                    val humidityEntry = HumidityEntry(humidity, timestampKey)

                    if (station != null) {
                        val entries = humidityMap.getOrDefault(station, mutableListOf())
                        entries.add(humidityEntry)
                        humidityMap[station] = entries
                    }
                }
            }
        }
    }
    return humidityMap
}