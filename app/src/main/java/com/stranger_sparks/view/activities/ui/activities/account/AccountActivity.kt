package com.stranger_sparks.view.activities.ui.activities.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.stranger_sparks.R
import com.stranger_sparks.StrangerSparksApplication
import com.stranger_sparks.databinding.ActivityAccountBinding
import com.stranger_sparks.databinding.ActivityHelpBinding
import com.stranger_sparks.utils.ConstantUtils
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.view.activities.SignInSignUpActivity
import com.stranger_sparks.view.activities.ui.activities.account.gallery.GalleryActivity
import com.stranger_sparks.view.activities.ui.activities.account.profile.ProfileActivity
import com.stranger_sparks.view.activities.ui.activities.help.HelpActivityViewModel
import javax.inject.Inject

class AccountActivity : AppCompatActivity() {
    lateinit var binding: ActivityAccountBinding
    @Inject
    lateinit var viewModel: AccountViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this.application as StrangerSparksApplication).applicationComponent.inject(this)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        ConstantUtils.changeNotificationBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.inputSignal.observe(this) {

            if (it != null) {
                validateViewModelEvents(it)

            }
        }
        binding.btnGallery.setOnClickListener {
            Intent(applicationContext, GalleryActivity::class.java).also {
                startActivity(it)

            }
        }
        binding.btnProfile.setOnClickListener {
            Intent(applicationContext, ProfileActivity::class.java).also {
                startActivity(it)

            }
        }
        binding.ivClose.setOnClickListener {
            finish()
        }
    }

    private fun validateViewModelEvents(observerEvents: String) {
        if (observerEvents === Constants.ObserverEvents.CLOSE_NOTIFICATION_SCREEN.toString()) {

            finish()
        }
    }
}