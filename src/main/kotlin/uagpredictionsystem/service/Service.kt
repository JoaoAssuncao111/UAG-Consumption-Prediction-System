package uagpredictionsystem.service

import org.springframework.stereotype.Component
import uagpredictionsystem.repository.TransactionManager

sealed class UagGetError{
    object UagsNotFound: UagGetError()
}

@Component
class Service(private val transactionManager: TransactionManager) {

    fun getUags(): List<String> {
        return transactionManager.run {
            val repository = it.repository
            repository.getUags()
        }

    }
}