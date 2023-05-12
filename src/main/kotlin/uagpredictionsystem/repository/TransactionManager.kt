package uagpredictionsystem.repository

import org.springframework.stereotype.Component


interface TransactionManager {
    fun <R> run(block: (Transaction) -> R): R
}