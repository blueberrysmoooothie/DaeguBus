/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.daegubus.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.daegubus.R
import com.example.daegubus.presentation.theme.DaeguBusTheme
import com.example.daegubus.presentation.models.StationInfo
import com.example.daegubus.presentation.models.BusInfo
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.FileWriter
import java.io.PrintWriter
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.json.JSONException
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.nio.charset.Charset

class MainActivity : ComponentActivity() {
//    즐겨찾기 정보 json 파일
//    val jsonString = assets.open("data.json").reader().readText()
//    var jsonArray = JSONArray(jsonString)

    var myObj : JSONObject = JSONObject()
    var jsonArray : JSONArray = JSONArray()


    val retrofit =Retrofit.Builder()
        .baseUrl("https://port-0-bus-station-7xwyjq992lliyexgag.sel4.cloudtype.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val dataApiService = retrofit.create(DataApiService:: class.java)
    var stid:String = "000000"
    var myStation = false

    fun writeJson(test_view:TextView){
        val path = "data.json"
        val test_station = StationInfo("test","test","test")

        PrintWriter(FileWriter(path)).use {
            val gson = Gson()
            val jsonString = gson.toJson(test_station)
            it.write(jsonString)
            test_view.text = jsonString
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myObj.put("bsId", "7011012300")
        myObj.put("bsNm", "강산아파트건너")
        myObj.put("routeList", "")

        jsonArray.put(myObj)

        this.createSearchView()
    }

    fun createSearchView() {
        setContentView(R.layout.search)
//        val test_view = findViewById<TextView>(R.id.station_info1)
//        test_view.text = jsonString.toString()
//        this.writeJson(test_view)
        this.updateMyStations()
        val search_bar = findViewById<EditText>(R.id.search_input)
        val search_button: Button = findViewById<Button>(R.id.search_button)
        search_bar.setHint("Search")
        search_button.setOnClickListener {
            this.onSearchButtonClick()
        }
    }
    fun updateMyStations(){
        val my_stations_view = findViewById<LinearLayout>(R.id.my_stations_layout)
        var my_result :List<StationInfo> = List(jsonArray.length(), { i -> StationInfo("0","0","0")})
        for (index in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(index)

            my_result[index].bsId = jsonObject.getString("bsId")
            my_result[index].bsNm = jsonObject.getString("bsNm")
            my_result[index].routeList = jsonObject.getString("routeList")
        }
        addStation(my_result, my_stations_view)
    }
//
    fun checkMyStation(bsId : String){
        for (index in 0 until jsonArray.length()){
            if (jsonArray.getJSONObject(index).getString("bsId") == bsId) {
                myStation = true
                return
            }
        }
        myStation = false
    }
    fun addMyStation(bsId : String, bsNm : String, routeList : String){

        val jsonObj = JSONObject()
        jsonObj.put("bsId", bsId)
        jsonObj.put("bsNm", bsNm)
        jsonObj.put("routeList", routeList)

        jsonArray.put(jsonObj)
    }
//
    fun updateMyStationJson(array : JSONArray){
//        val path = "/src/assets/data.json"
//        PrintWriter(FileWriter(path)).use {
//            val gson = Gson()
//            val jsonString = gson.toJson(array)
//            it.write(jsonString)
//        }
//        jsonString = assets.open("data.json").reader().readText()
//        jsonArray = JSONArray(jsonString)

        for (index in 0 until jsonArray.length()){
            jsonArray.remove(0)
        }
        for (index in 0 until array.length()){
            jsonArray.put(array[index])
        }
    }
    fun onMyStationClick(bsId : String, bsNm : String, routeList : String){

        if (this.myStation){
            val newJsonArray = JSONArray()
            for (index in 0 until jsonArray.length()){
                if (jsonArray.getJSONObject(index).getString("bsId") != bsId){
                    newJsonArray.put(jsonArray.getJSONObject(index))

                }
            }
            this.updateMyStationJson(newJsonArray)
            this.myStation = false
        }
        else{
            this.addMyStation(bsId, bsNm, routeList)
            this.myStation = true
        }
    }

    fun fill_star(starButton: ImageButton){
        if (this.myStation){
//            별 노란색으로 채우기
            starButton.setImageResource(R.drawable.star_fill)
        }else{
//            별 색 지우기
            starButton.setImageResource(R.drawable.star_blank)
        }
    }

    fun onStaionViewClick(bsId: String, bsNm: String, routeList : String) {
        stid = bsId
        setContentView(R.layout.station_info)

//        bus info
        val station_text = findViewById<TextView>(R.id.station_text)
        val back_button = findViewById<Button>(R.id.back)
        val updateButton = findViewById<Button>(R.id.updateButton)
        val starButton = findViewById<ImageButton>(R.id.starButton)

        this.checkMyStation(bsId)
        this.fill_star(starButton)
        starButton.setOnClickListener{
            this.onMyStationClick(bsId, bsNm, routeList)
            this.onStaionViewClick(bsId, bsNm, routeList)
        }

        updateButton.setOnClickListener {
            this.updateBusInfo()
        }
        back_button.setOnClickListener {
            this.createSearchView()
        }
        station_text.text = bsNm
        this.updateBusInfo()
    }

    fun addBus(result: List<BusInfo>) {
        val busResultView = findViewById<LinearLayout>(R.id.busResultView)
        busResultView.removeAllViews()
        for (bus in result) {
            val ll = LinearLayout(this)
            ll.orientation = LinearLayout.HORIZONTAL

            val bus_view = TextView(this)
            bus_view.text = bus.routeNo
            bus_view.width = 90
            bus_view.textSize = 16f

            val arr_view = TextView(this)
            arr_view.text = bus.arrState[0]
            arr_view.textSize = 16f

            ll.addView(bus_view)
            ll.addView(arr_view)
            busResultView.addView(ll)
        }

    }

    fun updateBusInfo() {
        dataApiService.getBusInfo(stid)?.enqueue(object : Callback<List<BusInfo>> {
            override fun onResponse(
                call: Call<List<BusInfo>>,
                response: Response<List<BusInfo>>
            ) {
                if (response.isSuccessful) {
                    // 정상적으로 통신이 성공된 경우
                    val result: List<BusInfo>? = response.body()
                    if (result != null) {
                        addBus(result)
                    }

                } else {
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                }
            }

            override fun onFailure(call: Call<List<BusInfo>>, t: Throwable) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
            }
        })
    }

    fun addStation(result: List<StationInfo>, target : LinearLayout) {
        target.removeAllViews()
        for (station in result) {
            val station_view = TextView(this)
            station_view.text = station.bsNm
            station_view.textSize = 16f
            station.bsId

            station_view.setOnClickListener {
                this.onStaionViewClick( station.bsId, station.bsNm, station.routeList)
            }
            target.addView(station_view)
        }

    }

    fun onSearchButtonClick(){
        val search_bar = findViewById<EditText>(R.id.search_input)
        val searchText : String = search_bar.text.toString()
        val result_view = findViewById<LinearLayout>(R.id.resultView)
        dataApiService.getStaionInfo(searchText)?.enqueue( object  : Callback<List<StationInfo>>{
            override fun onResponse(call: Call<List<StationInfo>>, response: Response<List<StationInfo>>) {
                if(response.isSuccessful){
                    // 정상적으로 통신이 성공된 경우
                    val result: List<StationInfo>? = response.body()

                    if (result != null) {
                        addStation(result, result_view)
                    }
//                        test_view.text =  "성공"
                }else{
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                }
            }
            override fun onFailure(call: Call<List<StationInfo>>, t: Throwable) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
            }
        })
    }

}



@Composable
fun WearApp(greetingName: String) {
    DaeguBusTheme {
        /* If you have enough items in your list, use [ScalingLazyColumn] which is an optimized
         * version of LazyColumn for wear devices with some added features. For more information,
         * see d.android.com/wear/compose.
         */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center
        ) {
            Greeting(greetingName = greetingName)
        }
    }
}

@Composable
fun Greeting(greetingName: String) {
    var name : String = "hansu"
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,

        text = stringResource(R.string.hello_world,name)
//        text = stringResource(R.string.hello_world, greetingName)
    )
}
//
@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
    val jsonString = ComponentActivity().assets.open("data.json").reader().readText()
//    val jsonArray = JSONTokener(jsonString).nextValue() as JSONArray
}