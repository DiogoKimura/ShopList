package com.kimurashin.shoplist.network

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.kimurashin.shoplist.model.RequestStatus
import com.kimurashin.shoplist.model.ShopItemData
import kotlinx.coroutines.delay

class ShopNetworkService {
    private val itemList: MutableList<ShopItemData> = mutableListOf()

    private val _state = mutableStateOf(RequestStatus(false, itemList))
    val state: State<RequestStatus> = _state

    suspend fun getItems() {
        setLoadingAndDelay()

        itemList.addAll(
            mutableStateListOf(
                ShopItemData("Maçã", false, 0),
                ShopItemData("Banana", false, 1),
                ShopItemData("Leite", false, 2),
                ShopItemData("Manga", false, 3),
                ShopItemData("Pão", false, 4),
            )
        )
        _state.value = _state.value.copy(isLoading = false, data = itemList)
    }

    suspend fun addItem(item: ShopItemData) {
        setLoadingAndDelay()

        updateState {
            item.id = itemList.size
            itemList.add(item)
        }
    }

    suspend fun removeItem(item: ShopItemData) {
        setLoadingAndDelay()

        updateState {
            itemList.remove(item)
        }
    }

    suspend fun updateItem(item: ShopItemData) {
        setLoadingAndDelay()

        updateState {
            val index = itemList.indexOf(item)
            if (index != -1) {
                val oldItem = itemList[index]
                itemList.removeAt(index)
                itemList.add(item.copy(checked = !oldItem.checked))
            }
        }
    }

    private suspend fun setLoadingAndDelay() {
        _state.value = _state.value.copy(isLoading = true)
        delay(500)
    }

    private fun updateState(callback: () -> Unit) {
        callback.invoke()
        _state.value = _state.value.copy(false, itemList)
    }
}