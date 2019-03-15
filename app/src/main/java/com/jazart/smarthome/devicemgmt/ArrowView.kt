package com.jazart.smarthome.devicemgmt

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.jazart.smarthome.R
import kotlin.math.round


class ArrowView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val paint = Paint(R.color.colorAccent)
    val path = Path()
    val rect = RectF()
    var cornerRadius = 0f
    init {
        paint.color = resources.getColor(R.color.colorAccent, Resources.getSystem().newTheme())
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 20f
        paint.strokeJoin = Paint.Join.ROUND
        cornerRadius = round(64f * resources.displayMetrics.scaledDensity)
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        val desiredWidth = round(paddingLeft + paddingRight + 1000f)
//        val desiredHeight = round(paddingTop + paddingBottom + 1000f)
//        val finalWidth = resolveSize(desiredWidth.toInt(), widthMeasureSpec)
//        val finalHeight = resolveSize(desiredHeight.toInt(), heightMeasureSpec)
//        setMeasuredDimension(finalWidth, finalHeight)
//    }

    override fun onDraw(canvas: Canvas?) {
        val canvas = canvas ?: return
        canvas.save()
        val centerX = width * 0.5f
        val centerY = height * 0.5f
        path.rewind()
        path.moveTo(centerX, centerY)
        path.lineTo(centerX, centerY + 100f)
        path.lineTo(centerX * 0.85f, height * 0.75f)
        path.moveTo(centerX, centerY + 100f)
        path.lineTo( centerX * 1.15f, height * 0.75f)
        rect.set(width * .25f, 0f, width * .75f, height.toFloat() * .5f)
        canvas.apply {
            drawPath(path, paint)
            save()
        }
    }
}