package com.example.android.volleydemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.fragment.app.Fragment
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.android.volleydemo.ViewModel.QuoteViewModel
import com.google.common.util.concurrent.ListenableFuture

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
        val wm = WorkManager.getInstance(requireContext())
        val future: ListenableFuture<List<WorkInfo>> = wm.getWorkInfosByTag("QuoteAlert")
        val list: List<WorkInfo> = future.get()
        println(list.toString())
//        SettingsVM.createAlert(null, null, 60*15)
        val createButton = rootView.findViewById<ImageButton>(R.id.addAlert)

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
