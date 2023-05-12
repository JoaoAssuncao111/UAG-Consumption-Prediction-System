package uagpredictionsystem.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uagpredictionsystem.service.Service

@RestController
class Controller(private val service: Service) {

    @GetMapping(Uris.HOME)
    fun testHome(): ResponseEntity<*> {
        return ResponseEntity.status(200).body("")
    }

    @GetMapping(Uris.UAGS)
    fun getUags(): ResponseEntity<*> {
        val res = service.getUags()
        return ResponseEntity.status(200).body(res)
    }

    @GetMapping("/error")
    fun error(): ResponseEntity<*> {
        return ResponseEntity.status(200).body("")
    }
}