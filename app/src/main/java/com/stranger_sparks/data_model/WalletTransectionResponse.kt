package com.stranger_sparks.data_model

data class WalletTransectionResponse(
    val data: List<Data>,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val amount: Int,
        val created_at: String,
        val details: String,
        val payment_scource: String,
        val payment_status: String,
        val action: String,
    )
}