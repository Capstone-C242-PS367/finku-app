package com.capstone.finku.data.formvalidation

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.capstone.finku.R

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private val clearButtonImage: Drawable = ContextCompat.getDrawable(context, R.drawable.baseline_close_24)!!
    private val emailFormatError: String = context.getString(R.string.email_error)

    init {
        setOnTouchListener(this)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                error = if (!isValidEmail(s.toString())) emailFormatError else null
                if (s.isNotEmpty()) showClearButton() else hideClearButton()
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
            val isClearButtonClicked = event.x > clearButtonStart
            if (isClearButtonClicked) {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    showClearButton()
                    return true
                }
                if (event.action == MotionEvent.ACTION_UP) {
                    text?.clear()
                    hideClearButton()
                    return true
                }
            }
        }
        return false
    }

    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearButtonImage)
    }

    private fun hideClearButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }
}
