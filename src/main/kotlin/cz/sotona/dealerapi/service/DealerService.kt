package cz.sotona.dealerapi.service

import cz.sotona.dealerapi.dto.DealerStatistics
import cz.sotona.dealerapi.dto.LoyaltyStatistics
import cz.sotona.dealerapi.model.Dealer
import cz.sotona.dealerapi.model.Loyalty
import cz.sotona.dealerapi.repository.DealerRegistry
import org.springframework.stereotype.Service
import kotlin.math.round
import cz.sotona.dealerapi.dto.CrewStatistics

@Service
class DealerService(
    private val registry: DealerRegistry
) {

    fun addDealer(
        nickname: String,
        yearsInPrison: Int,
        loyalty: Loyalty,
        strength: Int,
        iq: Int
    ): Dealer =
        registry.addDealer(
            nickname,
            yearsInPrison,
            loyalty,
            strength,
            iq
        )

    fun updateDealer(
        id: Int,
        nickname: String,
        yearsInPrison: Int,
        loyalty: Loyalty,
        strength: Int,
        iq: Int
    ): Boolean =
        registry.updateDealer(
            id,
            nickname,
            yearsInPrison,
            loyalty,
            strength,
            iq
        )

    fun allDealers() = registry.allDealers()
    fun findById(id: Int) = registry.findById(id)
    fun findByNickname(nickname: String) = registry.findByNickname(nickname)
    fun dealersByTextInNickname(text: String) = registry.dealersByTextInNickname(text)
    fun removeDealer(id: Int) = registry.removeDealer(id)
    fun dealersByLoyalty(loyalty: Loyalty) = registry.dealersByLoyalty(loyalty)

    fun dealersCount(): Int =
        allDealers().size

    fun eliteDealersCount(): Int =
        eliteDealers().size

    fun loyaltyStatistics(): Map<Loyalty, LoyaltyStatistics> =
        Loyalty.entries.associateWith { loyalty ->
            val loyaltyDealers = allDealers().filter { it.loyalty == loyalty }
            LoyaltyStatistics(
                count = loyaltyDealers.size,
                averageIq = loyaltyDealers
                    .map { it.iq }
                    .average()
                    .let { round(it * 100) / 100 },
                averageStrength = loyaltyDealers
                    .map { it.strength }
                    .average()
                    .let { round(it * 100) / 100 },
            )
        }

    fun crewStatistics() = CrewStatistics(
        count = dealersCount(),
        eliteDealersCount = eliteDealersCount(),
        averageIq = averageIq(),
        averageStrength = averageStrength()
    )

    fun averageIq(): Double? {
        val dealers = allDealers()
        if (dealers.isEmpty()) {
            return null
        }
        return dealers.map { it.iq }.average().let { round(it * 100) / 100 }
    }

    fun averageStrength(): Double? {
        val dealers = allDealers()
        if (dealers.isEmpty()) {
            return null
        }
        return dealers.map { it.strength }.average().let { round(it * 100) / 100 }
    }

    fun eliteDealers(): List<Dealer> =
        allDealers()
            .filter { it.iq >= 110 }
            .filter { it.loyalty == Loyalty.INNER_CIRCLE}

    fun strongestDealer(): Dealer? =
        allDealers().maxByOrNull { it.strength }

    fun smartestDealer(): Dealer? =
        allDealers().maxByOrNull { it.iq }

    fun dealerWithMostYearsInPrison(): Dealer? =
        allDealers().maxByOrNull { it.yearsInPrison }

    fun dealerOfTheYear(): Dealer? =
        allDealers().maxByOrNull { it.iq + it.strength }

    fun statistics() =
        DealerStatistics(
            crewStatistics = crewStatistics(),
            loyaltyStatistics = loyaltyStatistics(),
            strongestDealer = strongestDealer(),
            smartestDealer = smartestDealer(),
            dealerOfTheYear = dealerOfTheYear(),
            dealerWithMostYearsInPrison = dealerWithMostYearsInPrison(),
            eliteDealers = eliteDealers()
        )
}