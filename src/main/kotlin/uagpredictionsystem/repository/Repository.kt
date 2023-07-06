package uagpredictionsystem.repository

import uagpredictionsystem.models.Humidity
import uagpredictionsystem.models.Level
import uagpredictionsystem.models.Location
import uagpredictionsystem.models.Temperature
import java.time.LocalDate


interface Repository {
    //Gets all UAG locations
    fun getLocations(): List<Location>

    //Gets
    fun getLevelsAndConsumptions(startDate: LocalDate, endDate: LocalDate, location: Int): List<Level>

    fun getLocationById(id: Int): Location?

    fun getTemperature(startDate: LocalDate, endDate: LocalDate, location: Int): List<Temperature>

    fun getHumidity(startDate: LocalDate, endDate: LocalDate, location: Int): List<Humidity>

    fun get9PastLevels(startDate: LocalDate, endDate: LocalDate, location: Int): List<Level>

    fun updateTraining(id: Int, training: String): Int

    fun insertUag(observation: String, name: String, distance: Double, latitude: Double, longitude: Double): Int

    fun getRealTemperatures(startDate: LocalDate, endDate: LocalDate, location: Int): List<Temperature>

    fun getRealestTemperatures(startDate: LocalDate, endDate: LocalDate, location: Int): MutableList<Temperature>

    fun insertConsumption(dateHour: LocalDate,gasLevel: Double,location: Int,depositNumber: Int,counter: Int?,consumption: Double): Int
    fun getLocationByName(name: String): Location?
    fun deleteLocationByName(name: String): Boolean
}