package com.davidpy.pizzacounter.di

import android.content.Context
import androidx.room.Room
import com.davidpy.pizzacounter.data.dao.PizzaDao
import com.davidpy.pizzacounter.data.database.PizzaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PizzaDatabase {
        return Room.databaseBuilder(
            context,
            PizzaDatabase::class.java,
            PizzaDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration()
         .build()
    }

    @Provides
    fun providePizzaDao(db: PizzaDatabase): PizzaDao = db.pizzaDao()
}
