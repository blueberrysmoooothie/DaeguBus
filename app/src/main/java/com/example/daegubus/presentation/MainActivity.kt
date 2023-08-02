/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.daegubus.presentation

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ScrollView
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
import androidx.recyclerview.widget.RecyclerView
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.daegubus.R
import com.example.daegubus.presentation.theme.DaeguBusTheme
import org.w3c.dom.Text
import com.example.daegubus.presentation.DataApiService
import com.example.daegubus.presentation.models.StationInfo
import com.example.daegubus.presentation.models.BusInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    val retrofit =Retrofit.Builder()
        .baseUrl("https://port-0-bus-station-7xwyjq992lliyexgag.sel4.cloudtype.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val dataApiService = retrofit.create(DataApiService:: class.java)
    var stid:String = "000000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.init()
    }

    fun init() {
        setContentView(R.layout.search)
        val search_bar = findViewById<EditText>(R.id.search_input)
        var search_button: Button = findViewById<Button>(R.id.search_button)
        search_bar.setHint("Search")
        search_button.setOnClickListener {
            this.onSeachButtonClick()
        }
    }

    fun onStaionViewClick(bsNm: String, bsId: String) {
        stid = bsId
        setContentView(R.layout.activity_main)

//            bus info
        val station_text = findViewById<TextView>(R.id.station_text)
        val back_button = findViewById<Button>(R.id.back)
        val updateButton = findViewById<Button>(R.id.updateButton)

        updateButton.setOnClickListener {
            this.updateBusInfo()
        }
        back_button.setOnClickListener {
            this.init()
        }
        station_text.text = bsNm
        this.updateBusInfo()
    }

    fun addBus(result: List<BusInfo>) {
        var busResultView = findViewById<LinearLayout>(R.id.busResultView)
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
                    var result: List<BusInfo>? = response.body()
                    if (result != null) {
                        addBus(result)
                    }
                    if (result != null) {

                    }

//                        test_view.text =  "성공"
                } else {
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                }
            }

            override fun onFailure(call: Call<List<BusInfo>>, t: Throwable) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
            }
        })
    }

    fun addStation(result: List<StationInfo>) {
        var result_view = findViewById<LinearLayout>(R.id.resultView)
        result_view.removeAllViews()
        for (station in result) {
            val station_view = TextView(this)
            station_view.text = station.bsNm
            station_view.textSize = 16f
            station.bsId

            station_view.setOnClickListener {
                this.onStaionViewClick(station.bsNm, station.bsId)
            }
            result_view.addView(station_view)
        }

    }

    fun onSeachButtonClick(){
        val search_bar = findViewById<EditText>(R.id.search_input)
        val searchText : String = search_bar.text.toString()
        dataApiService.getStaionInfo(searchText)?.enqueue( object  : Callback<List<StationInfo>>{
            override fun onResponse(call: Call<List<StationInfo>>, response: Response<List<StationInfo>>) {
                if(response.isSuccessful){
                    // 정상적으로 통신이 성공된 경우
                    var result: List<StationInfo>? = response.body()

                    if (result != null) {
                        addStation(result)
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
//    WearApp("Preview Android")
}