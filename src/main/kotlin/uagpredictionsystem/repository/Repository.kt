package uagpredictionsystem.repository

import uagpredictionsystem.models.Humidity
import uagpredictionsystem.models.Level
import uagpredictionsystem.models.Location
import uagpredictionsystem.models.Temperature
import java.time.LocalDate


interface Repository {

    fun getUags(): List<Location>

    fun getLevels(startDate: LocalDate, endDate: LocalDate, location: Int): List<Level>

    fun getLocationById(id: Int): Location?

    fun getTemperature(startDate: LocalDate, endDate: LocalDate, location: Int): List<Temperature>

    fun getHumidity(startDate: LocalDate, endDate: LocalDate, location: Int): List<Humidity>

    fun get9PastLevels(startDate: LocalDate, endDate: LocalDate, location: Int): List<Level>

    fun updateTraining(id: Int, training: String): Int
}