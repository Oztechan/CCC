/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.mustafaozhan.data.model.OfflineRates
import com.github.mustafaozhan.data.util.execSQL1To2
import com.github.mustafaozhan.data.util.execSQL2To3

@Suppress("MagicNumber")
@Database(entities = [(OfflineRates::class)], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "application_database.sqlite"

        private val MIGRATION_1_TO_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) = database.execSQL1To2()
        }
        private val MIGRATION_2_TO_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) = database.execSQL2To3()
        }

        fun createAppDatabase(
            context: Context
        ): AppDatabase = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).createFromAsset(DATABASE_NAME)
            .addMigrations(MIGRATION_1_TO_2, MIGRATION_2_TO_3)
            .build()
    }

    abstract fun offlineRatesDao(): OfflineRatesDao
}
