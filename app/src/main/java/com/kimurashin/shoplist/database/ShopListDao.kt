package com.kimurashin.shoplist.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kimurashin.shoplist.model.ShopItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopListDao {

    @Query("SELECT * FROM shop_items ORDER BY id DESC")
    fun getItems(): Flow<List<ShopItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(item: ShopItemEntity)

    @Delete
    suspend fun removeItem(item: ShopItemEntity)

    @Query("UPDATE shop_items SET checked = CASE WHEN checked = 0 THEN 1 ELSE 0 END WHERE id = :itemId")
    suspend fun updateItem(itemId: Long)
}