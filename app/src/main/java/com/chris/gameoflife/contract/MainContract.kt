package com.chris.gameoflife.contract

import com.chris.gameoflife.presenter.BasePresenter

/**
 * @author chenchris on 2019-06-14.
 */

interface MainView {
    fun onRenenderNextStatus(tripleList: MutableList<Triple<Int, Int, Int>>)
}

abstract class MainPresenter(gameOfLifeViewWidth: Int, gameOfLifeViewHeight: Int, view: MainView) : BasePresenter() {
    val pixelArray = Array(gameOfLifeViewWidth) { IntArray(gameOfLifeViewHeight) }

    //x,y,value
    val changList = mutableListOf<Triple<Int, Int, Int>>()
    abstract fun setPixelArrayElement(x: Int, y: Int, value: Int)
}