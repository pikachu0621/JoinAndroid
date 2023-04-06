package com.mayunfeng.join.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.IntRange
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.mayunfeng.join.Application
import com.mayunfeng.join.R
import com.mayunfeng.join.TOKEN_ERROR_KEY
import com.mayunfeng.join.base.AppBaseActivity.Companion.activityStack
import com.mayunfeng.join.base.BaseService
import com.mayunfeng.join.bean.UserSignTable
import com.mayunfeng.join.ui.activity.AdminUserStartActivity
import com.mayunfeng.join.ui.activity.LoginActivity
import com.mayunfeng.join.ui.activity.MainActivity
import com.mayunfeng.join.ui.fragment.MyStartSignUserFragment
import com.mayunfeng.join.utils.UserUtils
import com.pikachu.utils.utils.BackgroundUtils
import com.pikachu.utils.utils.GlideUtils
import com.pikachu.utils.utils.LogsUtils
import okhttp3.*
import java.io.Serializable
import java.util.concurrent.TimeUnit


enum class WebSocketType(
    val type: Int,
    val msg: String
) {
    WE_OK(2000, "链接成功"),
    WE_MESSAGE(2001, "有消息"),
    WE_CLOSED(2002, "断开连接"),
    WE_OTHER_DEVICES(2003, "其它设备登录"),
    WE_PWS_NUL(2004, "密码被修改"),
    WE_MESSAGE_GOTO_SIGN(2005, "有新签到任务"),
}
const val CLOSE_ALL_NOTIFY_KEY = "closeAllNotify"

class WebSocketService : BaseService<Serializable>() {

    private var webSocket: WebSocket? = null
    private var isClosed: Boolean = true
    private var reconnectionNum: Int = 10  // 重连次数
    private var reconnectionNumAdd: Int = 0


    override fun onCreate() {
        super.onCreate()
        webSocketService = this
        setForegroundService(
            this,
            1,
            MainActivity::class.java,
            R.mipmap.ic_launcher,
            getString(R.string.app_name),
            getString(R.string.app_name) + "运行行中",
            "签到在线中...",
            "点击进入软件"
        );
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startWebSocket()
        return super.onStartCommand(intent, flags, startId)
    }


    fun startWebSocketNul() {
        reconnectionNumAdd = 0
        startWebSocket()
    }

    fun startWebSocket() {
        if (!isClosed || reconnectionNumAdd >= reconnectionNum) return
        LogsUtils.showLog("------------开始连接------------")

        val client = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)               //允许失败重试
            .readTimeout(5, TimeUnit.SECONDS)     //设置读取超时时间
            .writeTimeout(5, TimeUnit.SECONDS)    //设置写的超时时间
            .connectTimeout(20, TimeUnit.SECONDS) //设置连接超时时间
            .build()
        val request = Request.Builder()
            .url(Application.getWsUrl("/ws"))
            .addHeader(TOKEN_ERROR_KEY, UserUtils.readLoginToken() ?: "")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            //连接成功
            override fun onOpen(webSocket: WebSocket, response: Response) {
                isClosed = false
                LogsUtils.showLog("WebSocket 链接成功!")
                postEventBus(null, WebSocketType.WE_OK.type)
            }

            //接收服务器消息 text
            override fun onMessage(webSocket: WebSocket, text: String) {
                isClosed = false
                val fromJson: UserSignTable? = try {
                    Gson().fromJson(text, UserSignTable::class.java)
                } catch (_ : Exception){
                    null
                }
                if (fromJson != null){
                    GlideUtils.with(this@WebSocketService)
                        .loadHeaderToken(fromJson.startSignInfo.userTable.userImg).intoBitmap {
                            showMsgNotify(
                                Application.myApplicationContext,
                                "你有新签到任务",
                                "${fromJson.startSignInfo.signTitle}，请在${MyStartSignUserFragment.formatTime(fromJson.startSignInfo.signTime)}内签到",
                                it,
                                Intent(this@WebSocketService, AdminUserStartActivity::class.java)
                            )
                        }
                    postEventBus(fromJson, WebSocketType.WE_MESSAGE_GOTO_SIGN.type)
                    return
                }
                postEventBus(text, WebSocketType.WE_MESSAGE.type)
            }

            //连接失败调用 异常信息t.getMessage()
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                LogsUtils.showLog("WebSocket 链接失败--${t.message}")
                isClosed = true
                reconnectionNumAdd++
                startWebSocket()
                // postEventBus(null, WebSocketType.WE_CLOSED.type)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                isClosed = true
                if (code == 1001) {
                    postEventBus(null, WebSocketType.WE_OTHER_DEVICES.type)
                    showUserOut("你的账号已在别处登录", "如非本人操作，请尽快修改密码！")
                    return
                }
                if (code == 1002) {
                    postEventBus(null, WebSocketType.WE_PWS_NUL.type)
                    showUserOut("你的密码已过期", "密码已过期，你被迫下线！")
                    return
                }
                postEventBus(null, WebSocketType.WE_CLOSED.type)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                isClosed = true
                if (code == 1001) {
                    postEventBus(null, WebSocketType.WE_OTHER_DEVICES.type)
                    showUserOut("你的账号已在别处登录", "如非本人操作，请尽快修改密码！")
                    return
                }
                if (code == 1002) {
                    postEventBus(null, WebSocketType.WE_PWS_NUL.type)
                    showUserOut("你的密码已过期", "密码已过期，你被迫下线！")
                    return
                }
                postEventBus(null, WebSocketType.WE_CLOSED.type)
            }
        })

    }


    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }

    fun sendMsg(msg: String) {
        webSocket?.send(msg)
    }

    fun cancel() {
        webSocket?.cancel()
        isClosed = true
        reconnectionNumAdd = 0
        stopService(Intent(this, javaClass::class.java))
    }

    companion object {

        private var webSocketService: WebSocketService? = null
        private var notifyId: Int = 2
        private var notifyIdList: ArrayList<Int> = arrayListOf()


        fun getWebSocketService(): WebSocketService? = webSocketService


        fun isServiceRunning(mContext: Context, className: Class<out Service>): Boolean {
            var isRunning = false
            val activityManager: ActivityManager =
                mContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val serviceList: List<ActivityManager.RunningServiceInfo> =
                activityManager.getRunningServices(30)
            if (serviceList.isEmpty()) {
                return false
            }
            for (i in serviceList.indices) {
                if (serviceList[i].service.className.contains(className.name) === true) {
                    isRunning = true
                    break
                }
            }
            return isRunning
        }



        fun showUserOut(title: String, msg: String){
            if (BackgroundUtils.getRunningTask(
                    Application.myApplicationContext,
                    Application.myApplicationContext.packageName)
            ) return
            UserUtils.loginTokenOut(activityStack.lastElement())
            showMsgNotify(
                Application.myApplicationContext,
                title,
                msg,
                null,
                Intent(Application.myApplicationContext, LoginActivity::class.java)
            )
        }



        /**
         * 通过通知启动服务
         */
        fun setForegroundService(
            service: Service,
            id: Int,
            cls: Class<out Activity>,
            @IntRange appIcon: Int,
            appName: String?,
            title: String?,
            subtitle: String?,
            msg: String?
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val contentIntent = PendingIntent.getActivity(
                    service, 0, Intent(service, cls), 0
                )
                val importance = NotificationManager.IMPORTANCE_LOW
                val channel = NotificationChannel("CHANNEL_ID$id", appName, importance)
                channel.description = title
                val builder = NotificationCompat.Builder(
                    service,
                    "CHANNEL_ID$id"
                )
                builder.setSmallIcon(appIcon)
                    .setContentTitle(subtitle)
                    .setContentText(msg)
                    .setContentIntent(contentIntent) //触摸跳转
                    .setAutoCancel(true)
                    .setOngoing(true)
                val notificationManager =
                    service.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
                service.startForeground(id, builder.build())
            }
        }


        fun closeAllNotify(context: Context){
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notifyIdList.forEach { notificationManager.cancel(it) }
            notifyIdList.clear()
        }


        /**
         * 消息通知
         */
        fun showMsgNotify(
            context: Context,
            title: String,
            msg: String,
            image: Bitmap? = null,
            intent: Intent,
        ) {
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    context.applicationInfo.name,
                    "消息通知",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                manager.createNotificationChannel(channel)
                channel.enableLights(true)
                channel.setShowBadge(true);
            }
            val contentIntent = PendingIntent.getActivity(
                context, 0, intent, 0
            )
            val notification = NotificationCompat.Builder(context, context.applicationInfo.name)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH) //设置该通知优先级
                .setCategory(Notification.CATEGORY_MESSAGE) //设置通知类别
                .setContentIntent(contentIntent) //触摸跳转
            if (image != null) notification.setLargeIcon(image)
            val build = notification.build()
            val notifyIdListNum = notifyId++
            notifyIdList.add(notifyIdListNum)
            manager.notify(notifyIdListNum, build)
        }
    }
}