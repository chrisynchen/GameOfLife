package com.chris.gameoflife.presenter

import android.util.Log
import com.chris.gameoflife.contract.MainPresenter
import com.chris.gameoflife.contract.MainView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @author chenchris on 2019-06-15.
 */
class MainPresenterImpl(
    gameOfLifeViewWidth: Int,
    gameOfLifeViewHeight: Int,
    private val view: MainView
) : MainPresenter(gameOfLifeViewWidth, gameOfLifeViewHeight, view) {
    private val PEROIDICALLY_SECOND = 300L
    private val TAG = MainPresenterImpl::class.java.simpleName

    private val ref = arrayOf(
        intArrayOf(-1, -1),
        intArrayOf(-1, 0),
        intArrayOf(-1, 1),
        intArrayOf(0, -1),
        intArrayOf(0, 1),
        intArrayOf(1, -1),
        intArrayOf(1, 0),
        intArrayOf(1, 1)
    )

    override fun setArrayElement(x: Int, y: Int, value: Int) {
        cellArray.let {
            Log.e("setArrayElement", "x:$x, y:$y")
            it[x][y] = value
        }
    }

    override fun clearArrayElement() {
        for (i in cellArray.indices) {
            for (j in cellArray[0].indices) {
                cellArray[i][j] = 0
            }
        }
    }

    override fun subscribe() {
        val disposable = Observable.interval(PEROIDICALLY_SECOND, TimeUnit.MILLISECONDS)
            .map {
                getNextStatus()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onRenderNextStatus(it)
            }, {
                Log.e(TAG, it.toString())
            })
        addDisposable(disposable)
    }

    override fun getNextStatus(): MutableList<Triple<Int, Int, Int>> {
        changList.clear()
        val newPixelArray = Array(cellArray.size) { IntArray(cellArray[0].size) }
        for (i in newPixelArray.indices) {
            for (j in newPixelArray[0].indices) {
                val newValue = if (isLive(cellArray, i, j)) 1 else 0
                if (newPixelArray[i][j] == newValue) continue

                newPixelArray[i][j] = newValue
                changList.add(Triple(i, j, newValue))
            }
        }

        for (i in cellArray.indices) {
            for (j in cellArray[0].indices) {
                cellArray[i][j] = newPixelArray[i][j]
            }
        }

        return changList
    }

    private fun isLive(pixelArray: Array<IntArray>, i: Int, j: Int): Boolean {
        var neighborLiveCount = 0
        for (k in ref.indices) {
            if (i + ref[k][0] < 0 || j + ref[k][1] < 0
                || i + ref[k][0] >= pixelArray.size
                || j + ref[k][1] >= pixelArray[0].size
            ) continue

            neighborLiveCount += pixelArray[i + ref[k][0]][j + ref[k][1]]
        }

        return if (pixelArray[i][j] == 0) {
            neighborLiveCount == 3
        } else {
            neighborLiveCount in 2..3
        }
    }
}