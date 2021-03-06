package com.example.android.volleydemo.View

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.ActionBar
import androidx.core.app.ShareCompat
import androidx.core.content.getSystemService
import androidx.navigation.fragment.NavHostFragment
import com.example.android.volleydemo.Constants
import com.example.android.volleydemo.model.Quote
import com.example.android.volleydemo.R
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

    fun toggleBottomNavigation(down:Boolean){
        if (!down){
            bottomView.visibility = View.VISIBLE
        }
        else{
            bottomView.visibility = View.GONE
        }
    }

    fun navigateTo(id:Int){
        NavHostFragment.findNavController(navigator).navigate(id)
    }

    fun shareQuote(quote: Quote){
        val mimeType = "text/plain"
        ShareCompat.IntentBuilder.from(this)
            .setType(mimeType)
            .setChooserTitle("Choose an app to share with: ")
            .setText("“${quote.message}”\n${quote.formatAuthorLife()}")
            .startChooser()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    fun animateQuote(view:View?){
        val animationType = sharedPreferences.getString("AnimationType", "")
        if (animationType!="none" && animationType!=""){
            val animation = AnimationUtils.loadAnimation(this, when(animationType){
                "none"->R.id.noneOption
                "fade"->R.anim.fadein
                "slideTop"->R.anim.toptocenter
                "slideBottom"->R.anim.bottomtocenter
                "slideLeft"->R.anim.lefttoright
                "slideRight"->R.anim.righttoleft
                else-> 0
            })
            view?.startAnimation(animation)
        }
    }

    fun showBottomNav(){
        if (bottomView.visibility == View.GONE){
            bottomView.visibility = View.VISIBLE
        }
    }
}
