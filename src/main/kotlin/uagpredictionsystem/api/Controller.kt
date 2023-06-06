package uagpredictionsystem.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uagpredictionsystem.api.models.ReadingInputModel
import uagpredictionsystem.service.Service

@RestController
class Controller(private val service: Service) {
    
    @GetMapping(Uris.UAGS)
    fun getUags(): ResponseEntity<*> {
        val res = service.getUags()
        return ResponseEntity.status(200).body(res)
    }

    //Table name in path variable
    @GetMapping(Uris.READINGS)
    fun getReading(@PathVariable readingType: String,@ModelAttribute input: ReadingInputModel): ResponseEntity<*> {
        val res = service.getReading(input.startDate, input.endDate, input.location, readingType)
        return ResponseEntity.status(200).body(res)
    }
}
