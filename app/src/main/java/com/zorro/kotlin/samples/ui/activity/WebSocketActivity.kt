/*
 *
 *  *    Copyright (C) 2016 Amit Shekhar
 *  *    Copyright (C) 2011 Android Open Source Project
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */
package com.zorro.kotlin.samples.ui.activity

import com.zorro.kotlin.samples.R
import com.zorro.kotlin.samples.ui.base.ZorroBaseActivity
import kotlinx.android.synthetic.main.activity_web_socket.*
import kotlinx.android.synthetic.main.include_toolbar_layout.*
import okhttp3.*
import okio.ByteString
import okio.ByteString.Companion.decodeHex
import java.util.concurrent.TimeUnit

/**
 * Created by Zorro on 09/12/16.
 *
 *
 * WebSocket
 */
class WebSocketActivity : ZorroBaseActivity() {
    private var webSocket: WebSocket? = null
    override fun attachLayoutRes(): Int = R.layout.activity_web_socket

    override fun initView() {

        setBlackStatusBar(toolbar)
    }

    override fun bindListener() {
        iv_left.setOnClickListener {
            finish()
        }
    }

    override fun start() {
        connectWebSocket()
    }


    override fun onStop() {
        super.onStop()
        disconnectWebSocket()
    }

    private fun connectWebSocket() {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .pingInterval(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
        val request = Request.Builder()
            .url("ws://echo.websocket.org")
            .build()
        webSocket = client.newWebSocket(request, getWebSocketListener())
    }

    private fun disconnectWebSocket() {
        if (webSocket != null) {
            webSocket!!.cancel()
        }
    }

    private fun getWebSocketListener(): WebSocketListener {
        return object : WebSocketListener() {
            override fun onOpen(
                webSocket: WebSocket,
                response: Response
            ) {
                webSocket.send("Hello...")
                webSocket.send("...World!")
                webSocket.send("deadbeef".decodeHex())
                webSocket.close(1000, "Goodbye, World!")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                runOnUiThread {
                    textView!!.append("\n")
                    textView!!.append("MESSAGE: $text")
                }
            }

            override fun onMessage(
                webSocket: WebSocket,
                bytes: ByteString
            ) {
                runOnUiThread {
                    textView!!.append("\n")
                    textView!!.append("MESSAGE: " + bytes.hex())
                }
            }

            override fun onClosing(
                webSocket: WebSocket,
                code: Int,
                reason: String
            ) {
                webSocket.close(1000, null)
                runOnUiThread {
                    textView!!.append("\n")
                    textView!!.append("CLOSE: $code $reason")
                }
            }

            override fun onFailure(
                webSocket: WebSocket,
                t: Throwable,
                response: Response?
            ) {
                t.printStackTrace()
            }
        }
    }

    companion object {
        private val TAG = WebSocketActivity::class.java.simpleName
    }
}