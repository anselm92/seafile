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

package com.bwksoftware.android.seafile.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by ansel on 10/10/2017.
 */
class Repo {
    @SerializedName("name")
    @Expose
    open var name: String? = null

    @SerializedName("permission")
    @Expose
    open var permission: String? = null

    @SerializedName("encrypted")
    @Expose
    open var encrypted: Boolean? = null

    @SerializedName("owner")
    @Expose
    open var owner: String? = null

    @SerializedName("mtime")
    @Expose
    open var mtime: Long? = null

    @SerializedName("size")
    @Expose
    open var size: Long? = null

}