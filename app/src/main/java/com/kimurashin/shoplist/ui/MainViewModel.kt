package com.kimurashin.shoplist.ui

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kimurashin.shoplist.model.ShopItemEntity
import com.kimurashin.shoplist.repository.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ShopRepository
) : ViewModel() {
    val allItems: Flow<List<ShopItemEntity>> = repository.getItems()

    fun addItem(itemData: ShopItemEntity) = viewModelScope.launch {
        repository.addItem(itemData)
    }

    fun addItem(itemData: String, bitmap: ByteArray? = null) = viewModelScope.launch {
        repository.addItem(ShopItemEntity(itemData, false, image = bitmap))
    }

    fun removeItem(itemData: ShopItemEntity) = viewModelScope.launch {
        repository.removeItem(itemData)
    }

    fun updateItem(itemData: ShopItemEntity) = viewModelScope.launch {
        repository.updateItem(itemData.id)
    }
}