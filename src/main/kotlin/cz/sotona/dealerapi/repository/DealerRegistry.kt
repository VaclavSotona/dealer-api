package cz.sotona.dealerapi.repository

import cz.sotona.dealerapi.model.Loyalty
import cz.sotona.dealerapi.model.Dealer

interface DealerRegistry {

    fun addDealer(
        nickname: String,
        yearsInPrison: Int,
        loyalty: Loyalty,
        strength: Int,
        iq: Int
    ): Dealer

    fun updateDealer(
        id: Int,
        nickname: String,
        yearsInPrison: Int,
        loyalty: Loyalty,
        strength: Int,
        iq: Int
    ): Boolean

    fun allDealers(): List<Dealer>
    fun findById(id: Int): Dealer?
    fun findByNickname(nickname: String): List<Dealer>
    fun dealersByTextInNickname(text: String): List<Dealer>
    fun dealersByLoyalty(loyalty: Loyalty): List<Dealer>
    fun removeDealer(id: Int): Boolean
}