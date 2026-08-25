package cz.sotona.dealerapi.controller

import cz.sotona.dealerapi.dto.CreateDealerRequest
import cz.sotona.dealerapi.dto.DealerStatistics
import cz.sotona.dealerapi.model.Dealer
import cz.sotona.dealerapi.model.Loyalty
import cz.sotona.dealerapi.service.DealerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/dealers")
class DealerController(
    private val service: DealerService
) {

    @PostMapping
    fun addDealer(
        @RequestBody request: CreateDealerRequest
    ): Dealer {
        return service.addDealer(
            request.nickname,
            request.yearsInPrison,
            request.loyalty,
            request.strength,
            request.iq
        )
    }

    @GetMapping
    fun getAllDealers(): List<Dealer> =
        service.allDealers()

    @GetMapping("/elite-dealers")
    fun eliteDealers(): List<Dealer> =
        service.eliteDealers()

    @GetMapping("/statistics")
    fun statistics(): DealerStatistics =
        service.statistics()

    @GetMapping("/id/{id}")
    fun getDealer(
        @PathVariable id: Int
    ): ResponseEntity<Any> {
        val dealer = service.findById(id)
        return if (dealer == null) {
            ResponseEntity
                .status(404)
                .body("Dealer s ID $id nebyl nalezen.")
        } else {
            ResponseEntity.ok(dealer)
        }
    }

    @DeleteMapping("/id/{id}")
    fun removeDealer(
        @PathVariable id: Int
    ): ResponseEntity<Void> {
        return if (service.removeDealer(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/nickname/{nickname}")
    fun searchDealerByNickname(
        @PathVariable nickname: String
    ): List<Dealer> =
        service.findByNickname(nickname)

    @GetMapping("/text/{text}")
    fun searchDealerByText(
        @PathVariable text: String
    ): List<Dealer> =
        service.dealersByTextInNickname(text)

    @GetMapping("/loyalty/{loyalty}")
    fun searchDealerByLoyalty(
        @PathVariable loyalty: Loyalty
    ): List<Dealer> =
        service.dealersByLoyalty(loyalty)
}