package com.stranger_sparks.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.internal.ContextUtils
import com.stranger_sparks.R
import com.stranger_sparks.fcm.MyFirebaseMessagingServiceOLD
import java.util.regex.Pattern

object ConstantUtils {

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun changeNotificationBarColor(activity: Activity, color: Int, isLight: Boolean) {
        // Ensure system bars are drawn
        WindowCompat.setDecorFitsSystemWindows(activity.window, true)

        // Set the status bar color
        activity.window.statusBarColor = color

        // Set icon color (true = dark icons, false = light icons)
        WindowInsetsControllerCompat(activity.window, activity.window.decorView)
            .isAppearanceLightStatusBars = isLight
    }


    fun showSuccessToast(context: Context, message: String) {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.custom_toast_success, null)

        val tvMessage = layout.findViewById<TextView>(R.id.tvMessage)
        tvMessage.text = message

        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.setGravity(Gravity.BOTTOM, 0, 0)
        toast.view = layout
        toast.show()


        /*val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val customToastLayout = inflater.inflate(R.layout.custom_toast_success,null)
        val customToast = Toast(context)
        customToast.view = customToastLayout
        customToast.setGravity(Gravity.CENTER,0,0)
        customToast.duration = Toast.LENGTH_LONG
        customToast.show()


        // Inflating the layout for the toast
        // Inflating the layout for the toast
       ;


        val layout: View =  LayoutInflater.from(context).inflate(bfer.fmis.ap.gov.R.layout.custom_toast_success, null)


        val text = layout.findViewById<View>(R.id.tvtoast) as TextView

        text.text = "No internet connection"
        text.setTextColor(Color.rgb(0, 132, 219))
        val toast = Toast(context)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.duration = Toast.LENGTH_LONG
        toast.setView(layout)
        toast.show()*/
    }

    fun showCustomAlert(ctx: Context?, heading: String?, message: String?) {
        val dialog = Dialog(ctx!!, R.style.AlertDialogCustom)
        dialog.setContentView(R.layout.custom_alert)
        dialog.setCancelable(false)
        val tvHeading = dialog.findViewById<TextView>(R.id.tvHeading)
        val tvContent = dialog.findViewById<TextView>(R.id.tvContent)
        val btnCancel = dialog.findViewById<AppCompatButton>(R.id.btnCancel)
        val btnOk = dialog.findViewById<AppCompatButton>(R.id.btnOk)
        tvContent.text = message
        tvHeading.text = heading
        btnOk.setOnClickListener { v: View? -> dialog.dismiss() }
        btnCancel.setOnClickListener { v: View? -> dialog.dismiss() }
        dialog.show()
    }

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw      = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }
    fun alertDialog(message: String, context: Activity, title: String = "Alert") {

        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)

        alertDialog.setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        })

        alertDialog.show()
    }
    fun hasEditText(editText: EditText, errMsg: String??
    ): Boolean {
        if (editText.text.toString().trim { it <= ' ' }.isEmpty()) {
            editText.error = errMsg
            editText.requestFocus()//(editText, context)
            return false
        } else {
            // editText.isFocusable= false
        }
        return true
    }

    fun EditText.isTextEmpty(): Boolean {
        return this.text.toString().trim() == ""
    }
    fun isRegexMatch(regex: String, input: String): Boolean {
        val mPattern = Pattern.compile(regex)
        val match = mPattern.matcher(input)
        return match.matches()
    }

    @SuppressLint("RestrictedApi")
    fun createFullScreen(context: Context) {
        val window = ContextUtils.getActivity(context)?.window

        window?.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            decorView.apply {
                systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            statusBarColor =
                ContextCompat.getColor(context, android.R.color.transparent)
        }
    }

    fun darkModeDisable() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    fun hideKeyboard(context: Context, view: View) {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}