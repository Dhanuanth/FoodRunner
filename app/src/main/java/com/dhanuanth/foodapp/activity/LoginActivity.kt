package com.dhanuanth.foodapp.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import kotlinx.android.synthetic.main.recycler_home_single_row.*
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class LoginActivity : AppCompatActivity() {
    lateinit var number: EditText
    lateinit var forgot: TextView
    lateinit var account: TextView
    lateinit var login: Button
    lateinit var password: EditText
    lateinit var shared_preferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        forgot = findViewById(R.id.txtforgot)
        account = findViewById(R.id.txtnoaccount)
        login = findViewById(R.id.btnlogin)
        number = findViewById(R.id.txtmobile)
        password = findViewById(R.id.txtpassword)
        shared_preferences = getSharedPreferences(
            getString(R.string.shared_preferances_name),
            Context.MODE_PRIVATE
        )
        val loggedin = shared_preferences.getBoolean("isLoggedIn", false)
        if (!loggedin) {
            forgot.setOnClickListener {
                val intent = Intent(
                    this@LoginActivity,
                    ForgotPassword::class.java
                )
                startActivity(intent)
            }
            account.setOnClickListener {
                val intent = Intent(
                    this@LoginActivity,
                    RegisterActivity::class.java
                )
                startActivity(intent)
            }
            login.setOnClickListener {

                val queue = Volley.newRequestQueue(this)
                val url = "http://13.235.250.119/v2/login/fetch_result/"
                if (connectionmanager().checkconnectivity(this)) {

                    val jsonparams = JSONObject()
                    jsonparams.put("mobile_number", number.text.toString())
                    jsonparams.put("password", password.text.toString())


                    val json_request = object : JsonObjectRequest(Method.POST, url, jsonparams,
                        Response.Listener {
                            try {
                                val data = it.getJSONObject("data")
                                val success = data.getBoolean("success")

                                if (success) {
                                    val data2 = data.getJSONObject("data")
                                    val intent =
                                        Intent(this@LoginActivity, MainActivity::class.java)

                                    shared_preferences.edit().putBoolean("isLoggedIn", true).apply()
                                    shared_preferences.edit()
                                        .putString("Userid", data2.getString("user_id")).apply()
                                    shared_preferences.edit()
                                        .putString("Name", data2.getString("name"))
                                        .apply()
                                    shared_preferences.edit()
                                        .putString("Email", data2.getString("email"))
                                        .apply()
                                    shared_preferences.edit()
                                        .putString("Mobile", data2.getString("mobile_number"))
                                        .apply()
                                    shared_preferences.edit()
                                        .putString("Address", data2.getString("address")).apply()


                                    startActivity(intent)
                                    finish()
                                } else {
                                    if (number.text.toString().length != 10) {
                                        Toast.makeText(
                                            this,
                                            "Invalid username",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        number.setCompoundDrawablesWithIntrinsicBounds(
                                            0,
                                            0,
                                            R.drawable.ic_baseline_warning_24,
                                            0
                                        )
                                        password.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

                                    } else {
                                        number.setCompoundDrawablesWithIntrinsicBounds(
                                            0,
                                            0,
                                            R.drawable.ic_baseline_warning_24,
                                            0
                                        )
                                        password.setCompoundDrawablesWithIntrinsicBounds(
                                            0,
                                            0,
                                            R.drawable.ic_baseline_warning_24,
                                            0
                                        )

                                        Toast.makeText(
                                            this,
                                            "Invalid username or password",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }


                            } catch (e: JSONException) {
                                Toast.makeText(
                                    this as Context,
                                    "Some unexpected error occured",
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                        }


                        , Response.ErrorListener {
                            Toast.makeText(this, "Volley Error", Toast.LENGTH_LONG).show()

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

            }


        } else {
            val intent = Intent(
                this@LoginActivity,
                MainActivity::class.java
            )

            startActivity(intent)
            finish()

        }
    }


}