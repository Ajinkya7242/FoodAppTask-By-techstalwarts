package com.example.tasktechstalwartsfoodapp.DI

import android.content.Context
import androidx.room.Room
import com.example.tasktechstalwartsfoodapp.database.AppDatabase
import com.example.tasktechstalwartsfoodapp.database.AppDatabaseDAO
import com.example.tasktechstalwartsfoodapp.network.MealApiInterface
import com.example.tasktechstalwartsfoodapp.utils.Constants.BASE_URL
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideApiService(): MealApiInterface {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun providesNotesDao(appDatabase: AppDatabase): AppDatabaseDAO =appDatabase.appDao()

    @Singleton
    @Provides
    fun provideAddDatabase(@ApplicationContext context: Context):AppDatabase
            = Room.databaseBuilder(context,AppDatabase::class.java
        ,"pets_tbl").fallbackToDestructiveMigrationFrom().build()


}