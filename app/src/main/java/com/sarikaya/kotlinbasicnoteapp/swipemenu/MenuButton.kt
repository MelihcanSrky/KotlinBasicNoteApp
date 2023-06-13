package com.sarikaya.kotlinbasicnoteapp.swipemenu

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

class MenuButton(
    private val context: Context,
    private val imageResId: Int,
    private val color: Int,
    private val listener: RecyclerItemClickListener
) {
    private var pos: Int = 0
    private var clickRegion: RectF? = null
    private var resources: Resources

    init {
        resources = context.resources
    }

    fun onClick(x: Float, y: Float): Boolean {
        if (clickRegion != null && clickRegion!!.contains(x, y)) {
            listener.onClick(pos)
            return true
        }
        return false
    }

    fun onDraw(c: Canvas, rectF: RectF, pos: Int) {
        val p = Paint()
        p.color = color
        c.drawRect(rectF, p)
        val drawable = ContextCompat.getDrawable(context, imageResId)
        val bitmap = drawableToBitmap(drawable)
        c.drawBitmap(bitmap, ((rectF.left + rectF.right) / 2) - 18, ((rectF.top + rectF.bottom) / 2) - 18, p)
        clickRegion = rectF
        this.pos = pos
    }

    private fun drawableToBitmap(drawable: Drawable?): Bitmap {
        if (drawable is BitmapDrawable) return drawable.bitmap
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}
