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

import android.util.Base64
import android.util.Base64.NO_WRAP
import android.util.Base64.NO_WRAP
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import okhttp3.HttpUrl




/**
 * Created by ansel on 10/10/2017.
 */
class ApiRequestInterceptor(private val httpUsername: String, private val httpPassword: String) : Interceptor {

    private val authorizationValue: String
        get() {
            val userAndPassword = httpUsername + ":" + httpPassword
            return "Basic " + Base64.encodeToString(userAndPassword.toByteArray(), Base64.NO_WRAP)
        }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {


        var request = chain.request()
        val url = request.url()
                .newBuilder()
//                .addQueryParameter("username", httpUsername)
//                .addQueryParameter("password", httpPassword)
                .build()

        request = request
                .newBuilder()
                .addHeader("Authorization", authorizationValue)
                .url(url)
                .build()

        return chain.proceed(request)
    }

}