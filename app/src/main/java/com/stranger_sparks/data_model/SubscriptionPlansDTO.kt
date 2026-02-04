package com.stranger_sparks.data_model

data class SubscriptionPlansDTO(
    val data: List<Data>,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val id: String,
        val features: String,
        val no_of_profile: String,
        val price: String,
        val title: String,
        var isSelected: Boolean = true
    )
}