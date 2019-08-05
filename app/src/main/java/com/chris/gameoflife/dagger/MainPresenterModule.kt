package com.chris.gameoflife.dagger

import com.chris.gameoflife.contract.MainPresenter
import com.chris.gameoflife.presenter.MainPresenterImpl
import dagger.Binds
import dagger.Module


/**
 * Created by chris chen on 2019-08-06.
 */

@Module
abstract class MainPresenterModule {
    @Binds
    abstract fun provideMainPresenter(presenter: MainPresenterImpl): MainPresenter
}