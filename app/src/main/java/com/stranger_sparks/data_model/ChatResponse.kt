package com.stranger_sparks.data_model

data class ChatResponse(
    val data: List<Data>,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val id: String,
        val image_url: String,
        val message: String,
        val receiver_id: String,
        val sender_id: String,
        val sent_at: String
    )
}