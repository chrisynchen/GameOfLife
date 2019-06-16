package com.chris.gameoflife.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.SeekBar
import com.chris.gameoflife.R
import com.chris.gameoflife.ScreenUtil
import kotlinx.android.synthetic.main.seekbar_dialog.*

/**
 * @author chenchris on 2019-06-16.
 */
class SeekBarDialog(context: Context, val listener: Listener) : Dialog(context) {

    private val MAX_VALUE = ScreenUtil.screenWidth / 4
    private val MIN_VALUE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seekbar_dialog)

        seekBar.apply {
            max = MAX_VALUE
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    listener.onProgressChanged(
                        if (progress <= 0) {
                            MIN_VALUE
                        } else {
                            progress
                        }
                    )
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    //do nothing
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    //do nothing
                }
            })
        }
    }

    interface Listener {
        fun onProgressChanged(process: Int)
    }
}