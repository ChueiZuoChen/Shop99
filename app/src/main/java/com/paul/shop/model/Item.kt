package com.paul.shop.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
@Entity
data class Item(var title:String,
                var price:Int,
                var imageUrl:String,
                @PrimaryKey
                @get:Exclude var id:String,
                var content:String,
                var viewcount:Int,
                var category:String):Parcelable{
    constructor():this("",0, "","","",0,"")
}

