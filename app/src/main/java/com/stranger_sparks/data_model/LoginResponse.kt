package com.stranger_sparks.data_model

data class LoginResponse(
    var data: Data,
    var message: String,
    var status: Boolean
) {
    data class Data(
        var id: String,
        
        var user_id: String,
        var name: String,
       
        var alternative_phone: String,
        var birth_star: String,
        var age: String,
        var gender: String,
        var email: String,
        var phone: String,
        var location: String,
        var otp: String,
        var height: String,
        
        var device_id: String,
        var description: String,
        var hobbies: String,
        var wallet: Any?,
        var marital: String,
        var languages: String,
        var caste: String,

        var created_at: String,
       
        var updated_at: String,
        var status: String,
        
        var profile_completed: String,
       
        var profile_count: String,
       
        var subscription_varue: String,
      
        var is_subscription: String,
        var image: String,
       
        var is_online: String,
        var religion: String,
        var job: String,
        
        var family_name: String,
        var weight: String,
        var company: String,
        var salary: String,
       
        var father_name: String,
        var education: String,
        var designation: String,
        var bio: String,
    )
}