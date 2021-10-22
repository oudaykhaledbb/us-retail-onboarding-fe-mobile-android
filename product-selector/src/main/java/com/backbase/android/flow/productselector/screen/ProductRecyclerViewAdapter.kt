package com.backbase.android.flow.productselector.screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.backbase.android.flow.productselector.models.Product
import com.backbase.android.flow.productselector.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import io.github.vejei.carouselview.CarouselAdapter
import kotlinx.android.synthetic.main.row_product.view.*
import java.util.*
import kotlin.collections.ArrayList

class ProductRecyclerViewAdapter(private val imageBaseUrl: String, private val onProductSelected: ( (Int, Product, Boolean) -> Boolean )? ) :
    CarouselAdapter<ProductRecyclerViewAdapter.ItemViewHolder>() {

    private val products = ArrayList<Product>()
    var selectedProducts = ArrayList<Product>()

    override fun onCreatePageViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_product,
                parent,
                false
            ))

    override fun getPageCount() = products.size

    override fun onBindPageViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(products[position], View.OnClickListener {
            onProductSelected?.invoke(position, products[position], selectedProducts.contains(products[position]))
        })
    }

    fun selectProduct(product: Product){
        selectedProducts.add(product)
        notifyDataSetChanged()
    }

    fun unselectProduct(product: Product){
        selectedProducts.remove(product)
        notifyDataSetChanged()
    }

    fun setSingleSelection(product: Product){
        selectedProducts.clear()
        selectedProducts.add(product)
        notifyDataSetChanged()
    }

    fun setItems(newItems: List<Product>) {
        products.clear()
        products.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(product: Product, onClickListener: View.OnClickListener) {
            view.lblPeriod.text = product.costsFrequency.toLowerCase(Locale.ROOT)
            view.lblPrice.text = "${product.cost.currency} ${product.cost.value}"
            view.lblDescription.text = product.description
            view.lblHeader.text = product.name
            view.imgTick.visibility = if (selectedProducts.contains(product)) View.VISIBLE else View.GONE

            view.container.setOnClickListener { onClickListener.onClick(view) }
            var requestOptions = RequestOptions()
            view.imgProduct.clipToOutline = true
            view.container.clipToOutline = true
            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(24))
            Glide.with(view.context)
                .load(product.imageUrl.replace("{BB_GATEWAY_URL}", imageBaseUrl))
                .placeholder(R.drawable.placeholder)
                .apply(requestOptions)
                .into(view.imgProduct)
        }
    }

}

