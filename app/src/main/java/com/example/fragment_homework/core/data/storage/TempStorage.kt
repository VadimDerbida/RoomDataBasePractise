package com.example.fragment_homework.core.data.storage

import android.content.Context
import android.content.SharedPreferences

const val USER_LOGGED_IN = "userLoggedIn"
const val USER_NAME = "userName"
const val USER_EMAIL = "userEmail"
const val USER_PASSWORD = "userPassword"

class TempStorage(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("storage", Context.MODE_PRIVATE)

    var isLoggedIn: Boolean
        get() = sharedPreferences.getBoolean(USER_LOGGED_IN, false)
        set(value) {
            sharedPreferences.edit().putBoolean(USER_LOGGED_IN, value).apply()
        }

    var createdUserName: String?
        get() = sharedPreferences.getString(USER_NAME, "")
        set(value) {
            sharedPreferences.edit().putString(USER_NAME, value).apply()
        }

    var createdUserEmail: String?
    get() = sharedPreferences.getString(USER_EMAIL, "")
    set(value) {
        sharedPreferences.edit().putString(USER_EMAIL, value).apply()
    }
    var createdUserPassword: String?
        get() = sharedPreferences.getString(USER_PASSWORD, "")
        set(value) {
            sharedPreferences.edit().putString(USER_PASSWORD, value).apply()
        }
}