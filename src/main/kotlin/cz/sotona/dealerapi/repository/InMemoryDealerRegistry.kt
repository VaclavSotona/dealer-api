package cz.sotona.dealerapi.repository

import cz.sotona.dealerapi.model.Dealer
import cz.sotona.dealerapi.model.Loyalty
import org.springframework.stereotype.Repository

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
                iq = (50..120).random(),
                strength = (50..120).random()
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

    override fun allDealers(): List<Dealer> =
        dealers.toList()

    override fun findById(id: Int): Dealer? =
        dealers.find { it.id == id }

    override fun dealersByLoyalty(loyalty: Loyalty): List<Dealer> =
        dealers.filter { it.loyalty == loyalty }

    override fun findByNickname(nickname: String): List<Dealer> =
        dealers.filter { it.nickname.equals(nickname, ignoreCase = true) }

    override fun removeDealer(id: Int): Boolean =
        dealers.removeIf { it.id == id }

    override fun dealersByTextInNickname(text: String): List<Dealer> =
        dealers.filter { it.nickname.contains(text, ignoreCase = true) }
}