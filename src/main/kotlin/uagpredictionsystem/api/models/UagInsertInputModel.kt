package uagpredictionsystem.api.models

data class UagInsertInputModel(
                     val observation: String,
                     val name: String,
                     val distance: Double,
                     val latitude: Double,
                     val longitude: Double
)