package com.dhanuanth.foodapp.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dhanuanth.foodapp.R
import com.dhanuanth.foodapp.util.connectionmanager
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class ForgotPassword : AppCompatActivity() {
    lateinit var button: Button
    lateinit var mobile: EditText
    lateinit var email: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgotpassword)
        button = findViewById(R.id.next)
        mobile = findViewById(R.id.txtmobilef)
        email = findViewById(R.id.txtemailf)

        button.setOnClickListener {
            val queue = Volley.newRequestQueue(this)

            val url = "http://13.235.250.119/v2/forgot_password/fetch_result"
            if (mobile.text.length == 10 && email.text.length != 0) {
                val jsonparams = JSONObject()
                jsonparams.put("mobile_number", mobile.text.toString())
                jsonparams.put("email", email.text.toString())
                if (connectionmanager().checkconnectivity(this)) {

                    val json_request =
                        object : JsonObjectRequest(Method.POST, url, jsonparams, Response.Listener {
                            try {
                                val data = it.getJSONObject("data")
                                val success = data.getBoolean("success")


                                if (success) {
                                    val message = data.getBoolean("first_try")
                                    if (message) {
                                        Toast.makeText(
                                            this,
                                            "OTP has been send(valid for 24 hourse)",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        val intent = Intent(this, ResetActivity::class.java)
                                        intent.putExtra("mobile", mobile.text.toString())
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Use the latest OTP that has been send to you",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        val intent = Intent(this, ResetActivity::class.java)
                                        intent.putExtra("mobile", mobile.text.toString())
                                        startActivity(intent)
                                        finish()

                                    }
                                } else {
                                    email.setCompoundDrawablesWithIntrinsicBounds(
                                        0,
                                        0,
                                        R.drawable.ic_baseline_warning_24,
                                        0
                                    )
                                    Toast.makeText(this, "Invalid entries", Toast.LENGTH_SHORT)
                                        .show()
                                    mobile.setCompoundDrawablesWithIntrinsicBounds(
                                        0,
                                        0,
                                        R.drawable.ic_baseline_warning_24,
                                        0
                                    )


                                }
                            } catch (e: JSONException) {
                                Toast.makeText(
                                    this as Context,
                                    "Some unexpected error occured",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }, Response.ErrorListener {
                            Toast.makeText(this, "Volley Error has occured", Toast.LENGTH_LONG)
                                .show()


                        }) {
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["content_type"] = "application/json"
                                headers["token"] = "4988a0ddd5c12c"
                                return headers
                            }
                        }
                    queue.add(json_request)
                } else {
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
            } else if (mobile.text.length != 10) {
                mobile.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_warning_24,
                    0
                )
                Toast.makeText(this, "Invalid mobile number", Toast.LENGTH_SHORT).show()
            } else {
                email.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_warning_24,
                    0
                )
                Toast.makeText(this, "Invalid Email address", Toast.LENGTH_SHORT).show()
            }
        }


    }
}