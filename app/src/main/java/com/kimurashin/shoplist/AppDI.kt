package com.kimurashin.shoplist

import android.content.Context
import androidx.room.Room
import com.kimurashin.shoplist.database.MIGRATION_1_2
import com.kimurashin.shoplist.database.ShopListDatabase
import com.kimurashin.shoplist.database.ShopLocalDataSource
import com.kimurashin.shoplist.network.ShopDataSource
import com.kimurashin.shoplist.network.ShopNetworkService
import com.kimurashin.shoplist.repository.ShopRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppDI {

    @Provides
    @Singleton
    fun provideShopListDao(shopListDatabase: ShopListDatabase) = shopListDatabase.shopListDao()

    @Provides
    @Singleton
    fun provideShopNetworkService() = ShopNetworkService()

    @Provides
    @Singleton
    fun provideShopDataSource(networkService: ShopNetworkService) = ShopDataSource(networkService)

    @Provides
    @Singleton
    fun provideShopRepository(dataSource: ShopLocalDataSource) = ShopRepository(dataSource)

    @Provides
    @Singleton
    fun provideShopDatabase(@ApplicationContext applicationContext: Context): ShopListDatabase {
        return Room.databaseBuilder(
            applicationContext,
            ShopListDatabase::class.java,
            "shop_database"
        ).addMigrations(MIGRATION_1_2).build()
    }
}