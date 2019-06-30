package com.chris.gameoflife.contract

import com.chris.gameoflife.presenter.BasePresenter

/**
 * @author chenchris on 2019-06-14.
 */

interface MainView {
    fun onRenderNextStatus(cellArray: Array<IntArray>)
}

abstract class MainPresenter(gameOfLifeViewWidth: Int, gameOfLifeViewHeight: Int, view: MainView) : BasePresenter() {
    var cellArray = Array(gameOfLifeViewWidth) { IntArray(gameOfLifeViewHeight) }

    abstract fun setCellArrayElement(x: Int, y: Int)
    abstract fun getNextStatus(): Array<IntArray>
    abstract fun clearArrayElement()
}