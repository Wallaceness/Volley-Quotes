package com.example.android.volleydemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.android.volleydemo.ViewModel.QuoteViewModel

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {
    lateinit var SettingsVM:QuoteViewModel
    lateinit var createAlertDialog:CreateAlert

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView= inflater.inflate(R.layout.fragment_settings, container, false)
        SettingsVM = QuoteViewModel(requireActivity().application)
//        SettingsVM.createAlert(null, null, 60*15)
        val createButton = rootView.findViewById<Button>(R.id.addAlert)

        createButton.setOnClickListener {
            openDialog()
        }
        return rootView
    }

    fun openDialog(){
        createAlertDialog = CreateAlert()
        createAlertDialog.show(childFragmentManager, "CreateAlert")
    }

}
