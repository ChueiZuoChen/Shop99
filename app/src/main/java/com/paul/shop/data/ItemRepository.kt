package com.paul.shop.data

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import com.paul.shop.model.Item
import com.paul.shop.view.FirestoreQueryLiveData

class ItemRepository(applcation:Application){
    private var itemDao: ItemDao
    private lateinit var items:LiveData<List<Item>>
    private var firestoreQueryLiveData = FirestoreQueryLiveData()
    private var network = false
    init {
        itemDao = ItemDatabase.getDatabase(applcation).getItemDao()
        items = itemDao.getItems()
        val cm = applcation.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        network = networkInfo.isConnected
    }

    fun getAllItems() : LiveData<List<Item>> {
        if (network) {
            items = firestoreQueryLiveData
            Log.d("MainActivity","network on")
        } else {
            items = itemDao.getItems()
            Log.d("MainActivity","network off")
        }
        return items
    }

    fun setCategory(categoryId: String) {
        if (network) {
            firestoreQueryLiveData.setCategory(categoryId)
        } else {
            items = itemDao.getItemsByCategory(categoryId)
        }
    }
}