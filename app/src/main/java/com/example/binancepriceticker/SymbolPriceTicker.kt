package com.example.binancepriceticker

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SymbolPriceTicker(val s:String?, val c: String?)
