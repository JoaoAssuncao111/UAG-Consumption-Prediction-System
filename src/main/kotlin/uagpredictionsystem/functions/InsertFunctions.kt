package functions


import password
import uagpredictionsystem.TemperatureEntry
import uagpredictionsystem.functions.DeliveryEntry
import uagpredictionsystem.functions.LevelEntry
import uagpredictionsystem.replaceAccentedCharacters
import url
import user
import java.sql.*
import java.text.SimpleDateFormat

fun insertLocationData(
    url: String, user: String, password: String, locations: MutableList<String>, observations: List<String>
) {
    try {
        val locationConnection = DriverManager.getConnection(url, user, password)
        val sql = "INSERT INTO location (observation,name,distance) VALUES (?, ?, ?) " +
                "ON CONFLICT (observation, name, distance) DO NOTHING;"
        val preparedStatement: PreparedStatement = locationConnection.prepareStatement(sql)

        for (i in 0 until locations.size) {
            preparedStatement.setString(1, observations[i])
            preparedStatement.setString(2, locations[i])
            preparedStatement.setFloat(3, 0.0f)
            preparedStatement.executeUpdate()
        }
        preparedStatement.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun insertLevelData(levelData: List<LevelEntry>) {
    try {
        val levelConnection = DriverManager.getConnection(url, user, password)
        val insertLevelSql =
            "INSERT INTO level(date_hour, prediction_id, gas_level, location, deposit_number, counter) VALUES (?, ?, ?, ?, ?, ?)" +
                    "ON CONFLICT (date_hour, prediction_id, gas_level, location, deposit_number, counter) DO NOTHING;"
        val checkLocationSql = "SELECT * FROM location WHERE name = ?"
        val insertLocationSql = "INSERT INTO location (observation,name,distance) VALUES (?,?,?)"

        val insertLevelDataStatement = levelConnection.prepareStatement(insertLevelSql)

        levelData.forEach { entry ->
            val checkLocationStatement = levelConnection.prepareStatement(checkLocationSql)
            checkLocationStatement.setString(1, entry.location.split("-")[0].trim())
            val locationResultSet = checkLocationStatement.executeQuery()

            val locationId = if (locationResultSet.next()) {
                locationResultSet.getInt("id")
            } else {
                val insertLocationStatement =
                    levelConnection.prepareStatement(insertLocationSql, Statement.RETURN_GENERATED_KEYS)
                insertLocationStatement.setString(1, "")
                insertLocationStatement.setString(2, entry.location.split("-")[0].trim())
                insertLocationStatement.setFloat(3, 0.0f)
                insertLocationStatement.executeUpdate()

                val generatedKeys = insertLocationStatement.generatedKeys
                if (generatedKeys.next()) {
                    generatedKeys.getInt(1)
                } else {
                    throw SQLException("Inserting location failed, no ID obtained.")
                }
            }
            // Parse date string and convert to Date
            val dateFormat = SimpleDateFormat("EEE, dd/MM/yyyy")
            val date = Date(dateFormat.parse(entry.date.replace("\"", "")).time)

            insertLevelDataStatement.setDate(1, date)
            insertLevelDataStatement.setInt(2, 0)
            insertLevelDataStatement.setFloat(3, entry.level)
            insertLevelDataStatement.setInt(4, locationId)
            insertLevelDataStatement.setInt(5, entry.deposit)
            insertLevelDataStatement.setLong(6, entry.counter)
            insertLevelDataStatement.executeUpdate()
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun insertDeliveryData(deliveryData: List<DeliveryEntry>) {
    try {
        val deliveryConnection = DriverManager.getConnection(url, user, password)
        val insertDeliveryEntry =
            "INSERT INTO delivery(company, load_amount,location_id, time_of_day, date_hour) VALUES (?, ?, ?, ?, ?)" +
                    "ON CONFLICT (company, load_amount, location_id, time_of_day, date_hour) DO NOTHING;"
        val checkCompanySql = "SELECT * FROM company WHERE name = ?"
        val insertCompanySql = "INSERT INTO company (name,market_share,daily_fleet) VALUES (?,?,?)"
        val checkLocationSql = "SELECT * FROM location WHERE name = ?"
        val checkLocationStatement = deliveryConnection.prepareStatement(checkLocationSql)

        deliveryData.forEach { entry ->
            if (entry.company.contains("/")) {
                //TODO("multiple companies for same delivery")
            }
            val checkCompanyStatement = deliveryConnection.prepareStatement(checkCompanySql)
            checkCompanyStatement.setString(1, entry.company)
            val companyResultSet = checkCompanyStatement.executeQuery()

            val companyId = if (companyResultSet.next()) {
                companyResultSet.getInt("id")
            } else {
                val insertCompanyStatement =
                    deliveryConnection.prepareStatement(insertCompanySql, Statement.RETURN_GENERATED_KEYS)
                insertCompanyStatement.setString(1, entry.company)
                insertCompanyStatement.setFloat(2, 0.0f)
                insertCompanyStatement.setInt(3, 0)
                insertCompanyStatement.executeUpdate()

                val generatedKeys = insertCompanyStatement.generatedKeys
                if (generatedKeys.next()) {
                    generatedKeys.getInt(1)
                } else {
                    throw SQLException("Inserting company failed, no ID obtained.")
                }
            }

            checkLocationStatement.setString(1, entry.location)
            val locationResultSet = checkLocationStatement.executeQuery()

            val locationId = if (locationResultSet.next()) {
                locationResultSet.getInt("id")
            } else {
                throw SQLException("Inserting location failed, no ID obtained.")
            }

            val insertDeliveryDataStatement = deliveryConnection.prepareStatement(insertDeliveryEntry)
            // Parse date string and convert to Date
            val dateFormat = SimpleDateFormat("EEE, dd/MM/yyyy")
            val date = Date(dateFormat.parse(entry.date.replace("\"", "")).time)

            insertDeliveryDataStatement.setInt(1, companyId)
            insertDeliveryDataStatement.setFloat(2, entry.loadAmount)
            insertDeliveryDataStatement.setInt(3, locationId)
            insertDeliveryDataStatement.setString(4, entry.timeOfDay.trim())
            insertDeliveryDataStatement.setDate(5, date)
            insertDeliveryDataStatement.executeUpdate()
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }


}

fun insertTemperatureData(temperatureData: List<TemperatureEntry>) {
    try {

        val connection = DriverManager.getConnection(url, user, password)
        val insertDeliveryEntry =
            "INSERT INTO temperature(date_hour, location, prediction_id, min_value, max_value) VALUES (?, ?, ?, ?, ?)" +
                    "ON CONFLICT(date_hour, location, prediction_id, min_value, max_value) DO NOTHING;"
        val locations = mutableListOf<String>()
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("SELECT name FROM LOCATION")
        while(resultSet.next()){
            val value = replaceAccentedCharacters(resultSet.getString("name"))
            locations.add(value)
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
}




