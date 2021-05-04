package com.tokopedia.filter.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.filter.R
import com.tokopedia.filter.view.model.ProductX
import kotlinx.android.synthetic.main.main_item.view.*

/**
 * Created on : 30/04/21 | 22.24
 * Author     : dededarirahmadi
 * Name       : dededarirahmadi
 * Email      : dededarirahmadi@gmail.com
 */


class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ListProductViewHolder>() {
    private var listData = ArrayList<ProductX>()

    fun setData(data: List<ProductX>){
        if (data != listData){
            listData.clear()
            listData.addAll(data)
            notifyDataSetChanged()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListProductViewHolder =
            ListProductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.main_item, parent, false))

    override fun onBindViewHolder(holder: ListProductViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount() = listData.size

    inner class ListProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: ProductX){
            Glide.with(itemView.context)
                    .load(data.imageUrl)
                    .into(itemView.img_product)
            itemView.title_product.text = data.name
            itemView.price_product.text = "Rp. ".plus(data.priceInt.toString())
            itemView.location_product.text = data.shop.city
        }
    }
}