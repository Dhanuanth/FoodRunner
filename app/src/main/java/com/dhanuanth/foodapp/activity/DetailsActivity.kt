package com.dhanuanth.foodapp.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ProgressBar
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
import com.android.volley.toolbox.Volley
import com.dhanuanth.foodapp.R
import com.dhanuanth.foodapp.adapter.DetailsRecyclerAdapter
import com.dhanuanth.foodapp.database.CartDatabase
import com.dhanuanth.foodapp.database.CartEntities
import com.dhanuanth.foodapp.model.Food
import com.dhanuanth.foodapp.util.connectionmanager
import org.json.JSONException

class DetailsActivity : AppCompatActivity() {
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    lateinit var recyclerHome: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recycleradapter: DetailsRecyclerAdapter
    lateinit var proceedbutton: TextView
    lateinit var progresslayout: RelativeLayout
    lateinit var progressbar: ProgressBar
    lateinit var text: TextView
    lateinit var  rlcontent:RelativeLayout
    lateinit var da:DetailsActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        da=this

        proceedbutton = findViewById(R.id.cartbutton)
        toolbar = findViewById(R.id.toolbar2)
        progresslayout = findViewById(R.id.progresslayout)
        progressbar = findViewById(R.id.progressbar)
        rlcontent=findViewById(R.id.rlcontent)
        rlcontent.visibility=View.GONE
        progresslayout.visibility = View.VISIBLE
        setUpToolbar()
        toolbar.setNavigationOnClickListener { onBackPressed() }
        val restaurant_id = intent.getStringExtra("id")
        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/${restaurant_id}"
        if (connectionmanager().checkconnectivity(this)) {

            val json_request = object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                val data = it.getJSONObject("data")
                val data2 = data.getJSONArray("data")
                val success = data.getBoolean("success")

                progresslayout.visibility = View.GONE

                if (success) {
                    val foodlist = arrayListOf<Food>()
                    try {


                        for (i in 0 until data2.length()) {
                            val foodJsonobject = data2.getJSONObject(i)
                            val foodObject = Food(
                                foodJsonobject.getString("id"),///////////////
                                foodJsonobject.getString("name"),
                                foodJsonobject.getString("cost_for_one"),
                                foodJsonobject.getString("restaurant_id")

                            )


                            foodlist.add(foodObject)
                        }

                        recyclerHome = findViewById(R.id.recyclerDetails)
                        layoutManager = LinearLayoutManager(this)
                        recycleradapter = DetailsRecyclerAdapter(this as Context,proceedbutton,rlcontent, 0,foodlist)
                        recyclerHome.adapter = recycleradapter
                        recyclerHome.layoutManager = layoutManager

                    } catch (e: JSONException) {
                        Toast.makeText(this, "JSOn Error", Toast.LENGTH_LONG).show()

                    }
                } else {
                    Toast.makeText(this, "Volley Error", Toast.LENGTH_LONG).show()
                }
            },
                Response.ErrorListener {

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
            rlcontent.setOnClickListener {

                    val intent2 = Intent(this, CartActivity::class.java)
                    intent2.putExtra("text", intent.getStringExtra("text"))
                    intent2.putExtra("res_id", intent.getStringExtra("id"))
                    startActivity(intent2)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay);


            }
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

    fun setUpToolbar() {
        toolbar.setTitleTextAppearance(this, R.style.font2)

        setSupportActionBar(toolbar)
        supportActionBar?.title = intent.getStringExtra("text")
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        val dialogue = AlertDialog.Builder(this)
        if (retrieveall(this as Context).execute().get()) {
            dialogue.setTitle("Confirmation")
            dialogue.setMessage("Going back will reset cart items.Do you still want to proceed?")
            dialogue.setPositiveButton("YES") { text, listener ->
                retrievedelete(this as Context).execute().get()

                super.onBackPressed()

            }
            dialogue.setNegativeButton("NO") { text, listener ->
            }
            dialogue.create()
            dialogue.show()
        } else {
            super.onBackPressed()

        }

    }

    class retrievedelete(val context: Context) : AsyncTask<Void, Void, Int>() {
        override fun doInBackground(vararg p0: Void?): Int {
            val db = Room.databaseBuilder(context, CartDatabase::class.java, "Carts").build()
            val cartno: Int = db.cartDao().deleteallCarts()
            return cartno
        }

    }

    class retrieveall(val context: Context) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg p0: Void?): Boolean {
            val db = Room.databaseBuilder(context, CartDatabase::class.java, "Carts").build()
            val cartno: List<CartEntities> = db.cartDao().getallCarts()
            return cartno.isNotEmpty()
        }

    }
    fun getinstance(): DetailsActivity? {
        return da
    }



}