package cz.sotona.dealerapi.repository

import cz.sotona.dealerapi.dto.CrewStatistics
import cz.sotona.dealerapi.dto.LoyaltyStatistics
import cz.sotona.dealerapi.model.Dealer
import cz.sotona.dealerapi.model.Loyalty
import org.springframework.stereotype.Repository
import kotlin.math.round

@Repository
class InMemoryDealerRegistry : DealerRegistry {

    private val dealers = mutableListOf<Dealer>()
    private var nextId = 1

    init {
        val names = listOf(
            "Profesor", "Kobra", "Buldozer", "Doktor",
            "Kraken", "Ghost", "Mamba", "Tank", "Šíbr"
        )

        repeat(121) {
            addDealer(
                nickname = names.random() + (1..99999).random(),
                yearsInPrison = (0..15).random(),
                loyalty = Loyalty.entries.random(),
                iq = (50..150).random(),
                strength = (50..160).random()
            )
        }
    }

    override fun addDealer(
        nickname: String,
        yearsInPrison: Int,
        loyalty: Loyalty,
        strength: Int,
        iq: Int
    ): Dealer {

        val dealer = Dealer(
            nextId++,
            nickname,
            yearsInPrison,
            loyalty,
            strength,
            iq
        )

        dealers.add(dealer)
        return dealer
    }

    override fun updateDealer(
        id: Int,
        nickname: String,
        yearsInPrison: Int,
        loyalty: Loyalty,
        strength: Int,
        iq: Int
    ): Boolean {

        val dealer = findById(id) ?: return false

        dealer.nickname = nickname
        dealer.yearsInPrison = yearsInPrison
        dealer.loyalty = loyalty
        dealer.strength = strength
        dealer.iq = iq

        return true
    }

    override fun removeDealer(id: Int): Boolean {
        return dealers.removeIf { it.id == id }
    }

    override fun dealersCount(): Int {
        return dealers.size
    }

    override fun eliteDealersCount(): Int {
        return eliteDealers().size
    }

    override fun allDealers(): List<Dealer> {
        return dealers
    }

    override fun findByNickname(nickname: String): List<Dealer> {
        return dealers.filter {
            it.nickname.equals(nickname, ignoreCase = true)
        }
    }

    override fun dealersByLoyalty(loyalty: Loyalty): List<Dealer> {
        return dealers.filter { it.loyalty == loyalty }
    }

    override fun dealersWithMinIq(minIq: Int): List<Dealer> {
        return dealers.filter { it.iq >= minIq }
    }

    override fun dealersWithIqInRange(minIq: Int, maxIq: Int): List<Dealer> {
        return dealers.filter { it.iq in minIq..maxIq }
    }

    override fun dealersWithMinStrength(minStrength: Int): List<Dealer> {
        return dealers.filter { it.strength >= minStrength }
    }

    override fun dealersByTextInNickname(text: String): List<Dealer> {
        return dealers.filter { it.nickname.contains(text, ignoreCase = true) }
    }

    override fun eliteDealers(): List<Dealer> {
        return dealers
            .filter { it.iq >= 110 }
            .filter { it.loyalty == Loyalty.INNER_CIRCLE}
    }


    override fun strongestDealer(): Dealer? {
        return dealers.maxByOrNull { it.strength }
    }

    override fun smartestDealer(): Dealer? {
        return dealers.maxByOrNull { it.iq }
    }

    override fun dealerWithMostYearsInPrison(): Dealer? {
        return dealers.maxByOrNull { it.yearsInPrison }
    }

    override fun smartestDealerFromLoyalty(loyalty: Loyalty): Dealer? {
        return dealers
            .filter { it.loyalty == loyalty }
            .maxByOrNull { it.iq }
    }

    override fun dealerOfTheYear(): Dealer? {
        return dealers.maxByOrNull { it.iq + it.strength }
    }

    override fun findById(id: Int): Dealer? {
        return dealers.find { it.id == id }
    }

    override fun averageIq(): Double? {
        if (dealers.isEmpty()) {
            return null
        }
        return dealers.map { it.iq }.average().let { round(it * 100) / 100 }
    }

    override fun averageStrength(): Double? {
        if (dealers.isEmpty()) {
            return null
        }
        return dealers.map { it.strength }.average().let { round(it * 100) / 100 }
    }

    override fun loyaltyStatistics(): Map<Loyalty, LoyaltyStatistics> =
        Loyalty.entries.associateWith { loyalty ->
            val dealers = dealers.filter { it.loyalty == loyalty }
            LoyaltyStatistics(
                count = dealers.filter { it.loyalty == loyalty }.size,
                averageIq = dealers.filter { it.loyalty == loyalty }
                    .map { it.iq }
                    .average()
                    .let { round(it * 100) / 100 },
                averageStrength = dealers.filter { it.loyalty == loyalty }
                    .map { it.strength }
                    .average()
                    .let { round(it * 100) / 100 },
            )
        }

    override fun crewStatistics() = CrewStatistics(
        count = dealersCount(),
        eliteDealersCount = eliteDealersCount(),
        averageIq = averageIq(),
        averageStrength = averageStrength()
    )
}