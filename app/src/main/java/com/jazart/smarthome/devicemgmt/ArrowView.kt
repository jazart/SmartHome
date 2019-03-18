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
        paint.strokeWidth = 50f
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
        path.lineTo(centerX, centerY + centerY * 0.70f)
        path.lineTo(centerX * 0.75f, centerY * 1.45f)
        path.moveTo(centerX, centerY + centerY * 0.70f)
        path.lineTo( centerX * 1.25f, centerY * 1.45f)
        canvas.apply {
            drawPath(path, paint)
            save()
        }
    }
}