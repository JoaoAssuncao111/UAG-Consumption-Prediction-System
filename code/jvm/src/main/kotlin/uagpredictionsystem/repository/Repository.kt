package uagpredictionsystem.repository

import org.jdbi.v3.core.statement.Update
import uagpredictionsystem.models.*
import java.time.LocalDate


interface Repository {

    fun getLocations(): List<Location>

    fun getLevelsAndConsumptions(startDate: LocalDate, endDate: LocalDate, location: Int): List<Level>

    fun getLocationById(id: Int): Location?

    fun getTemperatures(startDate: LocalDate, endDate: LocalDate, location: Int): List<Temperature>

    fun getHumidity(startDate: LocalDate, endDate: LocalDate, location: Int): List<Humidity>

    fun get9PastLevels(startDate: LocalDate, endDate: LocalDate, location: Int): List<Level>

    fun updateTraining(id: Int, training: String): Int

    fun insertUag(observation: String, name: String, distance: Double, latitude: Double, longitude: Double): Int

    fun getRealTemperatures(startDate: LocalDate, endDate: LocalDate, location: Int): List<Temperature>

    fun getRealestTemperatures(startDate: LocalDate, endDate: LocalDate, location: Int): MutableList<Temperature>

    fun getLocationByName(name: String): Location?

    fun deleteLocationByName(name: String): Boolean

    fun getDeliveries(startDate: LocalDate, endDate: LocalDate, location: Int): List<Delivery>

    fun getTraining(id: Int): String?

    fun insertLevel(
        dateHour: LocalDate,
        predictionId: Int,
        gasLevel: Double?,
        location: Int,
        depositNumber: Int,
        counter: Int?,
        consumption: Double
    ): Int

    fun getConsumptionByDate(dateHour: LocalDate, location: Int, depositNumber: Int): Level

    fun getNumberOfDeposits(location: Int): Int

    fun checkIfConsumptionEntryExists(dateHour: LocalDate, location: Int, depositNumber: Int): Boolean

    fun getPrediction(id: Int): String?

    fun updateLevel(date: LocalDate, id: Int, level: Double, consumption: Double, depositNumber: Int): Int
    fun getLevel(dateHour: LocalDate, location: Int, depositNumber: Int): Level?
}