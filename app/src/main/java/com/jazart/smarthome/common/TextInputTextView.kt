package com.jazart.smarthome.common

import android.content.Context
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.jazart.smarthome.R
import kotlinx.android.synthetic.main.text_input_compount_view.view.*

class TextInputTextView : ConstraintLayout {
    constructor(context: Context)
            : super(context)

    constructor(context: Context, attrs: AttributeSet?)
            : super(context, attrs) {

        LayoutInflater.from(context).inflate(R.layout.text_input_compount_view, this)
        context.withStyledAttributes(attrs, R.styleable.TextInputTextView) {
            val text = this.getText(R.styleable.TextInputTextView_textViewText)
            editableTV.text = text
        }

    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    fun toggleEditableStatus(shouldShowEdit: Boolean = true) {
        if (shouldShowEdit) {
            editTIL.editText?.text = SpannableStringBuilder.valueOf(editableTV.text)
            editTIL.apply {
                alpha = 0f
                visibility = View.VISIBLE
                animate().apply {
                    duration = 300L
                    alpha(1f)
                }
            }
            editableTV.animate().apply {
                alpha(0f)
                duration = 100L
            }
        } else {
            editableTV.animate().apply {
                duration = 50L
                alpha(1f)
            }
            editTIL.animate().apply {
                alpha(0f)
                duration = 100L
            }
            editTIL.visibility = View.GONE
            setText("${editTIL.editText?.text}")
        }
    }

    fun setText(text: String) {
        if (!text.isBlank()) {
            editableTV.text = text
        }
    }

    fun getText(): String = editableTV.text.toString()
}