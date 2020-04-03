package com.example.android.volleydemo

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

class DeleteAlert constructor(parent: AlertLaunchedListener, var message:String, var obj:Any): DialogFragment() {
    val parent=parent

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder: AlertDialog.Builder  = AlertDialog.Builder(requireContext());
        builder.setMessage(message)
            .setPositiveButton(R.string.delete, DialogInterface.OnClickListener {dialog, id ->
                parent.delete(obj)
            })
            .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener {dialog, id ->

            })
        return builder.create()
    }
}