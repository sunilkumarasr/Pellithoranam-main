package com.stranger_sparks.agora.media.fcm

import android.content.Context
import android.os.Build
import android.os.StrictMode
import android.util.Log
import androidx.annotation.RequiresApi
import com.stranger_sparks.utils.SharedPreferenceManager
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets


class FCMSend {

        private var BASE_URL = "https://fcm.googleapis.com/fcm/send"
        private var SERVER_KEY: String? = "key=AAAA8i4dyMU:APA91bEeyp8XRam_DV7PSxPTwuokP6-bYvv2JSF297YRMBHGj0FUM-i_IyZCs83Na-4ZQPNGYQc1rRmAeq8tXL71A-exRTGszN_AzHUWdzrIvbLv7ifys4W_PcAiWYTpDn3cWNhpVMrH"


        protected var title: String? =null
        protected var
        body:kotlin.String? = null
        //protected var to:kotlin.String? = "cSQVNHfyRc6Ohgd51B6Gmv:APA91bGo08S_xcnNF4hGDQLWzmiRzUfIQXssms1SrL2yCN7eS_N6Gxk5_hSAcBOU7AsLAS3XbaLXFO6jfpCnIS5Rj9_bFv1Q_cCFxL7nxRSsi74Jdu3t3R8K0rFfQuPOQfM2dECjz8xv"
        protected var to:kotlin.String? = "csKYN6UxSES87tAqdy-dfF:APA91bFkk34Dd9dSVcWfQMBzqPS5LA2vfyrSMZkm8NnnwPTgiis2DpATxvoaDAQVjYQd_7_iWgZ_JGFazhvq8-OYdqcHC9UrU-YBtQF2Vf92CBgZ5SAXOonYs3SIC6qQ9b6W0CxitIcc"
        protected var image:kotlin.String? = null
        protected var click_action:kotlin.String? = null
        protected var datas: HashMap<String, String>? = null
        protected var topic = false
        protected var result: String? = null

        fun Result(): String? {
            return result
        }


        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun send(title: String, message: String, ctx: Context): String {

                val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                StrictMode.setThreadPolicy(policy)
                try {
                    val json = JSONObject()
                    val json2 = JSONObject()
                    val body = JSONObject()

                    val sharedPreferenceManager = SharedPreferenceManager(ctx)
                 val   phnunber = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.phone.toString()
                 val    userID = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.id.toString()
                 val   fullName = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.name.toString()

                    json2.put("channel_id", "1234");
                    json2.put("user_name", fullName);
                    json2.put("user_id", userID);



                    json.put("to", to);

                    val notification = JSONObject()
                    notification.put("title", title)
                    notification.put("body", message)
                    notification.put("type", "video")
                    json.put("notification", notification);
                    json2.put("notification", notification);
                    body.put("body", json2);
                    json.put("data",body);
                    val conn = URL(BASE_URL).openConnection() as HttpURLConnection
                    conn.connectTimeout = 5000
                    conn.setRequestProperty("Content-Type", "application/json;")
                    conn.setRequestProperty("Authorization", SERVER_KEY)
                    conn.doOutput = true
                    conn.doInput = true
                    conn.requestMethod = "POST"
                    val os = conn.outputStream
                    os.write(json.toString().toByteArray(charset("UTF-8")))
                    os.close()
                    val `in`: InputStream = BufferedInputStream(conn.inputStream)
                    //val result: String = IOUtils.toString(`in`, "UTF-8")
                    val text = String(`in`.readAllBytes(), StandardCharsets.UTF_8)
                    Log.e("onSend Message","OnSend Message text 2 ${text}}")
                    `in`.close()
                    conn.disconnect()

                } catch (e: JSONException) {
                    Log.e("onSend Message","OnSend Message Error ${e.message}}")

                } catch (e: IOException) {
                    Log.e("onSend Message","OnSend Message Error 2 ${e.message}}")
                }

            return ""
        }




}