package com.stranger_sparks.data_model

import com.google.gson.annotations.SerializedName

data class StatusMessageResponse(
    val message: String,
    val status: Boolean,
    val otp: Int
)
data class StatusBankMainResponse(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = "",
    @SerializedName("data"    ) var data    : BankDetailsModel?    = BankDetailsModel()
)

data class OrderId(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = "",
    @SerializedName("data"    ) var data    : String?  = "",
)
data class PaymentResponse(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = "",
    @SerializedName("data"    ) var data    : String?  = "",
)

data class BankDetailsModel(
    @SerializedName("id"             ) var id            : String? = "",
    @SerializedName("qr_code"             ) var qr_code            : String? = "",
    @SerializedName("transaction_id" ) var transactionId : String? = "",
    @SerializedName("user_id"        ) var userId        : String? = "",
    @SerializedName("amount"         ) var amount        : String? = "",
    @SerializedName("reason"         ) var reason        : String? = "",
    @SerializedName("account_number" ) var accountNumber : String? = "",
    @SerializedName("bank_name"      ) var bankName      : String? = "",
    @SerializedName("branch_name"    ) var branchName    : String? = "",
    @SerializedName("ifsc_code"      ) var ifscCode      : String? = "",
    @SerializedName("created_at"     ) var createdAt     : String? = "",
    @SerializedName("updated_at"     ) var updatedAt     : String? = "",
    @SerializedName("status"         ) var status        : String? = "",
    @SerializedName("wallet_id"      ) var walletId      : String? = "",
    @SerializedName("googlepay"      ) var googlepay      : String? = "",
    @SerializedName("phonepe"      ) var phonepe      : String? = "",
)