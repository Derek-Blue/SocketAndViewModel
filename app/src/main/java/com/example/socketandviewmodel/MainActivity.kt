package com.example.socketandviewmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.GsonBuilder
import org.jetbrains.anko.alert

class MainActivity : AppCompatActivity(), WebSocketConnection.ServerListener {

    private val guid = "0000" //個人需求
    private val auth = "0000" //個人需求
    private val socket_url = "wss://www.cmoney.tw:8088/" //個人需求

    private lateinit var webSocketConnection : WebSocketConnection

    private lateinit var viewModel: ShareViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webSocketConnection = WebSocketConnection(socket_url, guid, auth)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ShareViewModel::class.java)
        viewModel.valueChange(mutableListOf())

        viewModel.getChangedValue().observe(viewLifecycleOwner, Observer { stocks ->
            val stockNoList = showStocks.map { stock -> stock.values.toList()[0] }
            changedStocks = stocks.filter { stockNoList.contains(it) }.toMutableList()
            if (changedStocks.isNotEmpty()) {
                changedStocks.forEach {
                    rvStock.adapter?.notifyItemChanged(stockNoList.indexOf(it))
                }
            }
        })
    }

    override fun onNewMessage(message: String) {
        //此fun依個人需求
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        val socketResponse = gson.fromJson(message, SocketResponse::class.java)
        when (socketResponse.Action) {
            "CheckAuthResult" -> {
                socketResponse.Data.HasAuth?.let {
                    if (it) webSocketConnection.sendSocketMessage("SubChannel InstantGroup_11")
                    else alert(socketResponse.Error!!.toString(),"Error") {
                        positiveButton("OK"){ }
                    }.show()
                }
            }
            "SubChannelResult" -> {
                socketResponse.Data.IsSuccess?.let {
                    if (it) return
                    else alert(socketResponse.Error!!.toString(),"Error") {
                        positiveButton("OK"){ }
                    }.show()
                }
            }
            "UpdateInstant_11" -> {
//                val socketAdditionalInfoMap = bindingData(socketResponse.Data.Columns!!, socketResponse.Data.Rows!!)
//                socketAdditionalInfoMap.forEach { stocksMap[it.key] = it.value }
//                viewModel.valueChange(socketAdditionalInfoMap.keys.toMutableList())
            }
        }
    }

    override fun onStatusChange(status: WebSocketConnection.ConnectionStatus) {
        val statusMsg =
            if (status == WebSocketConnection.ConnectionStatus.DISCONNECTED)webSocketConnection.permission
            else ""
        webSocketConnection.sendSocketMessage(statusMsg)
    }

    override fun onResume() {
        super.onResume()
        webSocketConnection.connect(this)
    }

    override fun onPause() {
        super.onPause()
        webSocketConnection.disconnect()
    }
}
