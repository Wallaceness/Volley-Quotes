package com.example.android.volleydemo

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class DeleteAlert constructor(parent:SavedQuotesFragment): DialogFragment() {
    val parent=parent

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder: AlertDialog.Builder  = AlertDialog.Builder(requireContext());
        builder.setMessage(R.string.confirm_message)
            .setPositiveButton(R.string.delete, DialogInterface.OnClickListener {dialog, id ->
                parent.deleteQuote()
            })
            .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener {dialog, id ->

            })
        return builder.create()
    }
}