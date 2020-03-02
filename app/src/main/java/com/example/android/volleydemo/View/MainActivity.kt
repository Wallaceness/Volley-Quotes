package com.example.android.volleydemo.View

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.volleydemo.MainFragment
import com.example.android.volleydemo.R
import com.example.android.volleydemo.ViewModel.QuoteViewModel

class MainActivity : AppCompatActivity(), MainFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    }

    override fun onFragmentInteraction(uri: Uri) {

    }

}
