package com.stranger_sparks.data_model

data class CallCancelModal(
    val status: Boolean?,
    val message: String,
    val data: CallCancelData
) {
    data class CallCancelData(
        val id: String?,
        val type: String?,
        val user_id: String?
    )
}
