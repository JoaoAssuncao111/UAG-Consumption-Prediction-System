package uagpredictionsystem.repository.jdbi

import org.jdbi.v3.core.Handle
import uagpredictionsystem.models.Humidity
import uagpredictionsystem.models.Level
import uagpredictionsystem.models.Location
import uagpredictionsystem.models.Temperature
import uagpredictionsystem.repository.Repository
import java.time.LocalDate

class Repository(
    private val handle: Handle
) : Repository {

    override fun getUags(): List<Location> {
        return handle.createQuery("select * from location")
            .mapTo(Location::class.java)
            .list()
    }

    override fun getLevelsAndConsumptions(startDate: LocalDate, endDate: LocalDate, location: Int): List<Level> {
        return handle.createQuery("select * from LEVEL WHERE date_hour" +
                " BETWEEN :startDate AND :endDate AND location = :location")
            .bind("startDate", startDate)
            .bind("endDate", endDate)
            .bind("location",location)
            .mapTo(Level::class.java)
            .list()
    }

    override fun get9PastLevels(startDate: LocalDate, endDate: LocalDate, location: Int): List<Level> {
        val newStartDate = startDate.minusDays(9)
        return handle.createQuery("select * from LEVEL WHERE date_hour" +
                " BETWEEN :startDate AND :endDate AND location = :location")
            .bind("startDate", newStartDate)
            .bind("endDate", startDate)
            .bind("location",location)
            .mapTo(Level::class.java)
            .list()
    }

    override fun getLocationById(id: Int): Location? {
        return handle.createQuery("select * from location where id = :id")
            .bind("id",id)
            .mapTo(Location::class.java)
            .singleOrNull()
    }

    override fun getTemperature(startDate: LocalDate, endDate: LocalDate, location: Int): List<Temperature> {
        return handle.createQuery("select * from temperature WHERE date_hour" +
                " BETWEEN :startDate AND :endDate AND location = :location")
            .bind("startDate", startDate)
            .bind("endDate", endDate)
            .bind("location",location)
            .mapTo(Temperature::class.java)
            .list()
    }

    override fun getRealTemperatures(startDate: LocalDate, endDate: LocalDate, location: Int): List<Temperature> {
        return handle.createQuery("select * from temperature WHERE date_hour" +
                " BETWEEN :startDate AND :endDate AND location = :location AND prediction_id == 0")
            .bind("startDate", startDate)
            .bind("endDate", endDate)
            .bind("location",location)
            .mapTo(Temperature::class.java)
            .list()
    }

    override fun getRealestTemperatures(startDate: LocalDate, endDate: LocalDate, location: Int): MutableList<Temperature> {
        return handle.createQuery("SELECT DISTINCT ON (date_hour) " +
                "FROM temperature " +
                "WHERE date_hour >= :startDate" +
                "  AND date_hour <= :endDate + INTERVAL '1 day'" +
                "and location = :location " +
                "ORDER BY date_hour, prediction_id;")

            .bind("startDate", startDate)
            .bind("endDate", endDate)
            .bind("location",location)
            .mapTo(Temperature::class.java)
            .list()
    }

    override fun getHumidity(startDate: LocalDate, endDate: LocalDate, location: Int): List<Humidity> {
        return handle.createQuery("select * from humidity WHERE date_hour" +
                " BETWEEN :startDate AND :endDate AND location = :location")
            .bind("startDate", startDate)
            .bind("endDate", endDate)
            .bind("location",location)
            .mapTo(Humidity::class.java)
            .list()
    }

    override fun updateTraining(id: Int,training: String): Int {
        return handle.createUpdate("update location set training = CAST(:training AS json) where id = :id")
            .bind("id",id)
            .bind("training",training)
            .execute()
    }

    override fun insertUag(observation: String, name: String, distance: Double, latitude: Double, longitude: Double): Int {
        return handle.createUpdate( "insert into location values (:observation, :name,:distance,:latitude,:longitude,null)")
            .bind("observation",observation)
            .bind("name",name)
            .bind("distance",distance)
            .bind("latitude",latitude)
            .bind("longitude",longitude)
            .execute()

    }


}