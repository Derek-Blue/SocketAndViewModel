package com.example.socketandviewmodel


import okhttp3.*
import java.util.concurrent.TimeUnit
import android.os.Handler


class WebSocketConnection (val serverUrl: String, val guid: String, val auth: String) {

    enum class ConnectionStatus{
        DISCONNECTED, CONNECTED
    }

    interface ServerListener{
        fun onNewMessage(message: String)
        fun onStatusChange(status: ConnectionStatus)
    }

    private val client = OkHttpClient.Builder()
        .readTimeout(3, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()
    private lateinit var webSocket: WebSocket
    private lateinit var messageHandler: Handler
    private lateinit var statusHandler: Handler
    private lateinit var serverListener: ServerListener
    val permission: String get() = "CheckAuth 19,$guid,$auth" //個人需求

    fun connect(serverListener: ServerListener){
        val request = Request.Builder().url(serverUrl).build()
        webSocket = client.newWebSocket(request, StockSocketListener())
        this.serverListener = serverListener
        messageHandler = Handler{
            this.serverListener.onNewMessage(it.obj as String)
            return@Handler true
        }
        statusHandler = Handler{
            this.serverListener.onStatusChange(it.obj as ConnectionStatus)
            return@Handler true
        }
    }

    fun disconnect() {
        webSocket.cancel()
        messageHandler.removeCallbacksAndMessages(null)
        statusHandler.removeCallbacksAndMessages(null)
    }

    fun sendSocketMessage(message: String){
        webSocket.send(message)
    }

    inner class StockSocketListener() : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            val message = statusHandler.obtainMessage(0, ConnectionStatus.CONNECTED)
            statusHandler.sendMessage(message)
            webSocket.send(permission)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val message = messageHandler.obtainMessage(0, text)
            messageHandler.sendMessage(message)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            val message = statusHandler.obtainMessage(0, ConnectionStatus.DISCONNECTED)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            disconnect()
        }
    }

}