package com.chris.gameoflife.dagger

import com.chris.gameoflife.activity.MainActivity
import com.chris.gameoflife.contract.MainView
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named

/**
 * Created by chris chen on 2019-08-06.
 */

@Component(modules = [MainPresenterModule::class])
interface MainPresenterComponent {
    fun inject(mainActivity: MainActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun gameOfLifeViewWidth(@Named("gameOfLifeViewWidth") gameOfLifeViewWidth: Int): Builder

        @BindsInstance
        fun gameOfLifeViewHeight(@Named("gameOfLifeViewHeight") gameOfLifeViewHeight: Int): Builder

        @BindsInstance
        fun view(mainView: MainView): Builder

        fun build(): MainPresenterComponent
    }
}