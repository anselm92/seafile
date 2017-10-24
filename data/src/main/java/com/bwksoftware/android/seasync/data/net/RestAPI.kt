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

import com.bwksoftware.android.seasync.data.entity.Account
import com.bwksoftware.android.seasync.data.entity.Avatar
import com.bwksoftware.android.seasync.data.entity.Item
import com.bwksoftware.android.seasync.data.entity.Repo
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RestAPI {

    companion object {
        var API_BASE_URL = "https://cloud.bwk-technik.de/api2/"
    }

    @GET("repos/")
    fun getRepoList(@Header("Authorization") auth: String): Observable<List<Repo>>

    @GET("repos/")
    fun getRepoListSync(@Header("Authorization") auth: String): Call<List<Repo>>

    @GET("repos/{repo-id}/dir/")
    fun getDirectoryEntries(@Path(value = "repo-id") repo_id: String,
                            @Header("Authorization") auth: String): Observable<List<Item>>

    @GET("repos/{repo-id}/dir/")
    fun getDirectoryEntriesSync(@Path(value = "repo-id") repo_id: String,
                                @Header("Authorization") auth: String): Call<List<Item>>

    @GET("repos/{repo-id}/dir/")
    fun getDirectoryEntries(@Path(value = "repo-id") repo_id: String,
                            @Header("Authorization") auth: String,
                            @Query("p") directory: String): Observable<List<Item>>

    @GET("repos/{repo-id}/dir/")
    fun getDirectoryEntriesSync(@Path(value = "repo-id") repo_id: String,
                                @Header("Authorization") auth: String,
                                @Query("p") directory: String): Call<List<Item>>


    @POST("auth-token/")
    fun postAccountToken(@Body credentials: RequestBody): Observable<Account>

    @GET("avatars/user/{username}/resized/128/")
    fun getAvatar(@Path(value = "username", encoded = true) username: String,
                  @Header("Authorization") auth: String): Observable<Avatar>

    @GET("repos/{repo-id}/update-link/")
    fun getUpdateLink(@Path(value = "repo-id") repo_id: String,
                      @Header("Authorization") auth: String,
                      @Query("p") directory: String): Call<String>

    @GET("repos/{repo-id}/upload-link/")
    fun getUploadLink(@Path(value = "repo-id") repo_id: String,
                      @Header("Authorization") auth: String,
                      @Query("p") directory: String): Call<String>

    @Multipart
    @POST
    fun uploadFile(@Url url: String,
                   @Header("Authorization") auth: String,
                   @Query("parent_dir") parentDir: String,
                   @Query("relative_dir") relativeDir: String,
                   @Part file: MultipartBody.Part
    ): Call<String>

    @Multipart
    @POST
    fun updateFile(@Url url: String,
                   @Header("Authorization") auth: String,
                   @Part targetFile: MultipartBody.Part
    ): Call<String>

    @GET("repos/{repo-id}/file/")
    fun getFileDownloadLink(@Header("Authorization") auth: String,
                            @Query("p") directory: String): Call<String>

    @GET
    @Streaming
    fun downloadFile(@Url url: String): Call<ResponseBody>
}
