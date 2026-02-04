package com.stranger_sparks.data_model

data class GalleryImagesResponse(
    val data: List<Data>,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val created_at: String,
        val created_by: String,
        val id: String,
        val image: String,
        val user_id: String
    )
}