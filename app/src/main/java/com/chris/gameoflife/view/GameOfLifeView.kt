package com.chris.gameoflife.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.chris.gameoflife.ScreenUtil
import java.util.concurrent.TimeUnit


/**
 * @author chenchris on 2019-06-14.
 */
class GameOfLifeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    companion object {
        private val DEFAULT_CELL_SIZE = ScreenUtil.screenWidth / 16
    }

    private val CLICK_THRESHOLD = TimeUnit.MILLISECONDS.toMillis(100)
    private val paint = Paint()
    private val rect = Rect()
    private val liveColor = Color.GREEN
    private val deadColor = Color.WHITE
    private var listener: Listener? = null
    private var changedList: MutableList<Triple<Int, Int, Int>>? = null
    private var lastCheckTime: Long = 0L
    private var cellSize = DEFAULT_CELL_SIZE
    var columnSize = ScreenUtil.screenWidth / cellSize
    var rowSize = ScreenUtil.screenHeight / cellSize
    var lock = false

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun setChangedList(changedList: MutableList<Triple<Int, Int, Int>>) {
        this.changedList = changedList
    }

    fun setCellSize(size: Int) {
        cellSize = size
        columnSize = ScreenUtil.screenWidth / cellSize
        rowSize = ScreenUtil.screenHeight / cellSize
    }

    fun getCellSize(): Int {
        return cellSize
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawCells(canvas)
    }

    private fun drawCells(canvas: Canvas) {
        listener?.getPixelArray()?.let {
            for (i in 0 until it.size) {
                for (j in 0 until it[0].size) {
                    rect.set(
                        i * cellSize, j * cellSize,
                        (i + 1) * cellSize, (j + 1) * cellSize
                    )
                    paint.color = if (it[i][j] == 1) {
                        liveColor
                    } else {
                        deadColor
                    }
                    canvas.drawRect(rect, paint)
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

        if (currentTime - lastCheckTime < CLICK_THRESHOLD) {
            return super.onTouchEvent(event)
        }

        lastCheckTime = currentTime

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                setLive(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                setLive(x, y)
            }
            MotionEvent.ACTION_UP -> {
                setLive(x, y)
            }
        }
        return true
    }

    private fun setLive(x: Float, y: Float) {
        if (lock) return

        listener?.apply {
            getPixelArray().let {
                val xInt = (x / cellSize).toInt()
                val yInt = (y / cellSize).toInt()

                if (xInt >= it.size || yInt >= it[0].size) return

                setArrayElement(
                    xInt, yInt, if (it[xInt][yInt] == 0) {
                        1
                    } else {
                        0
                    }
                )

                invalidate()
            }
        }
    }

    interface Listener {
        fun setArrayElement(x: Int, y: Int, value: Int)
        fun getPixelArray(): Array<IntArray>
    }
}