package uagpredictionsystem.models

data class Location (
    val id: Int,
    val observation: String,
    val name: String,
    val distance: Double,
    val latitude: Double,
    val longitude: Double
)