package uagpredictionsystem.models

data class TemperatureAndConsumption(
    val temperatures: List<Temperature>,
    val consumptions:List<FilteredConsumptions>
)
