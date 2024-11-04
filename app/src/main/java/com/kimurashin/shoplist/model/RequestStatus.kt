package com.kimurashin.shoplist.model

data class RequestStatus(
    val isLoading: Boolean,
    val data: MutableList<ShopItemData>
)