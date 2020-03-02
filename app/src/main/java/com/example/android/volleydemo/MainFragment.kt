package com.example.android.volleydemo

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.android.volleydemo.ViewModel.QuoteViewModel
import com.example.android.volleydemo.databinding.FragmentMainBinding
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MainFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 */
class MainFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private var dataBinder: FragmentMainBinding? = null
    var quoteVM: QuoteViewModel?=null
    var quote: LiveData<JSONObject>?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        QuoteViewModel.VolleyQueue.init(requireActivity()!!.applicationContext)
        dataBinder = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        quoteVM = QuoteViewModel(requireActivity().application)

        quoteVM?.getQuote()?.observe(viewLifecycleOwner, Observer<JSONObject> {response->
            dataBinder?.quote = response
        })

        quoteVM!!.fetchRandom()
        return dataBinder?.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

}
