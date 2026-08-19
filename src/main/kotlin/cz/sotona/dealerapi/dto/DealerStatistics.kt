package cz.sotona.dealerapi.dto

import cz.sotona.dealerapi.model.Dealer

data class DealerStatistics(
    val count: Int,
    val eliteDealersCount: Int,
    val averageIq: Double?,
    val averageStrength: Double?,
    val strongestDealer: Dealer?,
    val smartestDealer: Dealer?,
    val dealerOfTheYear: Dealer?
)