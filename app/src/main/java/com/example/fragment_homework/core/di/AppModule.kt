package com.example.fragment_homework.core.di

import android.content.Context
import com.example.fragment_homework.core.data.storage.TempStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesStorage(@ApplicationContext context: Context) = TempStorage(context)
}