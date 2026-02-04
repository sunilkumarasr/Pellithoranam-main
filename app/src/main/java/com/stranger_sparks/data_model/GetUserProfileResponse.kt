package com.stranger_sparks.data_model

data class GetUserProfileResponse(
    var `data`: Data,
    var message: String,
    var status: Boolean,
    var profile_view_status: Boolean
) {
    data class Data(

        var id: String,
        var name: String,
        var age: Long,
        var gender: String,

        var father_name: Any?,
        var salary: Any?,
        var company: Any?,
        var weight: Any?,

        var family_name: Any?,
        var job: Any?,
        var religion: String?,

        var is_chart: Boolean,
        var email: String,
        var phone: String,

        var alternative_phone: String,
        var location: String,

        var device_id: String,
        var marital: String,
        var languages: String,
        var height: String,
        var description: String,
        var hobbies: String,
        var caste: String?,


        var profile_completed: String,

        var profile_pic: String,

        var profile_image: ProfileImage,

        var liked_status: Boolean,

        var liked_count: Long,

        var subscription_varue: String,

        var subscription_video: Boolean,

        var subscription_video_time: String,

        var subscription_audio: Boolean,

        var subscription_audio_time: String,

        


    ) {
        data class ProfileImage(
            var imageDate: List<String>,
            var image_count: Int
        )
    }
}