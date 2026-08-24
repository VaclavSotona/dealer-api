package cz.sotona.dealerapi.dto

import cz.sotona.dealerapi.model.Dealer
import cz.sotona.dealerapi.model.Loyalty

data class DealerStatistics(
    val crewStatistics: CrewStatistics,
    val loyaltyStatistics: Map<Loyalty, LoyaltyStatistics>,
    val strongestDealer: Dealer?,
    val smartestDealer: Dealer?,
    val dealerOfTheYear: Dealer?,
    val dealerWithMostYearsInPrison: Dealer?,
    val eliteDealers: List<Dealer>
)