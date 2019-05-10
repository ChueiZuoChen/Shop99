package com.paul.shop

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_row.view.*

class ItemHolder (view: View) :RecyclerView.ViewHolder(view) {
    var titleText = view.item_title
    var priceText = view.item_price
    var imageView = view.item_imagerul
    var countText = view.item_count
    fun bindTo(item:Item) {
        titleText.setText(item.title)
        priceText.setText(item.price.toString()+" AUD")
        Glide.with(itemView.context)
            .load(item.imageUrl)
            .apply(RequestOptions().override(120))
            .into(imageView)
        countText.setText(item.viewcount.toString())
        countText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.eye,0,0,0)
    }
}