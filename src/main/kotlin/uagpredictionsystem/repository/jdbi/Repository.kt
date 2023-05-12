package uagpredictionsystem.repository.jdbi

import org.jdbi.v3.core.Handle
import uagpredictionsystem.repository.Repository

class Repository(
    private val handle: Handle
) : Repository{

    override fun getUags(): List<String> {
        return handle.createQuery("select name from location")
            .mapTo(String::class.java)
            .list()
    }
}