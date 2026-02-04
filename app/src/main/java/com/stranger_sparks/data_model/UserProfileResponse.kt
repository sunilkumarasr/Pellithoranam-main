package com.stranger_sparks.data_model

import java.io.Serializable

data class UserProfileResponse(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val age: String,
        val created_at: String,
        val description: String,
        val device_id: String,
        val email: String,
        val gender: String,
        val height: String,
        val hobbies: String,
        val id: String,
        val image: String,
        val image_count: Int,
        val is_subscription: Int,
        val location: String,
        val name: String,
        val otp: String,
        val phone: String,
        val profile_completed: Int,
        val profile_count: Int,
        val profile_image: List<ProfileImage>,
        val status: String,
        val updated_at: String,
        val user_id: String,
        val wallet: String,
        val is_online: Int,
    ): Serializable{
        data class ProfileImage(
            val image: String
        ): Serializable
    }
}