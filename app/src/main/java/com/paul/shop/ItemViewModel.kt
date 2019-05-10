package com.paul.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ItemViewModel: ViewModel() {
    private var items = MutableLiveData<List<Item>>()
    private var firestoreQueryLiveData = FirestoreQueryLiveData()
    fun getItems():FirestoreQueryLiveData{
        return firestoreQueryLiveData
    }
}