package com.kimurashin.shoplist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kimurashin.shoplist.model.ShopItemEntity
import com.kimurashin.shoplist.util.Converter

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE shop_items ADD COLUMN image BLOB")
    }
}

@TypeConverters(Converter::class)
@Database(entities = [ShopItemEntity::class], version = 2)
abstract class ShopListDatabase: RoomDatabase() {
    abstract fun shopListDao(): ShopListDao
}