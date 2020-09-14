package com.dhanuanth.foodapp.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dhanuanth.foodapp.R
import com.dhanuanth.foodapp.adapter.HistoryRecyclerAdapter
import com.dhanuanth.foodapp.adapter.HomeRecyclerAdapter
import com.dhanuanth.foodapp.model.orderhistory
import com.dhanuanth.foodapp.model.restaurant
import com.dhanuanth.foodapp.util.connectionmanager


class HistoryFragment : Fragment() {
    val orderlist = arrayListOf<orderhistory>()
    lateinit var recyclerHome: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recycleradapter: HistoryRecyclerAdapter
    lateinit var progresslayout: RelativeLayout
    lateinit var progressbar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_history, container, false)
        var sp: SharedPreferences? = activity?.getSharedPreferences(getString(R.string.shared_preferances_name),
            Context.MODE_PRIVATE)
        progresslayout = view.findViewById(R.id.progresslayout)
        progressbar = view.findViewById(R.id.progressbar)
        progresslayout.visibility = View.VISIBLE

        val queue=Volley.newRequestQueue(activity as Context)
        val url="http://13.235.250.119/v2/orders/fetch_result/${sp?.getString("Userid","none")}"
        if (connectionmanager().checkconnectivity(activity as Context)) {

            val json_request = object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                val data = it.getJSONObject("data")
                val success = data.getBoolean("success")
                progresslayout.visibility = View.GONE

                if (success) {

                    val data2 = data.getJSONArray("data")
                    for (i in 0 until data2.length()) {
                        val orderJsonobject = data2.getJSONObject(i)

                        val orderObject = orderhistory(
                            orderJsonobject.getString("order_id"),///////////////
                            orderJsonobject.getString("restaurant_name"),
                            orderJsonobject.getString("total_cost"),
                            orderJsonobject.getString("order_placed_at"),
                            orderJsonobject.getJSONArray("food_items")
                        )
                        orderlist.add(orderObject)
                    }
                    recyclerHome = view.findViewById(R.id.recyclerHistory)
                    recyclerHome.addItemDecoration(DividerItemDecoration(context,
                        DividerItemDecoration.VERTICAL))
                    layoutManager = LinearLayoutManager(activity)
                    recycleradapter = HistoryRecyclerAdapter(activity as Context, orderlist)
                    recyclerHome.adapter = recycleradapter
                    recyclerHome.layoutManager = layoutManager
                    //recyclerHome.itemAnimator=DefaultItemAnimator()


                }


            }, Response.ErrorListener { }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["content_type"] = "application/json"
                    headers["token"] = "4988a0ddd5c12c"
                    return headers
                    //  return super.getHeaders()
                }
            }
            queue.add(json_request)
        }
        else{
            val dialogue = AlertDialog.Builder(activity as Context)
            dialogue.setTitle("Failed")
            dialogue.setMessage("Internet connection not found")
            dialogue.setPositiveButton("Go to settings") { text, listener ->
                val settingsintent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsintent)
                activity?.finish()
            }
            dialogue.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialogue.create()
            dialogue.show()
        }

        return view
    }



}