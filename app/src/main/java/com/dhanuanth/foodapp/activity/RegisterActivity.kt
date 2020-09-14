package com.dhanuanth.foodapp.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.mtp.MtpObjectInfo
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dhanuanth.foodapp.R
import com.dhanuanth.foodapp.util.connectionmanager
import org.json.JSONObject
import java.util.*

class RegisterActivity : AppCompatActivity() {
    lateinit var register: Button
    lateinit var Name: TextView
    lateinit var sp: SharedPreferences
    lateinit var email: TextView
    lateinit var mobile: TextView
    lateinit var delivery: TextView
    lateinit var password: TextView
    lateinit var cpassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        register = findViewById(R.id.register)
        Name = findViewById(R.id.txtname)
        email = findViewById(R.id.txtemail)
        mobile = findViewById(R.id.txtmobilenumber)
        delivery = findViewById(R.id.txtdelivery)
        password = findViewById(R.id.txtpass)
        cpassword = findViewById(R.id.txtconfirm)


        sp = getSharedPreferences(
            getString(R.string.shared_preferances_name),
            Context.MODE_PRIVATE
        )
        register.setOnClickListener {
            if(Name.text.toString().length<3){
                Name.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_warning_24,0)
            }
            else{
                Name.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)

            }
            if(mobile.text.toString().length!=10){
                mobile.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_warning_24,0)
            }
            else{
                mobile.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)

            }
            if(password.text.toString().length<4){
                password.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_warning_24,0)
            }
            else{
                password.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)

            }
            if (email.text.toString().isEmpty()){
                email.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_warning_24,0)

            }
            else{
                email.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)

            }
            if(password.text.toString()!=cpassword.text.toString()){
                password.text=null
                cpassword.text=null
                password.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_warning_24,0)
                cpassword.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_warning_24,0)

            }
            else{
                password.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
                cpassword.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)

            }
            val queue = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/register/fetch_result"
            val jsonparams = JSONObject()

            jsonparams.put("name", Name.text.toString())
            jsonparams.put("mobile_number", mobile.text.toString())
            jsonparams.put("email", email.text.toString())
            jsonparams.put("address", delivery.text.toString())
            jsonparams.put("password", password.text.toString())

            if (connectionmanager().checkconnectivity(this)) {

            val json_request =
                object : JsonObjectRequest(Method.POST, url, jsonparams, Response.Listener {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    val message = data.getString("errorMessage")

                    if (success) {
                        val intent = Intent(this, MainActivity::class.java)
                        getSharedPreferences(
                            "shared_preference",
                            Context.MODE_PRIVATE
                        ).edit().putBoolean("isLoggedIn", true).apply()

                        startActivity(intent)
                        finish()


                    } else {
                        Toast.makeText(this, message.toString(), Toast.LENGTH_LONG).show()
                    }


                },
                    Response.ErrorListener {

                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["content_type"] = "application/json"
                        headers["token"] = "4988a0ddd5c12c"
                        return headers
                    }
                }
            queue.add(json_request)
        }
        else{
            val dialogue = AlertDialog.Builder(this)
            dialogue.setTitle("Failed")
            dialogue.setMessage("Internet connection not found")
            dialogue.setPositiveButton("Go to settings") { text, listener ->
                val settingsintent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsintent)
                this?.finish()
            }
            dialogue.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(this)
            }
            dialogue.create()
            dialogue.show()
        }


    }


    }




}