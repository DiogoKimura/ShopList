package com.kimurashin.shoplist.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_items")
data class ShopItemEntity(
    val title: String,
    val checked: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val image: ByteArray? = null
)