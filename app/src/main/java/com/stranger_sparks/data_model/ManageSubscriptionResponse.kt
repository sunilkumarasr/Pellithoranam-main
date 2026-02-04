package com.stranger_sparks.data_model

data class ManageSubscriptionResponse(
    val `data`: Data?    = Data(),
    val message: String,
    val status: Boolean
) {
    data class Data(
        val available_balance:  String?  = "",
        val consumed_duration:  String? = "",
        val features: String?  = "",
        val no_of_profile:  String?  = "",
        val plan_status: String?  = "",
        val price:  String?  = "",
        val title:  String?  = ""
    )
}