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

package com.bwksoftware.android.seafile.prefs

import android.content.Context
import com.bwksoftware.android.seafile.BuildConfig
import javax.inject.Inject


class SharedPrefsController @Inject
constructor(private val context: Context) {

    enum class Preference(internal var defaultValue: String) {
        CURRENT_USER_TOKEN(""),
        CURRENT_USER_ACCOUNT("None"),
        DISABLE_CONTROLS_FOR_BEGINNER("true")
    }

    fun getPreference(preference: Preference): String {
        val sharedPref = context.getSharedPreferences(APP_IDENTIFIER, Context.MODE_PRIVATE)
        return sharedPref.getString(preference.toString(), preference.defaultValue)
    }

    fun preferenceExists(preference: Preference): Boolean {
        val sharedPref = context.getSharedPreferences(APP_IDENTIFIER, Context.MODE_PRIVATE)
        return sharedPref.contains(preference.toString())
    }

    fun setPreference(preference: Preference, value: String) {
        val sharedPref = context.getSharedPreferences(APP_IDENTIFIER, Context.MODE_PRIVATE)
        sharedPref.edit().putString(preference.toString(), value).apply()
    }

    companion object {
        private val APP_IDENTIFIER = BuildConfig.APPLICATION_ID
    }

}