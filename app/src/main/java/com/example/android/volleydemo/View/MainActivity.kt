package com.example.android.volleydemo.View

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHost
import androidx.navigation.fragment.NavHostFragment
import com.example.android.volleydemo.MainFragment
import com.example.android.volleydemo.R
import com.example.android.volleydemo.ViewModel.QuoteViewModel

class MainActivity : AppCompatActivity(), MainFragment.OnFragmentInteractionListener {
    lateinit var navigator:NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigator = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

    }

    override fun onFragmentInteraction(uri: Uri) {

    }

    fun navigateTo(id:Int){
        NavHostFragment.findNavController(navigator).navigate(id)
    }

}
