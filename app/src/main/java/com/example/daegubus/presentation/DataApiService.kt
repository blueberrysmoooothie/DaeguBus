package com.example.daegubus.presentation
import com.example.daegubus.presentation.models.StationInfo
import com.example.daegubus.presentation.models.BusInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DataApiService {
    @GET("/get_station_info/{searchText}")
    fun getStaionInfo(
        @Path("searchText") searchText : String
    ): Call<List<StationInfo>>

    @GET("/get_bus_info/{stid}")
    fun getBusInfo(
        @Path("stid") stid : String
    ): Call<List<BusInfo>>
}