package com.chris.gameoflife.presenter

import android.util.Log
import com.chris.gameoflife.contract.MainPresenter
import com.chris.gameoflife.contract.MainView
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.concurrent.Executor
import kotlin.test.assertTrue

/**
 * @author chenchris on 2019-06-17.
 */
@RunWith(PowerMockRunner::class)
@PowerMockIgnore("javax.net.ssl.*")
@PrepareForTest(Log::class)
class MainPresenterImplTest {
    private lateinit var presenter: MainPresenter

    private val view: MainView = mock()

    @Before
    fun setUp() {
        PowerMockito.mockStatic(Log::class.java)

        val immediate = object : Scheduler() {
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, false)
            }
        }

        RxJavaPlugins.setInitIoSchedulerHandler { immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }

        presenter = MainPresenterImpl(4, 3, view)
    }

    @Test
    fun setArrayElement() {
        presenter.setArrayElement(0, 0, 1)
        assertTrue {
            presenter.cellArray[0][0] == 1
        }

        presenter.setArrayElement(0, 0, 0)
        assertTrue {
            presenter.cellArray[0][0] == 0
        }
    }

    @Test
    fun clearArrayElement() {
        for (i in presenter.cellArray.indices) {
            for (j in presenter.cellArray[0].indices) {
                presenter.setArrayElement(i, j, 1)
            }
        }
        presenter.clearArrayElement()
        for (i in presenter.cellArray.indices) {
            for (j in presenter.cellArray[0].indices) {
                assertTrue {
                    presenter.cellArray[i][j] == 0
                }
            }
        }
    }

    /**
     * leetcode example
     * https://leetcode.com/problems/game-of-life/
     * input [[0,1,0],[0,0,1],[1,1,1],[0,0,0]]
     * output [[0,0,0],[1,0,1],[0,1,1],[0,1,0]]
     */
    @Test
    fun getNextStatus_failed() {
        // Given
        presenter.setArrayElement(0, 1, 1)
        presenter.setArrayElement(1, 2, 1)
        presenter.setArrayElement(2, 0, 1)
        presenter.setArrayElement(2, 1, 1)
        presenter.setArrayElement(2, 2, 1)

        // When
        presenter.getNextStatus()

        // Then
        for (i in presenter.cellArray.indices) {
            for (j in presenter.cellArray[0].indices) {
                assertTrue {
                    if ((i == 1 && j == 0) ||
                        (i == 1 && j == 2) ||
                        (i == 2 && j == 1) ||
                        (i == 2 && j == 2) ||
                        (i == 3 && j == 1)
                    ) {
                        presenter.cellArray[i][j] == 1
                    } else {
                        presenter.cellArray[i][j] == 0
                    }
                }
            }
        }
    }

}