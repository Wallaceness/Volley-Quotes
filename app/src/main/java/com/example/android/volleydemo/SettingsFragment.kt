package com.example.android.volleydemo

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioGroup
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.android.volleydemo.View.MainActivity
import com.example.android.volleydemo.ViewModel.QuoteViewModel
import com.google.common.util.concurrent.ListenableFuture

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {
    lateinit var SettingsVM:QuoteViewModel
    lateinit var createAlertDialog:CreateAlert
    lateinit var animationsRadio:RadioGroup
    lateinit var sharedPreferences:SharedPreferences
    lateinit var alertList:ArrayList<WorkInfo>
    lateinit var recycler:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sharedPreferences =(requireActivity() as MainActivity).sharedPreferences
        val rootView= inflater.inflate(R.layout.fragment_settings, container, false)
        SettingsVM = QuoteViewModel(requireActivity().application)
        animationsRadio = rootView.findViewById(R.id.animationOptions)

        val wm = WorkManager.getInstance(requireContext())
        val future: ListenableFuture<List<WorkInfo>> = wm.getWorkInfosByTag("QuoteAlert")
        alertList = ArrayList(future.get())
//        initialize recyclerview
        recycler = rootView.findViewById(R.id.alertTable)
        val adapter = AlertsAdapter(alertList)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        val createButton = rootView.findViewById<ImageButton>(R.id.addAlert)

        createButton.setOnClickListener {
            openDialog()
        }

        animationsRadio.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener{group, checkedId ->
            val editor = sharedPreferences?.edit()
            if (checkedId!=-1){
                when (checkedId){
                    R.id.noneOption->editor?.putString("AnimationType","none")
                    R.id.fadeOption->editor?.putString("AnimationType","fade")
                    R.id.slideTopOption->editor?.putString("AnimationType","slideTop")
                    R.id.slideBottomOption->editor?.putString("AnimationType","slideBottom")
                    R.id.slideLeftOption->editor?.putString("AnimationType","slideLeft")
                    R.id.slideRightOption->editor?.putString("AnimationType","slideRight")
                    else->null
                }
                editor?.apply()
            }
        })
        return rootView
    }

    fun openDialog(){
        createAlertDialog = CreateAlert()
        createAlertDialog.show(childFragmentManager, "CreateAlert")
    }

}
