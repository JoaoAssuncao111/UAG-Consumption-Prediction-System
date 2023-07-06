package uagpredictionsystem.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uagpredictionsystem.api.models.ReadingInputModel
import uagpredictionsystem.api.models.TrainingInputModel
import uagpredictionsystem.api.models.UagInsertInputModel
import uagpredictionsystem.service.Service
@CrossOrigin
@RestController
class Controller(private val service: Service) {
    
    @GetMapping(Uris.UAGS)
    fun getUags(): ResponseEntity<*> {
        val res = service.getUags()
        return ResponseEntity.status(200).body(res)
    }

    @GetMapping(Uris.READINGS)
    fun getReading(@PathVariable readingType: String,@ModelAttribute input: ReadingInputModel): ResponseEntity<*> {
        val res = service.getReading(input.startDate, input.endDate, input.location, readingType)
        return ResponseEntity.status(200).body(res)
    }
    @GetMapping(Uris.TEMP_CONSM)
    fun getTemperatureAndConsumption(@ModelAttribute input: ReadingInputModel): ResponseEntity<*> {
        val res = service.getTempAndCons(input.startDate, input.endDate, input.location)
        return ResponseEntity.status(200).body(res)
    }
    @GetMapping(Uris.TRAINING)
    fun getTraining(@ModelAttribute input: TrainingInputModel): ResponseEntity<*>{
        val res = service.getTraining(input.startDate,input.endDate)
        return ResponseEntity.status(200).body(res)
    }

    @PostMapping(Uris.UAGS)
    fun insertUag(@RequestBody input: UagInsertInputModel): ResponseEntity<*>{
        val res = service.insertUag(input.observation,input.name,input.distance,input.latitude,input.longitude)
        return ResponseEntity.status(200).body(res)
    }
    @GetMapping(Uris.UAG)
    fun getUagByName(@PathVariable name: String): ResponseEntity<*>{
        val res = service.getLocationByName(name)
        return ResponseEntity.status(200).body(res)
    }


}
