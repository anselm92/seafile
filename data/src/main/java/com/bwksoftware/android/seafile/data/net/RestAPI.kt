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
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface RestAPI {

    companion object {
        var API_BASE_URL = "https://cloud.bwk-technik.de/api2/"
    }

    @GET("repos/")
    fun getRepoList(@Header("Authorization") auth:String): Observable<List<Repo>>


    @POST("auth-token/")
    fun postAccountToken(@Body credentials : RequestBody): Observable<Account>
}
