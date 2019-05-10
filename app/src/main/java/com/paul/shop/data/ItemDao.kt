package com.paul.shop.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paul.shop.model.Item

@Dao
interface ItemDao {
    @Query("select * from Item order by viewcount")
    fun getItems() : List<Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItem(item: Item)
}