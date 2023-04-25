package functions
import DeliveryEntry
import LevelEntry
import headerLocations
import lines

fun extractObservations(lines: List<String>): List<String> {
    var rawObservations = ""
    for (i in 3..11) {
        rawObservations += lines[i]
    }
    val trimmedObservations = rawObservations.split(Regex("Obs:")).toMutableList()
    trimmedObservations.removeFirst()
    return trimmedObservations
}
fun extractLevelLocations(): List<MutableList<String>> {
    val rawLevelLocations1 = lines[13].split(Regex("\\t+")).toMutableList(); rawLevelLocations1.removeFirst()
    val rawLevelLocations2 = lines[12].split(Regex("\\t+")).toMutableList(); rawLevelLocations2.removeFirst()
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
    var linesIdx = 15

    while (linesIdx < lines.size) {
        var locationOffset = 3
        var hasCounter = true
        val currentLine = lines[linesIdx].split(Regex("\\t")).toMutableList()
        for (i in 0..locationDeposits.size) {

            val entry = LevelEntry(currentLine[1], "", 1, "", 0.0f, 0)
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
                    //Location and depoisit
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

fun extractDeliveryData(locations1: List<String>, locations2: List<String>): List<DeliveryEntry>{
    val deliveryData = mutableListOf<DeliveryEntry>()
    var linesIdx = 15
    //fix magic numbers via excel reading library if possible
    val portimaoOffset = 9
    val doubleDepositOffset = 10
    val singleDepositOffset = 6
    while (linesIdx < 30) {
        var deliveryIdx = 7 + locations1.size * 3 + locations2.size * 2
        val currentLine = lines[linesIdx].split(Regex("\\t")).toMutableList()
        for (i in 0 until headerLocations.size) {
            deliveryIdx += if (currentLine[deliveryIdx].contains("C")) {
                val company = currentLine[deliveryIdx + 2]
                val load = lines[13].split(Regex("\\t"))[deliveryIdx].split("%")[0].toFloat()
                val location = headerLocations[i]
                val timeOfDay = currentLine[deliveryIdx + 1]
                val date = currentLine[1]
                val entry = DeliveryEntry(company, load, location, timeOfDay, date)
                deliveryData.add(entry)
                if (currentLine[deliveryIdx + 6].matches(Regex("\\d{2}")) && headerLocations[i] == "Portimão") {
                    portimaoOffset
                } else if (currentLine[deliveryIdx + 6].matches(Regex("\\d{2}")) && headerLocations[i] != "Portimão") {
                    doubleDepositOffset
                } else {
                    singleDepositOffset
                }
            } else if (currentLine[deliveryIdx + 4].matches(Regex("\\d{2}")) && headerLocations[i] == "Portimão") {
                portimaoOffset
            } else if (currentLine[deliveryIdx + 4].matches(Regex("\\d{2}")) && headerLocations[i] != "Portimão") {
                doubleDepositOffset
            } else {
                singleDepositOffset
            }

        }
        linesIdx++
    }

    for (entry in deliveryData) {
        println(entry)
    }
    return deliveryData
}

