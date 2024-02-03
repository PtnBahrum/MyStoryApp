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

class MyEditText : AppCompatEditText {

    constructor(context: Context):super(context){
        init()
    }
    constructor(context: Context, attrs : AttributeSet): super(context,attrs){
        init()
    }
    constructor(context: Context,attrs: AttributeSet, defStyleAttr:Int): super(context,attrs,defStyleAttr){
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = resources.getDrawable(R.drawable.bgedittext)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
    private fun init() {
        addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable) {
                if(inputType == 50){
                    if(!s.toString().isValidEmail()){
                        error = resources.getString(R.string.invalid_email)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(inputType == 129){
                    if(s.toString().length < 8){
                        error = resources.getString(R.string.too_short)
                    }
                }
                if(s.isEmpty()){
                    error = resources.getString(R.string.must_fill)
                }
            }
        })
    }

    private fun String.isValidEmail() : Boolean = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}