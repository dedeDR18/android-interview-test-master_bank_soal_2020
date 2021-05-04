package com.tokopedia.maps

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tokopedia.maps.model.RapidApiResponseItem
import com.tokopedia.maps.network.ApiConfig
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.adapter.rxjava2.HttpException
import java.text.DecimalFormat
import java.text.NumberFormat

open class MapsActivity : AppCompatActivity() {

    private var mapFragment: SupportMapFragment? = null
    private var googleMap: GoogleMap? = null

    private lateinit var textCountryName: TextView
    private lateinit var textCountryCapital: TextView
    private lateinit var textCountryPopulation: TextView
    private lateinit var textCountryCallCode: TextView
    private lateinit var progressBar: ProgressBar

    private var editText: EditText? = null
    private var buttonSubmit: View? = null

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        bindViews()
        initListeners()
        loadMap()
    }

    private fun bindViews() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        editText = findViewById(R.id.editText)
        buttonSubmit = findViewById(R.id.buttonSubmit)
        textCountryName = findViewById(R.id.txtCountryName)
        textCountryCapital = findViewById(R.id.txtCountryCapital)
        textCountryPopulation = findViewById(R.id.txtCountryPopulation)
        textCountryCallCode = findViewById(R.id.txtCountryCallCode)
        progressBar = findViewById((R.id.progressBar))
    }

    private fun initListeners() {
        buttonSubmit!!.setOnClickListener {

            val countrySearch = editText!!.text.toString()

            if(countrySearch.isNotEmpty() || countrySearch.isNotBlank()){
                showLoading(true)

                //dapat dipindahkan ke ViewModel
                val client = ApiConfig.provideApiService().getCountryData(countrySearch)
                val disposables1 = client.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ response ->
                            if (response.size > 1) {
                                for (item in response) {
                                    if (item.name.toLowerCase().equals(countrySearch.toLowerCase())) {
                                        showMarker(item.latlng[0], item.latlng[1])
                                        showCountryData(item)
                                        showLoading(false)
                                    }
                                }
                            } else {
                                showMarker(response[0].latlng[0], response[0].latlng[1])
                                showCountryData(response[0])
                                progressBar.visibility = View.GONE
                            }
                        }, { error ->
                            showLoading(false)
                            val error = error as HttpException
                            val errorBody = error.response()!!.errorBody()!!.string()
                            Log.d("TEST", "error= $errorBody")
                            Toast.makeText(this, getString(R.string.data_not_found), Toast.LENGTH_SHORT).show()
                        })
                disposables.add(disposables1)
            }else {
                Toast.makeText(this, getString(R.string.toast_input_country_name), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(state : Boolean) = if (state) progressBar.visibility = View.VISIBLE else progressBar.visibility = View.GONE

    private fun loadMap() {
        mapFragment!!.getMapAsync { googleMap -> this@MapsActivity.googleMap = googleMap }
    }

    private fun showCountryData(detail: RapidApiResponseItem) {
        textCountryName.text = getString(R.string.country_name).plus(" ").plus(detail.nativeName)
        textCountryCapital.text = getString(R.string.country_capital).plus(" ").plus(detail.capital)
        textCountryPopulation.text = getString(R.string.country_population).plus(" ").plus(formatNumberPopulation(detail.population))
        textCountryCallCode.text = getString(R.string.country_phondcode).plus(" ").plus(detail.callingCodes[0])
    }

    private fun showMarker(lat: Double, lng: Double){
        if (lat != null && lng != null){
            val latLng = LatLng(lat, lng)
            mapFragment!!.getMapAsync {
                googleMap?.addMarker(MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)))
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
            }
        }
    }

    private fun formatNumberPopulation(population: Int) : String{
        val formatter: NumberFormat = DecimalFormat("#,###")
        return formatter.format(population).toString()
    }

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

}
