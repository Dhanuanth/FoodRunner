package com.dhanuanth.foodapp.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.AppComponentFactory
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.view.View.inflate
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater.from
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.dhanuanth.foodapp.R
import com.dhanuanth.foodapp.adapter.HomeRecyclerAdapter
import com.dhanuanth.foodapp.model.restaurant
import com.dhanuanth.foodapp.util.connectionmanager
import org.json.JSONException
import java.util.*
import java.util.Date.from
import kotlin.Comparator
import kotlin.collections.HashMap
import kotlin.math.cos


class HomeFragment : Fragment() {
    lateinit var recyclerHome: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progresslayout: RelativeLayout
    lateinit var progressbar: ProgressBar
    lateinit var recycleradapter: HomeRecyclerAdapter


    val restlist = arrayListOf<restaurant>()
    var compare1 = Comparator<restaurant> { rest1, rest2 ->

        if (rest1.restrating.compareTo(rest2.restrating, true) == 0)
            rest1.restname.compareTo(rest2.restname, true)
        else
            rest1.restrating.compareTo(rest2.restrating, true)

    }
    var compare2 = Comparator<restaurant> { rest1, rest2 ->

        if (rest1.restprice.compareTo(rest2.restprice, true) == 0)
            rest1.restname.compareTo(rest2.restname, true)
        else
            rest1.restprice.compareTo(rest2.restprice, true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        progresslayout = view.findViewById(R.id.progresslayout)
        progressbar = view.findViewById(R.id.progressbar)
        progresslayout.visibility = View.VISIBLE
        setHasOptionsMenu(true)

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
        if (connectionmanager().checkconnectivity(activity as Context)) {
            val jsonRequestobject = object : JsonObjectRequest(
                Request.Method.GET, url, null, Response.Listener {

                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    try {
                        progresslayout.visibility = View.GONE

                        if (success) {

                            val data2 = data.getJSONArray("data")
                            for (i in 0 until data2.length()) {
                                val bookJsonobject = data2.getJSONObject(i)
                                val bookObject = restaurant(
                                    bookJsonobject.getString("id"),///////////////
                                    bookJsonobject.getString("name"),
                                    bookJsonobject.getString("rating"),
                                    bookJsonobject.getString("cost_for_one"),
                                    bookJsonobject.getString("image_url")
                                )
                                restlist.add(bookObject)
                            }

                            recyclerHome = view.findViewById(R.id.recyclerHome)
                            layoutManager = LinearLayoutManager(activity)
                            recycleradapter = HomeRecyclerAdapter(activity as Context, restlist)
                            recyclerHome.adapter = recycleradapter
                            recyclerHome.layoutManager = layoutManager

                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Some error occured",
                                Toast.LENGTH_LONG
                            )
                                .show()

                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context,
                            "Unexpected error occured",
                            Toast.LENGTH_LONG
                        ).show()

                    }


                },
                Response.ErrorListener {
                    if (activity != null) {
                        Toast.makeText(
                            activity as Context,
                            "Volley Error occured",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["content_type"] = "application/json"
                    headers["token"] = "4988a0ddd5c12c"
                    return headers
                    //  return super.getHeaders()
                }
            }

            queue.add(jsonRequestobject)
        } else {
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater?.inflate(R.menu.menudashboard, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item?.itemId
        if (id == R.id.action_sort) {
            val mDialogView =
                LayoutInflater.from(activity as Context).inflate(R.layout.dialogue, null)
            val dialogue = AlertDialog.Builder(activity as Context)
                .setView(mDialogView)
                .setTitle("Sort By?")
            val cost1: RadioButton = mDialogView.findViewById(R.id.cost1)
            val cost2: RadioButton = mDialogView.findViewById(R.id.cost2)

            val rating: RadioButton = mDialogView.findViewById(R.id.rating)

            dialogue.setPositiveButton("OK") { text, listener ->

                if (rating.isChecked()) {
                    Collections.sort(restlist, compare1)
                    restlist.reverse()
                    recycleradapter.notifyDataSetChanged()
                }
                if (cost1.isChecked()) {
                    Collections.sort(restlist, compare2)
                    recycleradapter.notifyDataSetChanged()
                }
                if (cost2.isChecked()) {
                    Collections.sort(restlist, compare2)
                    restlist.reverse()
                    recycleradapter.notifyDataSetChanged()
                }
            }
            dialogue.setNegativeButton("Cancel") { text, listener ->
                Toast.makeText(activity as Context,"No changes made.",Toast.LENGTH_LONG).show()
            }
            dialogue.create()
            dialogue.show()


        }

        return super.onOptionsItemSelected(item)
    }



}


