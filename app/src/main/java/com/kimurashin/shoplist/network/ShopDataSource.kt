package com.kimurashin.shoplist.network

import androidx.compose.runtime.State
import com.kimurashin.shoplist.model.RequestStatus
import com.kimurashin.shoplist.model.ShopItemData
import javax.inject.Inject

class ShopDataSource @Inject constructor(
    private val networkService: ShopNetworkService
) {
    val state: State<RequestStatus> = networkService.state

    suspend fun getItems() = networkService.getItems()
    suspend fun addItem(item: ShopItemData) = networkService.addItem(item)
    suspend fun removeItem(item: ShopItemData) = networkService.removeItem(item)
    suspend fun updateItem(item: ShopItemData) = networkService.updateItem(item)
}