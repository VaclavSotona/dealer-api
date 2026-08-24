package cz.sotona.dealerapi.controller

import cz.sotona.dealerapi.dto.CreateDealerRequest
import cz.sotona.dealerapi.dto.DealerStatistics
import cz.sotona.dealerapi.model.Dealer
import cz.sotona.dealerapi.model.Loyalty
import cz.sotona.dealerapi.repository.DealerRegistry
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/dealers")
class DealerController(
    private val registry: DealerRegistry
) {

    @PostMapping
    fun addDealer(
        @RequestBody request: CreateDealerRequest
    ): Dealer {
        return registry.addDealer(
            request.nickname,
            request.yearsInPrison,
            request.loyalty,
            request.strength,
            request.iq
        )
    }

    @GetMapping
    fun getAllDealers(): List<Dealer> =
        registry.allDealers()

    @GetMapping("/count")
    fun dealerCount(): Int =
        registry.dealersCount()

    @GetMapping("/strongest")
    fun strongestDealer(): ResponseEntity<Dealer> =
        registry.strongestDealer()
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()

    @GetMapping("/smartest")
    fun smartestDealer(): ResponseEntity<Dealer> =
        registry.smartestDealer()
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()

    @GetMapping("/most-years-in-prison")
    fun dealerWithMostYearsInPrison(): ResponseEntity<Dealer> =
        registry.dealerWithMostYearsInPrison()
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()

    @GetMapping("/dealer-of-the-year")
    fun dealerOfTheYear(): ResponseEntity<Dealer> =
        registry.dealerOfTheYear()
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()

    @GetMapping("/elite-dealers")
    fun eliteDealers(): List<Dealer> =
        registry.eliteDealers()

    @GetMapping("/statistics/average-iq")
    fun averageIq(): ResponseEntity<Double> =
        registry.averageIq()
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()

    @GetMapping("/statistics/average-strength")
    fun averageStrength(): ResponseEntity<Double> =
        registry.averageStrength()
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()

    @GetMapping("/statistics")
    fun statistics(): DealerStatistics =
        DealerStatistics(
            crewStatistics = registry.crewStatistics(),
            loyaltyStatistics = registry.loyaltyStatistics(),
            strongestDealer = registry.strongestDealer(),
            smartestDealer = registry.smartestDealer(),
            dealerOfTheYear = registry.dealerOfTheYear(),
            dealerWithMostYearsInPrison = registry.dealerWithMostYearsInPrison(),
            eliteDealers = registry.eliteDealers()
        )

    @GetMapping("/id/{id}")
    fun getDealer(
        @PathVariable id: Int
    ): ResponseEntity<Any> {
        val dealer = registry.findById(id)
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
        return if (registry.removeDealer(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/nickname/{nickname}")
    fun searchDealerByNickname(
        @PathVariable nickname: String
    ): List<Dealer> =
        registry.findByNickname(nickname)

    @GetMapping("/text/{text}")
    fun searchDealerByText(
        @PathVariable text: String
    ): List<Dealer> =
        registry.dealersByTextInNickname(text)

    @GetMapping("/loyalty/{loyalty}")
    fun searchDealerByLoyalty(
        @PathVariable loyalty: Loyalty
    ): List<Dealer> =
        registry.dealersByLoyalty(loyalty)
}