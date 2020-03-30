package com.example.android.volleydemo

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.fragment.app.DialogFragment

class CreateAlert: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        lateinit var typeSpinner:Spinner
        lateinit var timeSpinner:Spinner
        lateinit var textField:EditText
        lateinit var authorOrKeywordLabel:TextView
        var type="Random"
        var frequency=0

        val rootView = inflater.inflate(R.layout.create_alert_dialog, null)
        textField = rootView.findViewById(R.id.keywordOrAuthor)
        authorOrKeywordLabel = rootView.findViewById(R.id.keywordOrAuthorLabel)
        typeSpinner = rootView.findViewById(R.id.alertSpinner)
        typeSpinner.adapter = ArrayAdapter.createFromResource(requireContext(), R.array.types_array, android.R.layout.simple_spinner_item).also{adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        typeSpinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                type = parent?.getItemAtPosition(position) as String
                if (type=="By Author" || type=="By Keyword"){
                    authorOrKeywordLabel.text =type
                    textField.visibility = View.VISIBLE
                    authorOrKeywordLabel.visibility = View.VISIBLE
                }
                else {
                    textField.visibility = View.GONE
                    authorOrKeywordLabel.visibility = View.GONE
                    textField.text.clear()
                }
            }

        }

        timeSpinner = rootView.findViewById(R.id.frequencySpinner)
        timeSpinner.adapter=ArrayAdapter.createFromResource(requireContext(), R.array.frequency_array, android.R.layout.simple_spinner_dropdown_item)

        timeSpinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                frequency = when(parent?.getItemAtPosition(position)){
                    "Every 30 Minutes"-> 30*60
                    "Every hour"->60*60
                    "Every 2 hours"->120*60
                    "Every 5 hours"->5*60*60
                    "Every 12 hours"->12*60*60
                    "Once a day"->24*60*60
                    else->0
                }
            }

        }


        builder.setView(rootView)
            .setTitle("New Quote Alert")
            .setPositiveButton(R.string.create_alert,DialogInterface.OnClickListener { dialog, id->
                val text = textField?.text.toString()
                val author = if (type=="By Author")text else null
                val keyword = if (type=="By Keyword")text else null
                (parentFragment as SettingsFragment).SettingsVM.createAlert(keyword, author, frequency, type)
            })
            .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener{dialog, id ->
                getDialog()?.cancel()
            })
        return builder.create()
    }
}