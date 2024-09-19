package com.example.app.di

import android.app.Application
import android.content.Context
import com.example.app.data.repositories.FirestoreRepository
import com.example.app.data.repositories.IFirestoreRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.installations.FirebaseInstallations
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AppModule {
    @Provides
    @ViewModelScoped
    fun provideContext(application: Application): Context = application.applicationContext

    @Provides
    @ViewModelScoped
    fun provideFirestoreInstance() = FirebaseFirestore.getInstance()

    @Provides
    @ViewModelScoped
    fun provideInstallationId():
            FirebaseInstallations {
        return FirebaseInstallations.getInstance()
    }

    @Provides
    @ViewModelScoped
    fun provideFirestoreRepository(
        db: FirebaseFirestore,
        firebaseInstallations: FirebaseInstallations
    ) =
        FirestoreRepository(db, firebaseInstallations)

}

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModuleBinds {

    @Binds
    @ViewModelScoped
    abstract fun bindFirestoreRepository(
        firestoreRepository: FirestoreRepository
    ): IFirestoreRepository
}



