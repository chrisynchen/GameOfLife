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
import kotlin.test.assertEquals

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
                return ExecutorScheduler.ExecutorWorker({ it.run() }, false)
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
        presenter.setCellArrayElement(0, 0)
        assertEquals(1, presenter.cellArray[0][0])

        presenter.setCellArrayElement(0, 0)
        assertEquals(0, presenter.cellArray[0][0])
    }

    @Test
    fun clearArrayElement() {
        // Given
        presenter.setCellArrayElement(0, 1)
        presenter.setCellArrayElement(1, 2)
        presenter.setCellArrayElement(2, 0)
        presenter.setCellArrayElement(2, 1)
        presenter.setCellArrayElement(2, 2)

        // When
        presenter.clearArrayElement()

        // Then
        assertEquals(0, presenter.cellArray[0][0])
        assertEquals(0, presenter.cellArray[0][1])
        assertEquals(0, presenter.cellArray[0][2])
        assertEquals(0, presenter.cellArray[1][0])
        assertEquals(0, presenter.cellArray[1][1])
        assertEquals(0, presenter.cellArray[1][2])
        assertEquals(0, presenter.cellArray[2][0])
        assertEquals(0, presenter.cellArray[2][1])
        assertEquals(0, presenter.cellArray[2][2])
        assertEquals(0, presenter.cellArray[3][0])
        assertEquals(0, presenter.cellArray[3][1])
        assertEquals(0, presenter.cellArray[3][2])
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
        presenter.setCellArrayElement(0, 1)
        presenter.setCellArrayElement(1, 2)
        presenter.setCellArrayElement(2, 0)
        presenter.setCellArrayElement(2, 1)
        presenter.setCellArrayElement(2, 2)

        // When
        presenter.getNextStatus()

        // Then
        assertEquals(0, presenter.cellArray[0][0])
        assertEquals(0, presenter.cellArray[0][1])
        assertEquals(0, presenter.cellArray[0][2])
        assertEquals(1, presenter.cellArray[1][0])
        assertEquals(0, presenter.cellArray[1][1])
        assertEquals(1, presenter.cellArray[1][2])
        assertEquals(0, presenter.cellArray[2][0])
        assertEquals(1, presenter.cellArray[2][1])
        assertEquals(1, presenter.cellArray[2][2])
        assertEquals(0, presenter.cellArray[3][0])
        assertEquals(1, presenter.cellArray[3][1])
        assertEquals(0, presenter.cellArray[3][2])
    }

}