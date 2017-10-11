/*
 *    Copyright 2018 BWK Technik GbR
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.bwksoftware.android.seafile.data.net

import com.bwksoftware.android.seafile.data.entity.Account
import com.bwksoftware.android.seafile.data.entity.Repo
import com.google.gson.Gson
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by ansel on 10/10/2017.
 */
@Singleton
class RestApiImpl @Inject constructor() {
    private val service: RestAPI

    init {
        val logging = HttpLoggingInterceptor()
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val builder = OkHttpClient.Builder()
                .addInterceptor(logging)
        val retro = Retrofit.Builder().baseUrl(RestAPI.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build()).build()
        service = retro.create(RestAPI::class.java)
    }

    fun getAccountToken(username: String, password: String): Observable<Account> {
        val requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),
                "username=$username&password=$password")
        return service.postAccountToken(requestBody)
    }

    fun getRepoList(authToken: String): Observable<List<Repo>> {
        return service.getRepoList(authToken)
    }
}