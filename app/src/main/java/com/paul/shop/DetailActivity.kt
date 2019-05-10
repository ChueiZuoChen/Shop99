package com.paul.shop

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.paul.shop.model.Item
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    lateinit var item: Item
    private val TAG = DetailActivity::class.java.simpleName
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        item = intent.getParcelableExtra<Item>("ITEM")
        Log.d(TAG, "onCreate: ${item.id} / ${item.title}")
        webview.settings.javaScriptEnabled = true
        webview.loadUrl(item.content)
    }

    override fun onStart() {
        super.onStart()
        item.viewcount++
        item.id?.let {
            FirebaseFirestore.getInstance().collection("items")
                .document(it).update("viewcount",item.viewcount)
        }
    }
}
