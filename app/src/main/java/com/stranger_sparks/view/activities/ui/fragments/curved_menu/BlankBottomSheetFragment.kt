package com.stranger_sparks.view.activities.ui.fragments.curved_menu

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stranger_sparks.R
import com.stranger_sparks.databinding.FragmentBlankBottomSheetBinding
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.view.activities.ui.activities.account.AccountActivity
import com.stranger_sparks.view.activities.ui.activities.account.like_liked.LikeLikedActivity
import com.stranger_sparks.view.activities.ui.activities.chat.ChatActivity
import com.stranger_sparks.view.activities.ui.activities.help.HelpActivity
import com.stranger_sparks.view.activities.ui.activities.manage_subcription.ManageSubscription
import com.stranger_sparks.view.activities.ui.activities.manage_subcription.ManageSubscriptionNew
import com.stranger_sparks.view.activities.ui.activities.notifications.Notifications
import com.stranger_sparks.view.activities.ui.activities.wallet.WalletActivity
import com.stranger_sparks.view.activities.ui.activities.wallet.transections.WalletTransactionsActivity


class BlankBottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentBlankBottomSheetBinding
    fun newInstance(): BlankBottomSheetFragment? {
        return BlankBottomSheetFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBlankBottomSheetBinding.inflate(inflater, container, false)
        val  viewModel: BlankBottomSheetViewModel = ViewModelProvider(this).get(BlankBottomSheetViewModel::class.java);
        viewModel.binding = binding
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }


    /*override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        *//*return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("data downloaded")
                .setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, id ->
                        //positive button action here dissmis dialog
                    })


            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")*//*

        val contentView = View.inflate(context, R.layout.fragment_blank_bottom_sheet, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(resources.getColor(R.color.transparent))
    }*/
    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView = View.inflate(context, R.layout.fragment_blank_bottom_sheet, null)
        dialog.setContentView(contentView)
        /*val window: Window? = dialog?.getWindow()
        val wlp = window?.attributes

        wlp?.gravity = Gravity.START
        wlp?.flags = wlp?.flags?.and(WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv())*/

        val window: Window? = dialog?.getWindow()
        val param = window?.attributes
        param?.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        param?.y = 20
        window?.attributes = param
        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        (contentView.parent as View).setBackgroundColor(resources.getColor(R.color.transparent))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel!!.observerEvents.observe(viewLifecycleOwner){observerEvents ->
            observerEvents?.let { validateViewModelEvents(observerEvents) }
        }

        binding.tvAccount.setOnClickListener {

            Intent(requireContext(), AccountActivity::class.java).also {
                //it.putExtra("url_type", Constants.ObserverEvents.ABOUT_US.toString())
                startActivity(it)
                dismiss()

            }
        }
        binding.tvLikesLiked.setOnClickListener {
            Intent(requireContext(), LikeLikedActivity::class.java).also {
                //it.putExtra("url_type", Constants.ObserverEvents.ABOUT_US.toString())
                startActivity(it)
                dismiss()

            }
        }
        binding.tvNotifications.setOnClickListener {
            Intent(requireContext(), Notifications::class.java).also {
                //it.putExtra("url_type", Constants.ObserverEvents.ABOUT_US.toString())
                startActivity(it)
                dismiss()

            }
        }
        binding.tvPayment.setOnClickListener {
            Intent(requireContext(), WalletTransactionsActivity::class.java).also {
                //it.putExtra("url_type", Constants.ObserverEvents.ABOUT_US.toString())
                startActivity(it)
                dismiss()

            }
        }
        binding.tvWallet.setOnClickListener {
            Intent(requireContext(), WalletActivity::class.java).also {
                startActivity(it)

            }
        }
        binding.tvManageSubscriptions.setOnClickListener {
            Intent(requireContext(), ManageSubscriptionNew::class.java).also {
                //it.putExtra("url_type", Constants.ObserverEvents.ABOUT_US.toString())
                startActivity(it)
                dismiss()
            }

        }


    }
    private fun validateViewModelEvents(observerEvents: Constants.ObserverEvents) {
        if (observerEvents === Constants.ObserverEvents.CLOSE_WINDOW) {
            dismiss()

        }
    }


}