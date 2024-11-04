package com.kimurashin.shoplist.repository

import com.kimurashin.shoplist.database.ShopLocalDataSource
import com.kimurashin.shoplist.model.ShopItemData
import com.kimurashin.shoplist.model.ShopItemEntity
import javax.inject.Inject

class ShopRepository @Inject constructor(
    private val dataSource: ShopLocalDataSource
) {
    fun getItems() = dataSource.getItems()
    suspend fun addItem(item: ShopItemEntity) = dataSource.addItem(item)
    suspend fun removeItem(item: ShopItemEntity) = dataSource.removeItem(item)
    suspend fun updateItem(itemId: Long) = dataSource.updateItem(itemId)
}