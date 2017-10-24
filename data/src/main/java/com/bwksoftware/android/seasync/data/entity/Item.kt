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

package com.bwksoftware.android.seasync.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Item {
    @SerializedName("id")
    @Expose
    open var id: String? = null


    @SerializedName("modifier_name")
    @Expose
    open var modifierName: String? = null

    @SerializedName("modifier_email")
    @Expose
    open var modifierEmail: String? = null

    @SerializedName("permission")
    @Expose
    open var permission: String? = null

    @SerializedName("modifier_contact_email")
    @Expose
    open var modifierContactEmail: String? = null

    @SerializedName("type")
    @Expose
    open var type: String? = null

    @SerializedName("mtime")
    @Expose
    open var mtime: Long? = null

    @SerializedName("name")
    @Expose
    open var name: String? = null

    @SerializedName("size")
    @Expose
    open var size: Long = 0


    open var dbId: Long? = null
    open var path: String? = null
    open var parent: Item? = null
    open var synced: Boolean? = null
    open var storage: String? = null
}