package uagpredictionsystem.repository.jdbi

import org.jdbi.v3.core.Handle
import uagpredictionsystem.repository.Repository
import uagpredictionsystem.repository.Transaction

class JdbiTransaction(
    private val handle: Handle
) : Transaction {

    override val repository: Repository by lazy { Repository(handle) }

    override fun rollback() {
        handle.rollback()
    }
}
