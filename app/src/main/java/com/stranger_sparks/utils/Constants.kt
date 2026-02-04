package com.stranger_sparks.utils


interface Constants {
    object Constants {
        const val BASE_URL = "https://fakestoreapi.com/"
    }

    /**
     * Purpose : All the usable intent key types is here.
     */
    object IntentStrings {
        var from = "fragmentType"

    }

    /**
     * Purpose : All the fragments is here which we are opening through the intent.
     */
    object FragmentTypes {
        var login = "login"

    }

    object FlagValues {
        /*
        * Note Don't Change Values we have dependencies
        *
        * */
        //This is for Close Alert Refund Cancellation Charge Alert (CancelationChargesDailougeFragment)
        var PROCEED_TO_PAY_CLOSE_CANCELLATION_ALERT = "Proceed to Pay Close Cancellation Alert"
    }

    /**
     * Purpose : All the shared preferences names is here.
     */
    object Pref {
        var shared_preference = "shared_preference"

    }

    /**
     * Purpose : All the params names will be here of the entire application.
     */
    object Params {
        var timingList = "timingList"

    }

    /**
     * Purpose : All the observable events will be here.
     * Used this events for observing main view model data.
     */
    enum class ObserverEvents {
        BACK_PRESS, OPEN_SIGN_IN_SCREEN,GOTO_OTP, GOTO_SIGN_UP,GOTO_SIGN_IN,GOTO_SUBSCRIPTION_SUCCESS, GOTO_HOME_SCREEN, GOTO_SCUBSCRIPTION, LOAD_NOTIFICATIONS, CLOSE_NOTIFICATION_SCREEN,
        SHOW_TRANSACTIONS,CLOSE_SHOW_TRANSACTIONS, CLOSE_WALLET_SCREEN, CLOSE_WINDOW, TERMS_AND_CONDITIONS, PRIVACY_POLICY, ABOUT_US, HELP, OPEN_BASIC_PROFILE, OPEN_HOME_SCREEN,FAQ
    }

    object ServerValues {
        const val S_ERROR = "Something went wrong"
    }
}
