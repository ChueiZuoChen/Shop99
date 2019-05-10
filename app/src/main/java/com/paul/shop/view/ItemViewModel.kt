package com.paul.shop.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paul.shop.data.ItemRepository
import com.paul.shop.model.Item

class ItemViewModel(application: Application): AndroidViewModel(application) {
    private lateinit var itemRepository: ItemRepository
    init {
        itemRepository = ItemRepository(application)
    }

    fun getItems(): LiveData<List<Item>>{
        return itemRepository.getAllItems()
    }

    fun setCategory(categoryId:String) {
        itemRepository.setCategory(categoryId)
    }
}