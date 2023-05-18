package uagpredictionsystem.repository

import uagpredictionsystem.functions.LevelEntry
import uagpredictionsystem.models.Humidity
import uagpredictionsystem.models.Location
import uagpredictionsystem.models.Temperature
import java.time.LocalDate


interface Repository {

    fun getUags(): List<Location>

    fun getLevels(startDate: LocalDate, endDate: LocalDate, location: Int): List<LevelEntry>

    fun getLocationById(id: Int): Location?

    fun getTemperature(startDate: LocalDate, endDate: LocalDate, location: Int): List<Temperature>

    fun getHumidity(startDate: LocalDate, endDate: LocalDate, location: Int): List<Humidity>
}