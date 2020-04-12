package com.example.socketandviewmodel

class SocketResponse(
    val Action: String,
    val Data: Data,
    val Error: Error?)

class Data(
    val HasAuth: Boolean?,
    val ChannelName: String?,
    val IsSuccess: Boolean?,
    val Columns: Array<String>?,
    val Rows: Array<Array<String>>?
)