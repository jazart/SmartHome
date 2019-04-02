package com.jazart.smarthome.common

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ConfirmDialog : BottomSheetDialogFragment(), DialogInterface.OnClickListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext()).run {
            setTitle("Add Favorite Device")
            setPositiveButton(android.R.string.ok, this@ConfirmDialog)
            setNegativeButton(android.R.string.no, this@ConfirmDialog)
            create()
        }

    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        dismiss()
    }
}