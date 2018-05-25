package jmm.jmmworkplan.net

import jmm.LoggingInterceptor
import jmm.jmmworkplan.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * user:Administrator
 * time:2018 04 09 11:13
 * package_name:jmm.baselibrary.data.net
 */
class RetrofitFactory private constructor() {
    companion object {
        val instance: RetrofitFactory by lazy { RetrofitFactory() }
    }

    private val retorfit: Retrofit
    private val interceptor: Interceptor

    init {
         interceptor = Interceptor {
            chain ->
            val request = chain.request()
                    .newBuilder()
                    .addHeader("Content-Type","application/json")
                    .addHeader("charset","utf-8")
                    .build()
            chain.proceed(request)
        }

        retorfit = Retrofit.Builder()
                .baseUrl("http://51jmm.tpddns.cn:10000/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(initClent())
                .build()
    }

    private fun initClent(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(ReadCookiesInterceptor())
                .addInterceptor(SaveCookiesInterceptor())
                .addInterceptor(addLogInterceptor())
                .connectTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .build()
    }

    private fun addLogInterceptor(): Interceptor {
        val loggingInterceptor = LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .request()
                .requestTag("Request")
                .response()
                .responseTag("Response")
                .addHeader("version", BuildConfig.VERSION_NAME)
                .build()
        return loggingInterceptor
    }

    fun <T>create(service:Class<T>):T {
        return retorfit.create(service)
    }
}