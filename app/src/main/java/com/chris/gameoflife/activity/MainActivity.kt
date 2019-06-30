package com.chris.gameoflife.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.chris.gameoflife.R
import com.chris.gameoflife.contract.MainPresenter
import com.chris.gameoflife.contract.MainView
import com.chris.gameoflife.presenter.MainPresenterImpl
import com.chris.gameoflife.view.GameOfLifeView
import com.chris.gameoflife.view.SeekBarDialog
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), GameOfLifeView.Listener, MainView {

    override fun onRenderNextStatus(cellArray: Array<IntArray>) {
        gameOfLifeView.setChangedList(cellArray)
        gameOfLifeView.invalidate()
    }

    override fun setArrayElement(x: Int, y: Int) {
        return presenter.setCellArrayElement(x, y)
    }

    private lateinit var presenter: MainPresenter

    private val seekBarDialogListener = object : SeekBarDialog.Listener {
        override fun onProgressChanged(process: Int) {
            gameOfLifeView.setCellSize(process)
            presenter.cellArray = Array(gameOfLifeView.columnSize) { IntArray(gameOfLifeView.rowSize) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenterImpl(gameOfLifeView.columnSize, gameOfLifeView.rowSize, this)
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
                presenter.unsubscribe()
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

            R.id.menu_seekBar -> {
                presenter.unsubscribe()
                presenter.clearArrayElement()
                gameOfLifeView.invalidate()
                gameOfLifeView.lock = false
                SeekBarDialog(
                    this,
                    seekBarDialogListener,
                    gameOfLifeView.getCellSize()
                ).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
