package uagpredictionsystem.repository.jdbi

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import uagpredictionsystem.repository.Transaction;
import uagpredictionsystem.repository.TransactionManager;


@Component
internal class JdbiTransactionManager(private val jdbi: Jdbi) : TransactionManager {

    override fun <R> run(block: (Transaction) -> R): R =
        jdbi.inTransaction<R, Exception> { handle ->
            val transaction = JdbiTransaction(handle)
            block(transaction)
        }
}