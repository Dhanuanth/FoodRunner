package com.dhanuanth.foodapp.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.MenuItem
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.room.Room

import com.dhanuanth.foodapp.R
import com.dhanuanth.foodapp.database.CartDatabase
import com.dhanuanth.foodapp.fragment.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_favourite.*

class MainActivity : AppCompatActivity() {
    lateinit var text: TextView
    lateinit var logout: TextView
    lateinit var drawer: DrawerLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var coordinator: CoordinatorLayout
    lateinit var frame: FrameLayout
    lateinit var navigation: NavigationView
    lateinit var name: TextView
    lateinit var mobile: TextView
    lateinit var login: ImageView
    var previousMenuItem: MenuItem? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawer = findViewById(R.id.drawer)
        toolbar = findViewById(R.id.toolbar)
        coordinator = findViewById(R.id.coordinator)
        frame = findViewById(R.id.frame)
        navigation = findViewById(R.id.navigation)

        val view = navigation.getHeaderView(0)
        name = view.findViewById(R.id.name)
        mobile = view.findViewById(R.id.mobile)
        login = view.findViewById(R.id.loginimage)
        name.text = getSharedPreferences("shared_preference", Context.MODE_PRIVATE).getString(
            "Name",
            "NONE"
        )
        mobile.text = "+91-" + getSharedPreferences(
            "shared_preference",
            Context.MODE_PRIVATE
        ).getString("Mobile", "NONE")
        DetailsActivity.retrievedelete(this as Context).execute().get()

        login.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame, ProfileFragment())
                .commit()
            supportActionBar?.title = "Profile"
            drawer.closeDrawers()
        }

        navigation.setNavigationItemSelectedListener {
            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it
            when (it.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, HomeFragment())
                        .commit()
                    supportActionBar?.title = "All Restaurants"
                    drawer.closeDrawers()
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, ProfileFragment())
                        .commit()
                    supportActionBar?.title = "Profile"
                    drawer.closeDrawers()
                }
                R.id.favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, Favouritefragment())
                        .commit()
                    supportActionBar?.title = "Favourites"
                    drawer.closeDrawers()
                }
                R.id.faq -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FaqFragment())
                        .commit()
                    supportActionBar?.title = "FAQ"
                    drawer.closeDrawers()
                }
                R.id.logout -> {
                    val dialogue = AlertDialog.Builder(this as Context)
                    dialogue.setTitle("Log out")
                    dialogue.setMessage("Are you sure you want to logout?")
                    dialogue.setPositiveButton("Yes") { text, listener ->
                        val intent = Intent(this, LoginActivity()::class.java)
                        getSharedPreferences("shared_preference", Context.MODE_PRIVATE).edit()
                            .putBoolean("isLoggedIn", false).apply()
                        startActivity(intent)
                        finish()
                    }
                    dialogue.setNegativeButton("No") { text, listener ->

                    }

                    dialogue.create()
                    dialogue.show()


                }
                R.id.history -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, HistoryFragment())
                        .commit()
                    supportActionBar?.title = "My Previous Orders"

                    drawer.closeDrawers()
                }
            }

            return@setNavigationItemSelectedListener true
        }
        setUpToolbar()
        openhome()
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this, drawer,
            R.string.open_drawer, R.string.close_drawer
        )
        drawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

    }

    fun setUpToolbar() {
        toolbar.setTitleTextAppearance(this, R.style.font1)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "My Restaurents"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home)
            drawer.openDrawer(GravityCompat.START)
        return super.onOptionsItemSelected(item)
    }

    fun openhome() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, HomeFragment())
            .commit()
        supportActionBar?.title = "All Restaurants"
        navigation.setCheckedItem(R.id.home)

    }
 var c=0;
    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        when (frag) {
            !is HomeFragment -> openhome()
            else -> {
                if(c==0) {c=1
                    Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()
                    Handler().postDelayed({

                        c=0

                    }, 2000)

                }
                else {
                    ActivityCompat.finishAffinity(this)
                }
            }
        }
    }


}


