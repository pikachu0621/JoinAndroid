package com.pkpk.join.utils.retrofit

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.HashMap

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.pkpk.join.utils
 * @Author:         pkpk.run
 * @Description:    null
 */
class RetrofitManager private constructor() {


    private var retrofit: Retrofit? = null
    private var builder: Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(Objects.requireNonNull(baseUrl, "baseUrl == null"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .callbackExecutor(Executors.newSingleThreadExecutor())


    companion object {
        private var baseUrl: String? = null
        private var retrofitManager: RetrofitManager? = null

        fun init(baseUrl: String) {
            RetrofitManager.baseUrl = baseUrl
        }

        fun <T: Any> Observable<T>.subscribeMainThread(retrofitObserver: RetrofitObserver<T>) {
            this.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .retry(1)  //请求失败重连次数
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retrofitObserver)
        }

        @Synchronized
        fun getInstance(): RetrofitManager {
            retrofitManager ?: synchronized(RetrofitManager::class.java) {
                retrofitManager = RetrofitManager()
            }
            return retrofitManager!!
        }
    }

    fun setResponseInterceptor(responseInterceptor: (request: Request) -> Request) {
        builder.client(OkHttpClient.Builder().addInterceptor {
            it.proceed(responseInterceptor(it.request()))
        }.build())
        retrofit = builder.build()
    }

    fun addHeader(hashMap: HashMap<String, String> ) {
        setResponseInterceptor{
            it.newBuilder().apply {
                hashMap.forEach { map -> addHeader(map.key, map.value) }
            }.build()
        }
    }

    fun addHeader(k: String, v: String) {
        addHeader(hashMapOf<String, String>().apply {
            put(k, v)
        })
    }


    fun <T> create(service: Class<T>): T {
        if (retrofit == null) {
            retrofit = builder.build()
        }
        return retrofit!!.create(service)
    }




}