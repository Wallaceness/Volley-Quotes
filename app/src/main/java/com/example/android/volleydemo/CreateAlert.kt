package com.example.android.volleydemo

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class CreateAlert: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater

        builder.setView(inflater.inflate(R.layout.create_alert_dialog, null))
            .setPositiveButton(R.string.create_alert,DialogInterface.OnClickListener { dialog, id->

            })
            .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener{dialog, id ->
                getDialog()?.cancel()
            })
        builder.create()
        return super.onCreateDialog(savedInstanceState)
    }
}