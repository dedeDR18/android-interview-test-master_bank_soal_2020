package com.tokopedia.maps.network

import com.tokopedia.maps.BuildConfig
import com.tokopedia.maps.model.RapidApiResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * Created on : 03/05/21 | 13.25
 * Author     : dededarirahmadi
 * Name       : dededarirahmadi
 * Email      : dededarirahmadi@gmail.com
 */
interface ApiService {
    @Headers(
            "x-rapidapi-key:8f96782605msh3aac38d5da71a56p1dbc9cjsn3767d5bcbe16",
            "x-rapidapi-host:restcountries-v1.p.rapidapi.com"
            )
    @GET("name/{countryName}")
    fun getCountryData(@Path("countryName") countryName: String) : Single<RapidApiResponse>
}