package com.example.app.di

import android.content.Context
import com.example.app.presentation.util.GoogleDriveSharedDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesSharedData(@ApplicationContext context: Context): GoogleDriveSharedDataStore =
        GoogleDriveSharedDataStore(context)
}