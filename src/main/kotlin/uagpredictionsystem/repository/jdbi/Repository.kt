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

    override fun getLevels(startDate: LocalDate, endDate: LocalDate, location: Int): List<Level> {
        return handle.createQuery("select * from LEVEL WHERE date_hour" +
                " BETWEEN :startDate AND :endDate AND location = :location")
            .bind("startDate", startDate)
            .bind("endDate", endDate)
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

    override fun getHumidity(startDate: LocalDate, endDate: LocalDate, location: Int): List<Humidity> {
        return handle.createQuery("select * from humidity WHERE date_hour" +
                " BETWEEN :startDate AND :endDate AND location = :location")
            .bind("startDate", startDate)
            .bind("endDate", endDate)
            .bind("location",location)
            .mapTo(Humidity::class.java)
            .list()
    }

}