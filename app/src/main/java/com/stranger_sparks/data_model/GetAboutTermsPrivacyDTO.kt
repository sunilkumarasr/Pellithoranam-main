package com.stranger_sparks.data_model

data class GetAboutTermsPrivacyDTO(
    val data: Data,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val information_title: String,
        val page_image: String,
        val page_title: String
    )
}