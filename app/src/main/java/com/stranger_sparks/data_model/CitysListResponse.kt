package com.stranger_sparks.data_model

data class CitysListResponse(
    val data: List<Data>,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val id: String,
        val name: String,
    )
}



data class CastResponse(
    val data: List<Caste>,
    val message: String,
    val status: Boolean
) {

}
data class Caste(
    val id: String,
    val name: String,
){
    override fun toString(): String {
        return name
    }
}
data class AgeResponse(
    val data: List<Age>,
    val message: String,
    val status: Boolean
) {

}
data class Age(
    val id: String,
    val name: String,
){
    override fun toString(): String {
        return name
    }
}