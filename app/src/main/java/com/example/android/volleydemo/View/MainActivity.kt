package com.example.android.volleydemo.View

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.core.content.getSystemService
import androidx.navigation.fragment.NavHostFragment
import com.example.android.volleydemo.Constants
import com.example.android.volleydemo.MainFragment
import com.example.android.volleydemo.R
import com.example.android.volleydemo.ViewModel.QuoteViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), MainFragment.OnFragmentInteractionListener {
    lateinit var navigator:NavHostFragment
    lateinit var bottomView:BottomNavigationView
    lateinit var toolbar: ActionBar
    lateinit var sharedPreferences:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences =getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        navigator = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        bottomView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        toolbar = supportActionBar!!
        toolbar.title = getString(R.string.find_quotes)

        bottomView.setOnNavigationItemSelectedListener(object:BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(p0: MenuItem): Boolean {
                val id = p0.itemId
                navigateTo(id)
                toolbar.title = p0.title
                return true
            }

        })

        //creates notification channel
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            val notificationMan = getSystemService<NotificationManager>()
            val channel = NotificationChannel("QUOTES_CHANNEL", "QuotesChannel",NotificationManager.IMPORTANCE_DEFAULT)
            notificationMan?.createNotificationChannel(channel)
        }
    }

    override fun onFragmentInteraction(uri: Uri) {

    }



    fun navigateTo(id:Int){
        NavHostFragment.findNavController(navigator).navigate(id)
    }

}
