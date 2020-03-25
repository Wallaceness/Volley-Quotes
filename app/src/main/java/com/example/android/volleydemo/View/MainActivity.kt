package com.example.android.volleydemo.View

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.fragment.NavHostFragment
import com.example.android.volleydemo.MainFragment
import com.example.android.volleydemo.R
import com.example.android.volleydemo.ViewModel.QuoteViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), MainFragment.OnFragmentInteractionListener {
    lateinit var navigator:NavHostFragment
    lateinit var bottomView:BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigator = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        bottomView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomView.setOnNavigationItemSelectedListener(object:BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(p0: MenuItem): Boolean {
                val id = p0.itemId
                navigateTo(id)
                return true
            }

        })
    }

    override fun onFragmentInteraction(uri: Uri) {

    }



    fun navigateTo(id:Int){
        NavHostFragment.findNavController(navigator).navigate(id)
    }

}
