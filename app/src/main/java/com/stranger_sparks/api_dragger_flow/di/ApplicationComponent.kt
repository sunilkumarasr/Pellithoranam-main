package com.stranger_sparks.api_dragger_flow.di

import com.stranger_sparks.api_dragger_flow.dagger_modules.StrangerSparksApiModule
import com.stranger_sparks.api_dragger_flow.dagger_modules.StrangerSparksRepositoryModule
import com.stranger_sparks.view.activities.BasicProfileActivity
import com.stranger_sparks.view.activities.HomeActivity
import com.stranger_sparks.view.activities.OtpVerificationActivity
import com.stranger_sparks.view.activities.SignInSignUpActivity
import com.stranger_sparks.view.activities.Subcription
import com.stranger_sparks.view.activities.ui.activities.call_List.CallHistoryListActivity
import com.stranger_sparks.view.activities.ui.activities.FiltersActivity
import com.stranger_sparks.view.activities.ui.activities.account.AccountActivity
import com.stranger_sparks.view.activities.ui.activities.account.add_images.AddImagesActivity
import com.stranger_sparks.view.activities.ui.activities.account.gallery.GalleryActivity
import com.stranger_sparks.view.activities.ui.activities.account.like_liked.LikeLikedActivity
import com.stranger_sparks.view.activities.ui.activities.account.profile.ProfileActivity
import com.stranger_sparks.view.activities.ui.activities.chat.chat_room.ChatRoom
import com.stranger_sparks.view.activities.ui.activities.display_user.DisplayUserActivity
import com.stranger_sparks.view.activities.ui.activities.help.HelpActivity
import com.stranger_sparks.view.activities.ui.activities.manage_subcription.AudioSubscriptionFragment
import com.stranger_sparks.view.activities.ui.activities.manage_subcription.ManageSubscription
import com.stranger_sparks.view.activities.ui.activities.manage_subcription.ProfileSubscriptionFragment
import com.stranger_sparks.view.activities.ui.activities.manage_subcription.VideoSubscriptionFragment
import com.stranger_sparks.view.activities.ui.activities.my_account.MyAccount
import com.stranger_sparks.view.activities.ui.activities.notifications.Notifications
import com.stranger_sparks.view.activities.ui.activities.settings.SettingsActivity
import com.stranger_sparks.view.activities.ui.activities.wallet.WalletActivity
import com.stranger_sparks.view.activities.ui.activities.wallet.transections.WalletTransactionsActivity
import com.stranger_sparks.view.activities.ui.activities.webview.WebViewUrlLoad
import com.stranger_sparks.view.activities.ui.fragments.home.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [StrangerSparksApiModule::class, StrangerSparksRepositoryModule::class])
interface ApplicationComponent {
    fun inject(mainActivity: SignInSignUpActivity)
    fun inject(subcription: Subcription)
    fun inject(webViewUrlLoad: WebViewUrlLoad)
    fun inject(otpVerificationActivity: OtpVerificationActivity)
    fun inject(basicProfileActivity: BasicProfileActivity)
    fun inject(helpActivity: HelpActivity)
    fun inject(accountActivity: AccountActivity)
    fun inject(profileActivity: ProfileActivity)
    fun inject(galleryActivity: GalleryActivity)
    fun inject(addImagesActivity: AddImagesActivity)
    fun inject(walletActivity: WalletActivity)
    fun inject(walletTransactionsActivity: WalletTransactionsActivity)
    fun inject(notifications: Notifications)
    fun inject(homeActivity: HomeActivity)
    fun inject(manageSubscription: ManageSubscription)
    fun inject(homeFragment: HomeFragment)
    fun inject(displayUserActivity: DisplayUserActivity)
    fun inject(myAccount: MyAccount)
    fun inject(likeLikedActivity: LikeLikedActivity)
    fun inject(chatRoom: ChatRoom)
    fun inject(settings: SettingsActivity)
    fun inject(outgoingVideo: AudioSubscriptionFragment)
    fun inject(outgoingVideo: ProfileSubscriptionFragment)
    fun inject(outgoingVideo: VideoSubscriptionFragment)
    fun inject(filtersActivity: FiltersActivity)
    fun inject(callHistoryListActivity: CallHistoryListActivity)

}