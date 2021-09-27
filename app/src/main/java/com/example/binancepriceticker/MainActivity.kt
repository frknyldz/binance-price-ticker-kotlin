package com.example.binancepriceticker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


object Constant {
    var baseUrl: String = "https://api.binance.com"
}

object ApiClient {

    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        if (retrofit == null)
            retrofit =
                Retrofit.Builder().baseUrl(Constant.baseUrl).addConverterFactory(
                    GsonConverterFactory.create()
                ).build()

        return retrofit as Retrofit
    }
}

data class Symbol(
    @SerializedName("symbol")
    val symbol: String = "",
    @SerializedName("price")
    val price: String = ""
)

interface PriceTickerService {
    @GET("/api/v3/ticker/price")
    fun getSymbolPrice(@Query("symbol") symbol: String): Call<Symbol>
}

class MainActivity : AppCompatActivity() {

    private lateinit var priceTickerService: PriceTickerService
    private lateinit var symbolNameEditText: EditText
    private lateinit var priceTextView: TextView
    private lateinit var getPriceButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        priceTickerService = ApiClient.getClient().create(PriceTickerService::class.java)
        symbolNameEditText = findViewById(R.id.symbolNameEditText) as EditText
        priceTextView = findViewById(R.id.priceTextView) as TextView
        getPriceButton = findViewById(R.id.getPriceButton) as Button

        getPriceButton.setOnClickListener(View.OnClickListener {

            var symbol = priceTickerService.getSymbolPrice(symbolNameEditText.text.toString())

            symbol.enqueue(object : Callback<Symbol> {
                override fun onFailure(call: Call<Symbol>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_LONG)
                        .show()
                    priceTextView.setText("")
                }

                override fun onResponse(call: Call<Symbol>, response: Response<Symbol>) {
                    if (response.isSuccessful) {
                        var symbol: Symbol = (response.body() as Symbol?)!!
                        priceTextView.setText(symbol.price)
                    } else {
                        Toast.makeText(applicationContext, "Invalid symbol name", Toast.LENGTH_LONG)
                            .show()
                        priceTextView.setText("")
                    }

                }
            })
        })

    }

}