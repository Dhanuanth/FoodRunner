package com.dhanuanth.foodapp.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.provider.Settings
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import com.dhanuanth.foodapp.R
import com.dhanuanth.foodapp.adapter.CartsRecyclerAdapter
import com.dhanuanth.foodapp.adapter.DetailsRecyclerAdapter
import com.dhanuanth.foodapp.database.CartDatabase
import com.dhanuanth.foodapp.database.CartEntities
import com.dhanuanth.foodapp.util.connectionmanager
import kotlinx.android.synthetic.main.recycler_details_single_row.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text
import java.util.*


class CartActivity : AppCompatActivity() {
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    lateinit var recyclerCart: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recycleradapter: CartsRecyclerAdapter
    lateinit var placebutton: Button
    lateinit var cartlist: List<CartEntities>
    lateinit var text: TextView
    lateinit var total: TextView
lateinit var ca:Activity
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        ca=this
        toolbar = findViewById(R.id.toolbar3)
        text = findViewById(R.id.restname)
        total = findViewById(R.id.total)
        text.text = intent.getStringExtra("text")
        val db = Room.databaseBuilder(
            this,
            CartDatabase::class.java, "Carts"
        ).build()
        var c: Int = 0
        cartlist = retrievefav(this as Context).execute().get()
        val price = retrieveprice(this as Context).execute().get()
        for (element in price) {
            c += element.toInt()
        }
        val foodid = retrievefoodid(this as Context).execute().get()

        recyclerCart = findViewById(R.id.recyclerCart)
        placebutton = findViewById(R.id.orderbutton)
        layoutManager = LinearLayoutManager(this)
        recycleradapter = CartsRecyclerAdapter(this as Context, cartlist)
        recyclerCart.adapter = recycleradapter
        recyclerCart.layoutManager = layoutManager
        setUpToolbar()
        toolbar.setNavigationOnClickListener { onBackPressed() }
        total.text = " Rs. ${c}"

        placebutton.setOnClickListener {
            val queue = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/place_order/fetch_result/"
            if (connectionmanager().checkconnectivity(this)) {

                val jsonparams = JSONObject()
                jsonparams.put(
                    "user_id",
                    getSharedPreferences(
                        getString(R.string.shared_preferances_name),
                        Context.MODE_PRIVATE
                    ).getString("Userid", "none")
                )
                jsonparams.put("restaurant_id", intent.getStringExtra("res_id"))
                jsonparams.put("total_cost", c.toString())


                val array = JSONArray()

                for (element in foodid) {
                    val food_item_id = JSONObject()
                    food_item_id.put("food_item_id", element)
                    array.put(food_item_id)

                }


                jsonparams.put("food", array)
                val json_request =
                    object : JsonObjectRequest(Method.POST, url, jsonparams, Response.Listener {
                        try {

                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if (success) {
                                retrievedeleteall(this as Context).execute().get()
                                val intent = Intent(this, PlacedActivity::class.java)

                                startActivity(intent)
                                overridePendingTransition(
                                    R.anim.slide_in_bottom,
                                    R.anim.slide_out_top
                                )
                                Handler().postDelayed({

                                    finish()

                                }, 1000)

                            } else {
                                Toast.makeText(
                                    this,
                                    "Some error occured while placing the order.Please try after some time",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        } catch (e: JSONException) {
                            Toast.makeText(this, "Some error Occured", Toast.LENGTH_LONG).show()
                        }
                    }, Response.ErrorListener {
                        Toast.makeText(this, "Some error Occured", Toast.LENGTH_LONG).show()

                    }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["content_type"] = "application/json"
                            headers["token"] = "4988a0ddd5c12c"
                            return headers
                            //  return super.getHeaders()
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
    }

    fun setUpToolbar() {
        toolbar.setTitleTextAppearance(this, R.style.font1)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "My Cart"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class retrievefav(val context: Context) : AsyncTask<Void, Void, List<CartEntities>>() {
        override fun doInBackground(vararg p0: Void?): List<CartEntities> {
            val db = Room.databaseBuilder(context, CartDatabase::class.java, "Carts").build()
            val cartlist: List<CartEntities> = db.cartDao().getallCarts()
            return cartlist
        }

    }

    class retrieveprice(val context: Context) : AsyncTask<Void, Void, Array<String>>() {
        override fun doInBackground(vararg p0: Void?): Array<String> {
            val db = Room.databaseBuilder(context, CartDatabase::class.java, "Carts").build()
            val cartlist: Array<String> = db.cartDao().getTotalPrice()
            return cartlist
        }

    }

    class retrievefoodid(val context: Context) : AsyncTask<Void, Void, Array<Int>>() {
        override fun doInBackground(vararg p0: Void?): Array<Int> {
            val db = Room.databaseBuilder(context, CartDatabase::class.java, "Carts").build()
            val cartlist: Array<Int> = db.cartDao().getTotalFood()
            return cartlist
        }

    }

    class retrievedeleteall(val context: Context) : AsyncTask<Void, Void, Int>() {
        override fun doInBackground(vararg p0: Void?): Int {
            val db = Room.databaseBuilder(context, CartDatabase::class.java, "Carts").build()
            val cartno: Int = db.cartDao().deleteallCarts()
            return cartno
        }
    }

    override fun finish() {

        super.finish()
        overridePendingTransition(R.anim.stay, R.anim.slide_out_right)

    }


}
