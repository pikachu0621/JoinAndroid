package com.mayunfeng.join.utils.retrofit

import io.reactivex.rxjava3.core.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.Executors

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.utils
 * @Author:         pkpk.run
 * @Description:    null
 */
class RetrofitManager private constructor(){



    private var retrofit: Retrofit? = null


    companion object{
        private var baseUrl: String? = null
        private var retrofitManager: RetrofitManager? = null

        fun init(baseUrl: String) {
            RetrofitManager.baseUrl = baseUrl
        }

        @Synchronized
        fun getInstance(): RetrofitManager {
            retrofitManager ?: synchronized(RetrofitManager::class.java){
                retrofitManager = RetrofitManager()
            }
            return retrofitManager!!
        }
    }

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(Objects.requireNonNull(baseUrl, "baseUrl == null"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .callbackExecutor(Executors.newSingleThreadExecutor())
            .build()
    }


    fun <T> create(service: Class<T>): T = retrofit!!.create(service)

}