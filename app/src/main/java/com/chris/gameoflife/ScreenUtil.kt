package com.chris.gameoflife

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

/**
 * @author chenchris on 2019-06-15.
 */
object ScreenUtil {

    var screenWidth: Int = 0
        private set
    var screenHeight: Int = 0
        private set

    fun init(context: Context) {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val screenSize = Point()
        display.getSize(screenSize)
        screenHeight = screenSize.y
        screenWidth = screenSize.x
    }
}