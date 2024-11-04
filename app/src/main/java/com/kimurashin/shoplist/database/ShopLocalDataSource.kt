package com.kimurashin.shoplist.database

import com.kimurashin.shoplist.model.ShopItemData
import com.kimurashin.shoplist.model.ShopItemEntity
import javax.inject.Inject

class ShopLocalDataSource @Inject constructor(
    private val shopListDao: ShopListDao
) {
    fun getItems() = shopListDao.getItems()
    suspend fun addItem(item: ShopItemEntity) = shopListDao.addItem(item)
    suspend fun removeItem(item: ShopItemEntity) = shopListDao.removeItem(item)
    suspend fun updateItem(itemId: Long) = shopListDao.updateItem(itemId)
}