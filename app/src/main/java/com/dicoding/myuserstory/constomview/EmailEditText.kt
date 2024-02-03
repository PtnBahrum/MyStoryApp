package com.dicoding.myuserstory.constomview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.myuserstory.R

class EmailEditText : AppCompatEditText {

    constructor(context: Context):super(context){
        init()
    }
    constructor(context: Context, attrs : AttributeSet): super(context,attrs){
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr:Int): super(context,attrs,defStyleAttr){
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = resources.getDrawable(R.drawable.bgedittext)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                error = if (s != null && !Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    resources.getString(R.string.invalid_email)
                } else {
                    null
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                error = if (s != null && !Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    resources.getString(R.string.invalid_email)
                } else {
                    null
                }
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                error = if (s != null && !Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    resources.getString(R.string.invalid_email)
                } else {
                    null
                }
            }
        })
    }

    private fun String.isValidEmail() : Boolean = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}