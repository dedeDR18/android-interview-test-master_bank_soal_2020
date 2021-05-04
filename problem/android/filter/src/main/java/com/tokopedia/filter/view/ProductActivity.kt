package com.tokopedia.filter.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.Slider
import com.tokopedia.filter.R
import com.tokopedia.filter.view.adapter.ProductAdapter
import com.tokopedia.filter.view.model.ProductX
import com.tokopedia.filter.view.model.Shop
import com.tokopedia.filter.view.model.product
import com.tokopedia.filter.view.utils.FromJson
import kotlinx.android.synthetic.main.bottom_sheet_filter.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max


class ProductActivity : AppCompatActivity() {

    private var dataProducts = listOf<ProductX>()
    private val listCity = ArrayList<String>()
    private var cityOfProduct = listOf<Pair<String, Int>>()
    private var selectedCity = ArrayList<String>()
    private lateinit var adapterProduct: ProductAdapter

    private lateinit var rv: RecyclerView
    private lateinit var fab: FloatingActionButton
    private var chip1: Chip? = null
    private var chip2: Chip? = null
    private var chip3: Chip? = null
    private var chipGroup: ChipGroup? = null
    private var priceSlider: Slider? = null
    private var btnApplyFilter: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        rv = findViewById(R.id.product_list)
        fab = findViewById(R.id.fab)

        initRv()

        dataProducts = FromJson.getData(this)

        adapterProduct.setData(dataProducts)

        fab.setOnClickListener {
            showBottomSheetDialogFilter()
        }

    }

    private fun initRv() = rv.apply {
        adapterProduct = ProductAdapter()
        rv.layoutManager = GridLayoutManager(this@ProductActivity, 2)
        rv.setHasFixedSize(true)
        rv.adapter = adapterProduct
    }

    private fun showBottomSheetDialogFilter(){
        adapterProduct.setData(dataProducts)
        val btnsheet = layoutInflater.inflate(R.layout.bottom_sheet_filter, null)
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        dialog.setContentView(btnsheet)

        initBottomSheetDialogWidget(dialog)

        var maxPriceFromSlider = 0

        getCityOfProduct()

        chip1?.apply {
            val cityChip1 = cityOfProduct[0].first
            text = cityChip1
            setOnClickListener{
                showToast(cityChip1)
            }
        }

        chip2?.apply {
            val cityChip2 = cityOfProduct[1].first
            text = cityChip2
            setOnClickListener {
                showToast(cityChip2)
            }
        }

        chip3?.apply {
            if(cityOfProduct.size > 2){
                val city = showOtherCity()
                text = "Other"
                setOnClickListener {
                    showToast(city.joinToString())
                }
            } else {
                visibility = View.GONE
            }
        }



        priceSlider?.apply {
            addOnChangeListener { slider, value, fromUser ->
                maxPriceFromSlider = slider.value.toInt()
            }
            value = getMinProductPrice()
            valueFrom = getMinProductPrice()
            valueTo = getMaxProductPrice()
            stepSize = getStepSizeSliderPrice( getMinProductPrice().toInt(), getMaxProductPrice().toInt()

            )
        }

        btnApplyFilter?.setOnClickListener {

            setSelectedCityForFiltering(chipGroup)

            setFilter(selectedCity, getMinProductPrice().toInt(), maxPriceFromSlider)

            clearSelectedCityForFiltering()
        }

        btnsheet.setOnClickListener {

            clearSelectedCityForFiltering()

            dialog.dismiss()
        }
        dialog.show()
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    private fun setFilter(city : ArrayList<String>, minPrice : Int, maxPrice : Int) {
        val filteredProduct = if (city.isNotEmpty()){
            dataProducts.filter { product ->
                product.priceInt in minPrice until maxPrice
                        &&
                city.contains(product.shop.city)
            }
        } else {
            dataProducts.filter { product -> product.priceInt in minPrice until maxPrice }
        }
        adapterProduct.setData(filteredProduct)
    }

    private fun setSelectedCityForFiltering(chipGroup: ChipGroup?){
        when(chipGroup!!.checkedChipId){
            R.id.chip_1 -> {
                selectedCity.clear()
                selectedCity.add(cityOfProduct[0].first)
            }

            R.id.chip_2 -> {
                selectedCity.clear()
                selectedCity.add(cityOfProduct[1].first)
            }

            R.id.chip_3 -> {
                selectedCity.clear()
                selectedCity.addAll(showOtherCity())
            }
        }
    }

    private fun getCityOfProduct() {
        for (item in dataProducts){
            listCity.add(item.shop.city)
        }
        cityOfProduct = listCity.groupingBy { it }.eachCount()
                .toList()
                .sortedByDescending { it.second }
    }

    private fun showOtherCity() : List<String>{
        var otherCity = listCity.distinct()
        return otherCity.drop(2)

    }

    private fun getMinProductPrice() : Float = dataProducts.minBy { it.priceInt }?.priceInt?.toFloat()!!

    private fun getMaxProductPrice() : Float = dataProducts.maxBy { it.priceInt }?.priceInt?.toFloat()!!

    private fun getStepSizeSliderPrice(p1: Int, p2:Int) : Float{
        var p1 = p1
        var p2 = p2

        while (p1 != p2) {
            if (p1 > p2)
                p1 -= p2
            else
                p2 -= p1
        }
        return p1.toFloat()
    }

    private fun clearSelectedCityForFiltering(){
        selectedCity.clear()
    }

    private fun showToast(city: String){
        Toast.makeText(this@ProductActivity, "kota terpilih = $city", Toast.LENGTH_SHORT).show()
    }

    private fun initBottomSheetDialogWidget(dialog: BottomSheetDialog){
        chip1 = dialog.findViewById(R.id.chip_1)!!
        chip2 = dialog.findViewById(R.id.chip_2)
        chip3 = dialog.findViewById(R.id.chip_3)
        chipGroup =dialog.findViewById(R.id.chip_group)
        priceSlider = dialog.findViewById(R.id.slider_price)
        btnApplyFilter = dialog.findViewById(R.id.btn_apply_filter)
    }

}