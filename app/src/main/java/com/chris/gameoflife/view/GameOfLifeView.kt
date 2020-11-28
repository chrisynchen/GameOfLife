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
    private var lastCheckTime: Long = 0L
    private var cellSize = DEFAULT_CELL_SIZE
    var columnSize = ScreenUtil.screenWidth / cellSize
    var rowSize = ScreenUtil.screenHeight / cellSize
    var lock = false
    private var cellArray: Array<IntArray> = Array(columnSize) { IntArray(rowSize) }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun setChangedList(cellArray: Array<IntArray>) {
        this.cellArray = cellArray
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
        cellArray.let {
            for (i in it.indices) {
                for (j in it[0].indices) {
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

        cellArray.let {
            val xInt = (x / cellSize).toInt()
            val yInt = (y / cellSize).toInt()

            if (xInt >= it.size || yInt >= it[0].size) return

            listener?.apply {
                setArrayElement(xInt, yInt)
            }

            invalidate()
        }
    }

    interface Listener {
        fun setArrayElement(x: Int, y: Int)
    }
}