package com.stranger_sparks.data_model

data class SuggestionCityResponse(
    val data: List<Data>,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val location: String
    )
}