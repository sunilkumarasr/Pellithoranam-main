package com.stranger_sparks.api_dragger_flow.repository

import com.stranger_sparks.api_dragger_flow.retrofit.StrangerSparksApiInterface
import com.stranger_sparks.data_model.AgeResponse
import com.stranger_sparks.data_model.CallHistoryDataResponse
import com.stranger_sparks.data_model.CastResponse
import com.stranger_sparks.data_model.ChatResponse
import com.stranger_sparks.data_model.CitysListResponse
import com.stranger_sparks.data_model.GalleryImagesResponse
import com.stranger_sparks.data_model.GetAboutTermsPrivacyDTO
import com.stranger_sparks.data_model.GetUserProfileResponse
import com.stranger_sparks.data_model.LikeLikedResponse
import com.stranger_sparks.data_model.LoginResponse
import com.stranger_sparks.data_model.ManageSubscriptionResponse
import com.stranger_sparks.data_model.NotificationsResponse
import com.stranger_sparks.data_model.OrderId
import com.stranger_sparks.data_model.PaymentResponse
import com.stranger_sparks.data_model.ProfileViewDetailsResponse
import com.stranger_sparks.data_model.StatusBankMainResponse
import com.stranger_sparks.data_model.StatusMessageDataResponse
import com.stranger_sparks.data_model.StatusMessageResponse
import com.stranger_sparks.data_model.SubscriptionPlansDTO
import com.stranger_sparks.data_model.SuggestionCityResponse
import com.stranger_sparks.data_model.UserProfileResponse
import com.stranger_sparks.data_model.WalletTransectionResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Part
import javax.inject.Inject


class StrangerSparksRepository @Inject constructor(private val apiService: StrangerSparksApiInterface) {

    fun getStaticData(pageNumber: String): Call<GetAboutTermsPrivacyDTO> {
        return apiService.getStaticData(pageNumber)
    }

    fun getSubscriptionList(): Call<SubscriptionPlansDTO> {
        return apiService.getSubscriptionList()
    }

    fun getSubscriptionV2List(type: String): Call<SubscriptionPlansDTO> {
        return apiService.getSubscriptionV2List(type)
    }


    fun getUserLogin(phoneNumber: String): Call<StatusMessageResponse> {
        return apiService.getUserLogin(phoneNumber)
    }

    fun validateOtp(phone: String, enteredOtp: String, device_id: String): Call<LoginResponse> {
        return apiService.validateOtp(phone, enteredOtp, device_id)
    }

    fun updateProfile(
        full_name: RequestBody,
        familyname: RequestBody,
        fathername: RequestBody,
        gender: RequestBody,
        hobbies: RequestBody,
        age: RequestBody,
        phone: RequestBody,
        birthStar: RequestBody,
        height: RequestBody,
        weight: RequestBody,
        profession: RequestBody,
        education: RequestBody,
        company: RequestBody,
        designation: RequestBody,
        salary: RequestBody,
        religion: RequestBody,
        location: RequestBody,
        languages: RequestBody,
        description: RequestBody,
        user_id: RequestBody,
        caste: RequestBody,
        image_outlet: MultipartBody.Part
    ): Call<LoginResponse> {
        return apiService.updateProfile( full_name,
            familyname,
            fathername,
            gender,
            hobbies,
            age,
            phone,
            birthStar,
            height,
            weight,
            profession,
            education,
            company,
            designation,
            salary,
            religion,
            location,
            languages,
            description,
            user_id,
            caste,
            image_outlet
        )
    }



    fun updateProfilewithoutpic(
        full_name: RequestBody,
        familyname: RequestBody,
        fathername: RequestBody,
        gender: RequestBody,
        hobbies: RequestBody,
        age: RequestBody,
        phone: RequestBody,
        birth_star: RequestBody,
        height: RequestBody,
        weight: RequestBody,
        profession: RequestBody,
        education: RequestBody,
        company: RequestBody,
        designation: RequestBody,
        salary: RequestBody,
        religion: RequestBody,
        location: RequestBody,
        languages: RequestBody,
        description: RequestBody,
        user_id: RequestBody,
        caste: RequestBody,
    ): Call<LoginResponse> {
        return apiService.updateProfilewithoutpic(
            full_name,
            familyname,
            fathername,
            gender,
            hobbies,
            age,
            phone,
            birth_star,
            height,
            weight,
            profession,
            education,
            company,
            designation,
            salary,
            religion,
            location,
            languages,
            description,
            user_id,
            caste
        )
    }

    fun galleryImage(
        user_id: RequestBody,
        image_outlet: Array<MultipartBody.Part>
    ): Call<StatusMessageResponse> {
        return apiService.galleryImage(user_id, image_outlet)
    }

    fun contactUs(
        name: String,
        email: String,
        phone: String,
        subject: String,
        message: String,
    ): Call<StatusMessageResponse> {
        return apiService.contactUs(name, email, phone, subject, message)
    }

    fun notificationList(
        user_id: String,per_page: String,page_number: Int
    ): Call<NotificationsResponse> {
        return apiService.notificationList(user_id, per_page, page_number)
    }

    fun addWallet(
        user_id: String,
        amount: String
    ): Call<StatusMessageResponse> {
        return apiService.addWallet(user_id, amount)
    }

    fun addOnlineWalletAmount(
        razorpay_order_id: String,
        razorpay_payment_id: String,
        razorpay_signature: String,
        bank: String,
        user_id: String,
        amount: String
    ): Call<StatusMessageResponse> {
        return apiService.addOnlineWalletAmount(razorpay_order_id, razorpay_payment_id, razorpay_signature, bank, user_id, amount)
    }
    fun createOrder(
        amount: String
    ): Call<OrderId> {
        return apiService.createOrder(amount)
    }

    fun checkPayment(
        amount: String,
        razorpay_order_id: String,
        razorpay_payment_id: String,
        razorpay_signature: String) : Call<PaymentResponse> {
        return apiService.razorpayCallback(amount, razorpay_order_id, razorpay_payment_id, razorpay_signature)
    }


    fun manualPayment(
        user_id: RequestBody,
        amount: RequestBody,
        subscription_id: RequestBody,
        transaction_id: RequestBody,
        screenshot: MultipartBody.Part
    ): Call<StatusMessageResponse> {
        return apiService.manualPayment(
            user_id, amount,subscription_id,transaction_id, screenshot
        )
    }

    fun getWalletByUser(
        user_id: String
    ): Call<StatusMessageDataResponse> {
        return apiService.getWalletByUser(user_id)
    }

    fun walletTransactionsList(
        user_id: String,
        status_type: String,
    ): Call<WalletTransectionResponse> {
        return apiService.walletTransactionsList(user_id, status_type)
    }

    fun getGalleryProfile(
        user_id: String
    ): Call<GalleryImagesResponse> {
        return apiService.getGalleryProfile(user_id)
    }

    fun galleryImageDelete(
        id: String
    ): Call<StatusMessageResponse> {
        return apiService.galleryImageDelete(id)
    }

    fun citySuggestion(cityName: String): Call<SuggestionCityResponse> {
        return apiService.citySuggestion(cityName)
    }

    fun searchUserByLocation(cityName: String, user_id: String): Call<UserProfileResponse> {
        return apiService.searchUserByLocation(cityName, user_id)
    }
    fun searchUserByLocationHome(cityName: String, user_id: String): Call<UserProfileResponse> {
        return apiService.searchUserByLocation(cityName, user_id)
    }

    fun getCitysList(): Call<CitysListResponse> {
        return apiService.getCitysList()
    }

    fun getUserProfile(user_id: String, profile_id: String): Call<GetUserProfileResponse> {
        return apiService.getUserProfile(user_id, profile_id)
    }

    fun profileViewDetails(user_id: String, profile_id: String): Call<ProfileViewDetailsResponse> {
        return apiService.profileViewDetails(user_id, profile_id)
    }

    fun userstartcall(user_id: String, profile_id: String, type: String): Call<ProfileViewDetailsResponse> {
        return apiService.userstartcall(user_id, profile_id,type)
    }

    fun likedProfile(
        user_id: String,
        profile_id: String
    ): Call<StatusMessageResponse> {
        return apiService.likedProfile(user_id, profile_id)
    }

    fun manageSubscriptions(
        user_id: String,
    ): Call<ManageSubscriptionResponse> {
        return apiService.manageSubscriptions(user_id)
    }

    fun manage_subscriptions_v2(
        user_id: String, type: String,
    ): Call<ManageSubscriptionResponse> {
        return apiService.manage_subscriptions_v2(user_id,type)
    }

    fun addPayment(
        user_id: String,
        subscription_id: String,
    ): Call<LoginResponse> {
        return apiService.addPayment(user_id, subscription_id)
    }

    fun datingMatches(
        user_id: String,
        type: String
    ): Call<LikeLikedResponse> {
        return apiService.datingMatches(user_id, type)
    }

    fun viewProfile(
        user_id: String,
        profile_id: String
    ): Call<StatusMessageResponse> {
        return apiService.viewProfile(user_id, profile_id)
    }

    fun walletWithdrawal(
        user_id: String,
        amount: String,
        reason: String,
        bank_name:String,
        account_number: String,
        confirm_account_number: String,
        ifsc_code: String,
        branch: String,
        googlepay: String,
        phonepe: String,
    ): Call<StatusMessageResponse> {
        return apiService.walletWithdrawal(
            user_id,
            amount,
            reason,
            bank_name,
            account_number,
            confirm_account_number,
            ifsc_code,
            branch,
            googlepay,
            phonepe,

        )
    }

    fun getBankDetails(
        user_id: String
    ): Call<StatusBankMainResponse> {
        return apiService.getBankDetails(
            user_id
        )
    }

    fun getQrDetails(

    ): Call<StatusBankMainResponse> {
        return apiService.getQrDetails(
        )
    }

    fun changeUserCallCancel(
        user_id: String,
        profile_id: String,
        type: String,
        call_type: String,
    ): Call<StatusMessageResponse> {
        return apiService.changeUserCallCancel(
            user_id,profile_id,type,call_type
        )
    }


    fun sendMessage(
        sender_id: String,
        receiver_id: String,
        message: String
    ): Call<StatusMessageResponse> {
        return apiService.sendMessage(sender_id, receiver_id, message)
    }


    fun searchChat( sender_id: String,
                    receiver_id: String,
                    message: String,
    ): Call<ChatResponse>{
        return apiService.searchChat(sender_id, receiver_id, message)
    }

    fun delete_only_for_you_post(@Field("sender_id") sender_id: String,
                   @Field("id") id: String
    ): Call<StatusMessageResponse>{
        return apiService.delete_only_for_you_post(sender_id, id)
    }

    fun delete_only_for_both_post(@Field("sender_id") sender_id: String,
                                 @Field("id") id: String
    ): Call<StatusMessageResponse>{
        return apiService.delete_only_for_both_post(sender_id, id)
    }

    fun getMessage( sender_id: String,
                    receiver_id: String
    ): Call<ChatResponse>{
        return apiService.getMessage(sender_id, receiver_id)
    }

    fun sendChartImage(
       sender_id: RequestBody,
        receiver_id: RequestBody,
       image: MultipartBody.Part
    ): Call<StatusMessageResponse> {
        return apiService.sendChartImage(
            sender_id, receiver_id, image
        )
    }

    fun logout(user_id: String
    ): Call<StatusMessageResponse>{
        return apiService.logout(user_id)
    }

    fun userAudioCall(user_id: String, profile_id: String, exitTime: String): Call<StatusMessageResponse> {
        return apiService.userAudioCall(user_id, profile_id, exitTime)
    }
    fun userMissedCall(user_id: String, profile_id: String, type: String): Call<StatusMessageResponse> {
        return apiService.userMissedCall(user_id, profile_id, type)
    }
    fun rejectCall(user_id: String, profile_id: String, type: String): Call<StatusMessageResponse> {
        return apiService.rejectCall(user_id, profile_id,type)
    }

    fun userVideoCall(user_id: String, profile_id: String, exitTime: String): Call<StatusMessageResponse> {
        return apiService.userVideoCall(user_id, profile_id, exitTime)
    }

    fun searchUserByFiltersHome(
        searchName: String,
        gender: String,
        age: String,
        religion: String,
        address: String,
        userId: String
    ): Call<UserProfileResponse> {
        return apiService.searchUserByFiltersHome(searchName,gender,age,religion,address, userId)
    }

    fun getCaste(

    ): Call<CastResponse> {
        return apiService.getCaste()
    }
    fun getAgeFilter(
    ): Call<AgeResponse> {
        return apiService.getAgeFilter()
    }

    fun getCallHistory(
        user_id: String
    ): Call<CallHistoryDataResponse> {
        return apiService.getCallHistory(user_id)
    }

    fun submitUserCallHistory(
        userId: String, type: String, CallerId: String, Name: String, duration: String, callType: String
    ): Call<StatusMessageDataResponse> {
        return apiService.submitUserCallHistory(userId, type, CallerId, Name, duration, callType)
    }

    fun submitProfileCallHistory(
        profileId: String, type: String, CallerId: String, Name: String, duration: String, callType: String
    ): Call<StatusMessageDataResponse> {
        return apiService.submitProfileCallHistory(profileId, type, CallerId, Name, duration, callType)
    }

}