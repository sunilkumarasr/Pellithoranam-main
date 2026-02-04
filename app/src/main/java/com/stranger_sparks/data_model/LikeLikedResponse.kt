package com.stranger_sparks.data_model

data class LikeLikedResponse(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val age: String,
        var type: String,
        val created_at: String,
        val description: String,
        val device_id: String,
        val email: String,
        val gender: String,
        val height: String,
        val hobbies: String,
        val id: String,
        val image: String,
        val is_subscription: String,
        val location: String,
        val name: String,
        val otp: String,
        val phone: String,
        val profile_completed: String,
        val profile_count: String,
        val status: String,
        val updated_at: String,
        val user_id: String,
        val wallet: String
    )
}