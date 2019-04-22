package com.jazart.smarthome.common

import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.BUTTON_POSITIVE
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ConfirmDialog : BottomSheetDialogFragment(), DialogInterface.OnClickListener {

    lateinit var targetFrag: OnDialogClicked

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            targetFrag = parentFragment as OnDialogClicked? ?: targetFrag
        } catch (e: ClassCastException) {
            dismiss()
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext()).run {
            setView(android.R.layout.select_dialog_item)
            setTitle(arguments?.getString(DIALOG_MESSAGE) ?: "Are you sure you want to take this action?")
            setPositiveButton(android.R.string.yes, this@ConfirmDialog)
            setNegativeButton(android.R.string.no, this@ConfirmDialog)
            create()
        }
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        if (which == BUTTON_POSITIVE) {
            targetFrag.onOptionClicked(which)
        }
        dismiss()
    }

    companion object {
        const val DIALOG_MESSAGE = "DIALOG_MESSAGE"
        fun newInstance(message: String): ConfirmDialog {
            return ConfirmDialog().apply {
                arguments = Bundle().apply { putString(DIALOG_MESSAGE, message) }
            }
        }
    }

    interface OnDialogClicked {
        fun onOptionClicked(option: Int)
    }
}