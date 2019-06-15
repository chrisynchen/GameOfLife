package com.chris.gameoflife

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.annotation.IntDef
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


/**
 * @author chenchris on 2019-06-14.
 */
class GameOfLifeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    companion object {
        const val DRAW_TYPE_POINT = 0
        const val DRAW_TYPE_MOVE = 1

        @IntDef(DRAW_TYPE_POINT, DRAW_TYPE_MOVE)
        @Retention(AnnotationRetention.SOURCE)
        annotation class DrawType
    }

    private val paintGreen: Paint = Paint().also {
        it.isAntiAlias = true
        it.isDither = true
        it.color = Color.GREEN
        it.style = Paint.Style.STROKE
        it.strokeJoin = Paint.Join.ROUND
        it.strokeCap = Paint.Cap.ROUND
        it.strokeWidth = 12f
    }

    private val paintWhite: Paint = Paint().also {
        it.isAntiAlias = true
        it.isDither = true
        it.color = Color.WHITE
        it.style = Paint.Style.STROKE
        it.strokeJoin = Paint.Join.ROUND
        it.strokeCap = Paint.Cap.ROUND
        it.strokeWidth = 12f
    }

    var lock = false

    private var listener: Listener? = null
    private var changedList: MutableList<Triple<Int, Int, Int>>? = null
    private var drawType = DRAW_TYPE_POINT
    private var bufferTime: Long = 0L

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun setChangedList(changedList: MutableList<Triple<Int, Int, Int>>) {
        this.changedList = changedList;
    }

    fun setDrawType(@DrawType drawType: Int) {
        this.drawType = drawType
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (lock) {
            changedList?.forEach {
                canvas.drawPoint(
                    it.first.toFloat(), it.second.toFloat(),
                    if (it.third == 0) {
                        paintWhite
                    } else {
                        paintGreen
                    }
                )
            }
            changedList?.clear()
            return
        }

//        canvas.drawPath(path, paint)
        listener?.getPixelArray()?.let {
            for (i in 0 until it.size) {
                for (j in 0 until it[0].size) {
                    if (it[i][j] == 1) {
                        canvas.drawPoint(i.toFloat(), j.toFloat(), paintGreen)
                    }
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        if (x < 0 || x >= ScreenUtil.screenWidth
            || y < 0 || y >= ScreenUtil.screenHeight
        ) return super.onTouchEvent(event)

        val currentTime = System.currentTimeMillis()

        if (currentTime - bufferTime < 100) {
            bufferTime = currentTime
            return super.onTouchEvent(event)
        }

        bufferTime = currentTime

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchDown(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
            }
            MotionEvent.ACTION_UP -> {
                touchUp(x, y)
            }
        }
        return true
    }

    private fun touchDown(x: Float, y: Float) {
        setLive(x, y)
    }

    private fun touchMove(x: Float, y: Float) {
        setLive(x, y)
    }

    private fun touchUp(x: Float, y: Float) {
        setLive(x, y)
    }

    private fun setLive(x: Float, y: Float) {
        if (lock) return

        listener?.apply {
            getPixelArray().let {
                val xInt = x.toInt()
                val yInt = y.toInt()

                if (drawType == DRAW_TYPE_POINT) {
                    if (it[xInt][yInt] == 0) {
                        setPixelArrayElement(xInt, yInt, 1)
                    }
                } else if (drawType == DRAW_TYPE_MOVE) {
                    setPixelArrayElement(xInt, yInt, 1)
                    setPixelArrayElement(xInt + 1, yInt, 1)
                    setPixelArrayElement(xInt, yInt + 1, 1)
                    setPixelArrayElement(xInt + 1, yInt + 1, 1)
                }

                invalidate()
            }
        }
    }

    interface Listener {
        fun setPixelArrayElement(x: Int, y: Int, value: Int)
        fun getPixelArray(): Array<IntArray>
    }
}