package com.kimurashin.shoplist.model

data class ShopItemData(
    val title: String,
    var checked: Boolean,
    var id: Int? = null
)