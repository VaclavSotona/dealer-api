package cz.sotona.dealerapi.dto

import cz.sotona.dealerapi.model.Loyalty

data class CreateDealerRequest(
    val nickname: String,
    val yearsInPrison: Int,
    val loyalty: Loyalty,
    val strength: Int,
    val iq: Int
)