package com.example.binancepriceticker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.binancepriceticker.databinding.FragmentSearchBinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI
import javax.net.ssl.SSLSocketFactory

class SearchFragment : Fragment() {

    val TAG = "SearchFragment"

    private lateinit var webSocketClient: WebSocketClient
    private lateinit var symbolName: String

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.buttonSearch.setOnClickListener {

            symbolName = binding.editTextSymbolName.text.toString().lowercase()

            webSocketClient.close()

            initWebSocket()

        }

        return view
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initWebSocket() {
        val binanceUri: URI? = URI(MainActivity.binanceUrl)

        createWebSocketClient(binanceUri)

        val socketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory

        webSocketClient.setSocketFactory(socketFactory)
        webSocketClient.connect()
    }

    private fun createWebSocketClient(binanceUri: URI?) {
        webSocketClient = object : WebSocketClient(binanceUri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d(TAG, "onOpen")
                subscribe()
            }

            override fun onMessage(message: String?) {
                Log.d(TAG, "onMessage msg : $message")
                setUpPriceText(message)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d(TAG, "onClose")
            }

            override fun onError(ex: Exception?) {
                Log.e(TAG, "onError: ${ex?.message}")
            }

        }
    }


    private fun setUpPriceText(message: String?) {

        message?.let {
            val moshi = Moshi.Builder().build()
            val adapter: JsonAdapter<SymbolPriceTicker> =
                moshi.adapter(SymbolPriceTicker::class.java)
            val symbol = adapter.fromJson(message)
            if (symbol?.s.equals(symbolName.uppercase()))
                activity?.runOnUiThread { binding.editTextSymbolVal.setText(symbol?.c) }
        }

    }

    private fun subscribe() {

        webSocketClient.send(
            "{\n" +
                    "    \"method\": \"SUBSCRIBE\",\n" +
                    "    \"params\": [\"${symbolName}@ticker\"],\n" +
                    "    \"id\": 1\n" +
                    "}"
        )
    }

    override fun onResume() {
        super.onResume()
        initWebSocket()
    }

    override fun onPause() {
        super.onPause()
        webSocketClient.close()
    }
}