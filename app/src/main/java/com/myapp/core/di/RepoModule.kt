package com.myapp.core.di

import com.myapp.core.dataprovider.RealtimeDbRepository
import com.myapp.core.dataprovider.RealtimeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent ::class)
 abstract class RepoModule {
    @Binds
    abstract fun providesRealtimeRepository(
        repo: RealtimeDbRepository
    ):RealtimeRepository

}