package com.stranger_sparks.data_model

data class CallHistoryDataResponse(
    val data: List<CallData>,
    val message: String,
    val status: Boolean
){
    data class CallData(
        val id: String,
        val user_id: String,
        val type: String,
        val name: String,
        val caller_id: String,
        val call_type: String,
        val duration: String,
        val created_at: String,
        val updated_at: String
    )
}