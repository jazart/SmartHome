package com.jazart.smarthome

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.text_input_compount_view.view.*

class TextInputTextView : ConstraintLayout {
    constructor(context: Context)
            : super(context)

    constructor(context: Context, attrs: AttributeSet?)
            : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    init {
        LayoutInflater.from(context).inflate(R.layout.text_input_compount_view, this)
    }

    fun toggleEditableStatus(shouldShowEdit: Boolean = true) {
        if (shouldShowEdit) {
            editableTV.animate().apply {
                translationYBy(1f)
                duration = 500L
                alpha(1f)
                start()
            }
            editTIL.visibility = View.GONE
        } else {
            editableTV.animate().apply {
                translationYBy(0f)
                duration = 250L
                alpha(0f)
                start()
            }
            editTIL.visibility = View.VISIBLE
        }
    }

}