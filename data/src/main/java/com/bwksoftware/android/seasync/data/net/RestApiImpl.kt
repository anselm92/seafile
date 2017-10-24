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

package com.bwksoftware.android.seasync.data.net

import android.content.Context
import android.net.Uri
import com.bwksoftware.android.seasync.data.entity.Account
import com.bwksoftware.android.seasync.data.entity.Avatar
import com.bwksoftware.android.seasync.data.entity.Item
import com.bwksoftware.android.seasync.data.entity.Repo
import com.google.gson.Gson
import io.reactivex.Observable
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by ansel on 10/10/2017.
 */
@Singleton
class RestApiImpl @Inject constructor(val context: Context) {
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
        return service.getRepoList("Token " + authToken)
    }

    fun getRepoListSync(authToken: String): Call<List<Repo>> {
        return service.getRepoListSync("Token " + authToken)
    }

    fun getAvatar(username: String, authToken: String): Observable<Avatar> {
        return service.getAvatar(username, "Token " + authToken)
    }

    fun getDirectoryEntries(authToken: String, repoID: String,
                            directory: String): Observable<List<Item>> {
        if (directory.isEmpty())
            return service.getDirectoryEntries(repoID, "Token " + authToken)
        return service.getDirectoryEntries(repoID, "Token " + authToken, directory)
    }

    fun getDirectoryEntriesSync(authToken: String, repoID: String,
                                directory: String): Call<List<Item>> {
        if (directory.isEmpty())
            return service.getDirectoryEntriesSync(repoID, "Token " + authToken)
        return service.getDirectoryEntriesSync(repoID, "Token " + authToken, directory)
    }

    fun getUpdateLink(authToken: String, repoID: String, directory: String): Call<String> {
        return service.getUpdateLink(repoID, authToken, directory)
    }

    fun getUploadLink(authToken: String, repoID: String, directory: String): Call<String> {
        return service.getUploadLink(repoID, authToken, directory)
    }

    fun uploadFile(url: String, authToken: String, parentDir: String, relativeDir: String,
                   file: File): Call<String> {
        val requestFile = RequestBody.create(
                MediaType.parse(context.contentResolver.getType(Uri.fromFile(file))),
                file
        )
        // MultipartBody.Part is used to send also the actual file name
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        return service.uploadFile(url, authToken, parentDir, relativeDir, body)
    }

    fun updateFile(url: String, authToken: String,
                   targetFile: File): Call<String> {
        val requestFile = RequestBody.create(
                MediaType.parse(context.contentResolver.getType(Uri.fromFile(targetFile))),
                targetFile
        )
        // MultipartBody.Part is used to send also the actual file name
        val body = MultipartBody.Part.createFormData("target_file", targetFile.name, requestFile)
        return service.updateFile(url, authToken, body)
    }

    fun getFileDownloadLink(authToken: String, directory: String): Call<String> {
        return service.getFileDownloadLink(authToken, directory)
    }

    fun downloadFile(url: String): Call<ResponseBody> {
        return service.downloadFile(url)
    }
}