package uagpredictionsystem.repository

interface Transaction {

    val repository: Repository

    fun rollback()
}