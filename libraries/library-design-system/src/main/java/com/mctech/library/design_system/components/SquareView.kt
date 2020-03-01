package com.mctech.library.design_system.components

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * @author MAYCON CARDOSO on 2020-03-01.
 */
class SquareView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr){
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}