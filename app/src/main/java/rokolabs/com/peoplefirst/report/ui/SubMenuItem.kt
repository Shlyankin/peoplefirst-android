package rokolabs.com.peoplefirst.report.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import rokolabs.com.peoplefirst.R

import android.content.res.TypedArray
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class SubMenuItem(context: Context?) : ConstraintLayout(context) {
    var textView:TextView
    var imageView:ImageView
    constructor(context: Context?, attrs: AttributeSet?) : this(context) {
        val ta = context?.obtainStyledAttributes(attrs, R.styleable.SubMenuItem, 0, 0)
        try {
            var check=ta?.getBoolean(R.styleable.SubMenuItem_checked,false)
            var text =ta?.getText(R.styleable.SubMenuItem_title)
            var active=ta?.getBoolean(R.styleable.SubMenuItem_active,false)
            setActive(active!!)
            setChecked(check!!)
            setText(text.toString())
            invalidate()
        } finally {
            ta?.recycle()
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_sub_menu_item, this, true)
        textView=findViewById<TextView>(R.id.textView3)
        imageView=findViewById(R.id.imageView2)
    }
    fun setText(string: String){
        textView.setText(string)
    }
    fun setChecked(boolean: Boolean){
        if(boolean){
            imageView.setImageResource(R.drawable.rounded_checked)
        }else{
            imageView.setImageResource(R.drawable.rounded_unchecked)
        }
    }
    fun setActive(boolean: Boolean){
        if(boolean){
            textView.alpha=1.0f
        }else{
            textView.alpha=0.6f
        }
    }
}