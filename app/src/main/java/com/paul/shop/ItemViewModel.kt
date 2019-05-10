package com.paul.shop

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ItemViewModel: ViewModel() {
    private var items = MutableLiveData<List<Item>>()
    fun getItems():MutableLiveData<List<Item>>{
        FirebaseFirestore.getInstance()
            .collection("items")
            .orderBy("viewcount",Query.Direction.DESCENDING)
            .limit(10)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (querySnapshot !=null && !querySnapshot.isEmpty){
                    var list = mutableListOf<Item>()
                    for (doc in querySnapshot) {
                        val item = doc.toObject(Item::class.java)
                        item.id = doc.id
                        list.add(item)
                    }
                    items.value = list
//                    items.value = querySnapshot.toObjects(Item::class.java)
                }
            }
        return items
    }
}