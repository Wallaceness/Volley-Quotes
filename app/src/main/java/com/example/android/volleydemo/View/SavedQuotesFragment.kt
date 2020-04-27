package com.example.android.volleydemo.View

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.volleydemo.*
import com.example.android.volleydemo.ViewModel.QuoteViewModel
import com.example.android.volleydemo.model.Quote

/**
 * A simple [Fragment] subclass.
 */
class SavedQuotesFragment : Fragment(),
    AlertLaunchedListener {
    var savedQuotes=arrayListOf<Quote>()
    lateinit var qvm:QuoteViewModel;
    lateinit var deleteDialog: DeleteAlert
    lateinit var selectedQuote: Quote
    lateinit var recycleView: RecyclerView
        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            // Inflate the layout for this fragment
            val rootView:View= inflater.inflate(R.layout.fragment_saved_quotes, container, false)
            (requireActivity() as MainActivity).showBottomNav()
            recycleView = rootView.findViewById<RecyclerView>(R.id.savedQuotesRecyclerview)
            qvm = QuoteViewModel(requireActivity().application)
            val adapter= SavedQuotesAdapter(
                savedQuotes,
                this
            )
            val layoutManager = LinearLayoutManager(requireContext())
            recycleView.layoutManager = layoutManager
            recycleView.adapter = adapter
            qvm.getSavedQuotes().observe(viewLifecycleOwner, Observer { response->
                savedQuotes=response as ArrayList<Quote>
                adapter.updateQuotes(savedQuotes)
            })
            recycleView.addOnScrollListener(object:RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy>0){
                        (activity as MainActivity).toggleBottomNavigation(true)
                    }
                    else if (dy<0){
                        (activity as MainActivity).toggleBottomNavigation(false)
                    }
                    super.onScrolled(recyclerView, dx, dy)
                }
            })

            setHasOptionsMenu(true)
            return rootView
    }

    override fun delete(obj:Any){
        qvm.deleteQuote(obj as Quote)
    }

    fun launchDialog(quote: Quote){
        selectedQuote = quote
        deleteDialog = DeleteAlert(
            this,
            getString(R.string.confirm_message),
            quote
        )
        deleteDialog.show(childFragmentManager, "DeleteAlertDialog")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            inflater.inflate(R.menu.action_menu_saved, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id =item.itemId
        if (id== R.id.multiQuote){
            recycleView.layoutManager = LinearLayoutManager(requireContext())
        }
        else if (id== R.id.gridQuote){
            recycleView.layoutManager = GridLayoutManager(requireContext(),2)

        }
        return super.onOptionsItemSelected(item)
    }

}
