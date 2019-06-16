package com.chris.gameoflife.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.chris.gameoflife.GameOfLifeView
import com.chris.gameoflife.R
import com.chris.gameoflife.contract.MainPresenter
import com.chris.gameoflife.contract.MainView
import com.chris.gameoflife.presenter.MainPresenterImpl
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), GameOfLifeView.Listener, MainView {
    override fun onRenderNextStatus(tripleList: MutableList<Triple<Int, Int, Int>>) {
        gameOfLifeView.setChangedList(tripleList)
        gameOfLifeView.invalidate()
    }

    override fun setArrayElement(x: Int, y: Int, value: Int) {
        return presenter.setArrayElement(x, y, value)
    }

    override fun getPixelArray(): Array<IntArray> {
        return presenter.pixelArray
    }

    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenterImpl(GameOfLifeView.COLUMN_SIZE, GameOfLifeView.ROW_SIZE, this)
        gameOfLifeView.setListener(this)
    }

    override fun onPause() {
        presenter.unsubscribe()
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.activity_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.menu_start -> {
                gameOfLifeView.lock = true
                presenter.subscribe()
                true
            }
            R.id.menu_pause -> {
                presenter.unsubscribe()
                gameOfLifeView.lock = false
                true
            }
            R.id.menu_clear -> {
                presenter.unsubscribe()
                presenter.clearArrayElement()
                gameOfLifeView.invalidate()
                gameOfLifeView.lock = false
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
