# Binance Price Ticker

Android application implemented with Kotlin to fetch real-time crypto currency price from Binance websocket api and presents them on UI.

More details of Binance API can be reachable from [the link](https://github.com/binance/binance-spot-api-docs/blob/master/web-socket-streams.md)

Compiled sdk version is 30.

Dependencies

    // WebSocket  
	implementation "org.java-websocket:Java-WebSocket:1.5.1"  
  
	// Moshi - To parse json response to model
	implementation 'com.squareup.moshi:moshi:1.12.0'  
	kapt 'com.squareup.moshi:moshi-kotlin-codegen:1.12.0'

![Home](/screenshots/img1.png)      ![Search](/screenshots/img2.png) 

Not focused on UI design, aim of the project is usage of websocket stream api and fun.