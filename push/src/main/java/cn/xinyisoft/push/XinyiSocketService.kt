package cn.xinyisoft.push

import android.app.Service
import android.content.Intent
import android.os.IBinder

class XinyiSocketService : Service() {

    private var socketConnect: XinyiSocketConnect? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        socketConnect = XinyiSocketConnect(this@XinyiSocketService)
        socketConnect?.connect()
    }

    override fun onDestroy() {
        super.onDestroy()
        XinyiSocketService.socketService = null
        socketConnect?.disConnect()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    companion object {
        var socketService: Intent? = null
    }
}
