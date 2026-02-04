package com.stranger_sparks.data_model

data class NotificationsResponse(
    val data: List<Data>,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val body: String,
        val click_action: String,
        val created_at: String,
        val id: String,
        val status: String,
        val title: String,
        val user_id: String,
        val profile_id: String
    )
}