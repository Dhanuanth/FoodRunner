package com.dhanuanth.foodapp.activity

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dhanuanth.foodapp.R
import com.dhanuanth.foodapp.util.connectionmanager
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class ResetActivity : AppCompatActivity() {
    lateinit var otp: EditText
    lateinit var newpass: EditText
    lateinit var confirmpass: EditText
    lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)
        newpass = findViewById(R.id.newpassword)
        confirmpass = findViewById(R.id.confirmpassword)
        otp = findViewById(R.id.otp)
        button = findViewById(R.id.submitotp)
        button.setOnClickListener {
            if (confirmpass.text.toString() == newpass.text.toString()) {
                if (newpass.text != null && newpass.text.length >= 4) {
                    val queue = Volley.newRequestQueue(this)

                    val url = "http://13.235.250.119/v2/reset_password/fetch_result"
                    if (connectionmanager().checkconnectivity(this)) {

                        val jsonparams = JSONObject()
                        jsonparams.put("otp", otp.text.toString())
                        jsonparams.put("password", newpass.text.toString())
                        jsonparams.put("mobile_number", intent.getStringExtra("mobile"))
                        val json_request = object :
                            JsonObjectRequest(Method.POST, url, jsonparams, Response.Listener {
                                try {
                                    val data = it.getJSONObject("data")
                                    val success = data.getBoolean("success")
                                    if (success) {
                                        val dialogue = AlertDialog.Builder(this as Context)
                                        dialogue.setIcon(R.drawable.ic_baseline_check_24)
                                        dialogue.setTitle("Success")
                                        dialogue.setMessage("Your password has been changed successfully.Click OK to continue.")
                                        dialogue.setPositiveButton("OK") { text, listener ->

                                            finish()
                                        }
                                        dialogue.setNegativeButton(null) { text, listener ->

                                        }


                                        dialogue.setOnCancelListener { finish() }



                                        dialogue.create()
                                        dialogue.show()

                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Something went wrong.Try again later.",
                                            Toast.LENGTH_LONG
                                        ).show()

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

                } else {
                    Toast.makeText(this, "Invalid password", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "passwords  don't match", Toast.LENGTH_LONG).show()

            }


        }


    }

    override fun finish() {
        super.finish()

    }
}