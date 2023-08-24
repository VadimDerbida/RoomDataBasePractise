package com.example.fragment_homework.core.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize

data class User(
    var email: String,
    val name: String,
    val password: String
) : Parcelable
