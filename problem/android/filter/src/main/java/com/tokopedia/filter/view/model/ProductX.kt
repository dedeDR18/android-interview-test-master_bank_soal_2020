package com.tokopedia.filter.view.model

data class ProductX(
    val discountPercentage: Int,
    val id: Int,
    val imageUrl: String,
    val name: String,
    val priceInt: Int,
    val shop: Shop,
    val slashedPriceInt: Int
)