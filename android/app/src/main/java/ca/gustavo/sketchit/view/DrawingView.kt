package ca.gustavo.sketchit.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View

private const val STROKE_WIDTH = 20F
private const val PAINT_COLOR = 0xFF660000.toInt()

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var isDrawable = true
    private val path = Path()
    private val canvasPaint = Paint(Paint.DITHER_FLAG)
    private var onDrawListener: DrawingListener? = null

    private lateinit var canvas: Canvas
    private lateinit var bitmap: Bitmap

    fun setOnDrawListener(listener: DrawingListener) {
        onDrawListener = listener
    }

    private val paint = Paint().apply {
        isAntiAlias = true
        color = PAINT_COLOR
        strokeWidth = STROKE_WIDTH
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawBitmap(bitmap, 0F, 0F, canvasPaint)
        canvas?.drawPath(path, paint)
    }

    fun startDraw(x: Float, y: Float) {
        path.moveTo(x, y)
        invalidate()
    }

    fun draw(x: Float, y: Float) {
        path.lineTo(x, y)
        invalidate()
    }

    fun endDraw() {
        canvas.drawPath(path, paint)
        path.reset()
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isDrawable) return false

        when (event?.action) {
            ACTION_DOWN -> {
                path.moveTo(event.x, event.y)
                onDrawListener?.onStartDraw(event.x, event.y)
                performClick()
            }
            ACTION_MOVE -> {
                path.lineTo(event.x, event.y)
                onDrawListener?.onDraw(event.x, event.y)
                performClick()
            }
            ACTION_UP -> {
                canvas.drawPath(path, paint)
                path.reset()
                onDrawListener?.onEndDraw()
                performClick()
            }
            else -> return false
        }

        invalidate()
        return true
    }

    override fun performClick(): Boolean {
        return if (isDrawable) true else super.performClick()
    }
}