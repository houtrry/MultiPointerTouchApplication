package com.houtrry.multipointertouchapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.view.ViewCompat

/**
 * @author: houtrry
 * @time: 2020/4/8
 * @desc:
 */
class MultiPointerTouchView : View {

    private val bitmap: Bitmap by lazy {
        BitmapFactory.decodeResource(context.resources, R.mipmap.test)
    }

    private val paint: Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG)
    }

    private var offsetX: Float = 0f
    private var offsetY: Float = 0f

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, -1)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.drawBitmap(bitmap, offsetX, offsetY, paint)
        }
    }

    private var downX: Float = 0f
    private var downY: Float = 0f
    private var originalX: Float = 0f
    private var originalY: Float = 0f
    private var pointId: Int = 0

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        event?.let {
            when (it.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    downX = it.x
                    downY = it.y
                    originalX = offsetX
                    originalY = offsetY
                    pointId = it.getPointerId(0)
                }
                MotionEvent.ACTION_MOVE -> {
                    val pinterIndex = it.findPointerIndex(pointId)
                    offsetX = originalX + it.getX(pinterIndex) - downX
                    offsetY = originalY + it.getY(pinterIndex) - downY
                }
                MotionEvent.ACTION_UP -> {

                }
                MotionEvent.ACTION_POINTER_DOWN -> {
                    val actionIndex = it.actionIndex
                    downX = it.getX(actionIndex)
                    downY = it.getY(actionIndex)
                    originalX = offsetX
                    originalY = offsetY
                    pointId = it.getPointerId(actionIndex)
                }
                MotionEvent.ACTION_POINTER_UP -> {

                    var actionIndex = it.actionIndex
                    if (it.getPointerId(actionIndex) == pointId) {
                        actionIndex = if (actionIndex == it.pointerCount - 1) {
                            it.pointerCount - 2
                        } else {
                            it.pointerCount - 1
                        }
                        pointId = it.getPointerId(actionIndex)
                        downX = it.getX(actionIndex)
                        downY = it.getY(actionIndex)
                        originalX = offsetX
                        originalY = offsetY
                    }
                }
                else -> {

                }
            }
        }

        ViewCompat.postInvalidateOnAnimation(this)
        return true
    }
}