package com.example.daegubus.presentation.models

import com.google.gson.annotations.SerializedName

data class StationInfo(
//    "bsBit":"bit",
//    "bsId":"7011012300",
//    "bsNm":"\uac15\uc0b0\uc544\ud30c\ud2b8\uac74\ub108",
//    "ngisXPos":167611.42471892,
//    "ngisYPos":366479.33588006,
//    "routeList":"719, \ub3d9\uad6c2, \ub3d9\uad6c8, \ud314\uacf51",
//    "style":"bs",
//    "wincId":"20649
    @SerializedName("bsId") val bsId: String,
    @SerializedName("bsNm") val bsNm: String,
    @SerializedName("routeList") val routeList: String,
    )
data class BusInfo(

//    "arrState":"\uc804","bsGap":2,
//    "bsNm":"\uc544\uc591\uad50\uc5ed(2\ubc88\ucd9c\uad6c)",
//    "busAreaCd":"DG",
//    "busTCd2":"D",
//    "busTCd3":"N",
//    "crfId":1645355858,
//    "moveDir":"1",
//    "prevBsGap":1,
//    "routeId":"4050008000",
//    "routeNo":"\ub3d9\uad6c8",
//    "routeNote":"\uc5f0\uacbd\uc9c0\uad6c\ubc29\uba74","vhcNo2":"2352"},

    @SerializedName("routeNo") val routeNo: String,
    @SerializedName("arrState") val arrState: List<String>,
)