package com.tokopedia.filter.view.utils

import android.content.Context
import com.tokopedia.filter.R
import com.tokopedia.filter.view.model.ProductX
import com.tokopedia.filter.view.model.Shop
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

/**
 * Created on : 01/05/21 | 21.22
 * Author     : dededarirahmadi
 * Name       : dededarirahmadi
 * Email      : dededarirahmadi@gmail.com
 */
object FromJson {
    fun getData(context: Context) : List<ProductX>{

        val productList = ArrayList<ProductX>()
        val dataProducts = mutableListOf<ProductX>()
        try {

            val obj = JSONObject(loadJSONFromRawFolder(context)!!)

            val data: JSONObject = obj.getJSONObject("data")

            val productsArray: JSONArray = data.getJSONArray("products")

            for (i in 0 until productsArray.length()) {

                val productDetail = productsArray.getJSONObject(i)
                val shop = productDetail.getJSONObject("shop")
                val p = ProductX(
                        discountPercentage = productDetail.getInt("discountPercentage"),
                        id = productDetail.getInt("id"),
                        imageUrl = productDetail.getString("imageUrl"),
                        name = productDetail.getString("name"),
                        priceInt = productDetail.getInt("priceInt"),
                        slashedPriceInt = productDetail.getInt("slashedPriceInt"),
                        shop = Shop(
                                city = shop.getString("city"),
                                id = shop.getInt("id"),
                                name = shop.getString("name")
                        )
                )
                productList.add(p)
            }
            dataProducts.addAll(productList)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return dataProducts
    }

    private fun loadJSONFromRawFolder(context: Context): String? {
        var json: String? = null
        json = try {
            val `is` = context.resources.openRawResource(R.raw.products);
            val size = `is`.available()
            val buffer = ByteArray(size)
            val charset: Charset = Charsets.UTF_8
            `is`.read(buffer)
            `is`.close()
            String(buffer, charset)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}