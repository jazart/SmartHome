package com.jazart.smarthome.TypeAdapters

import android.os.Build
import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue
import type.CustomType
import java.text.DateFormat
import java.time.LocalDateTime
import java.util.*

object Adapters {


//    val dateTypeAdapter = object: CustomTypeAdapter<LocalDateTime> {
//        override fun decode(value: CustomTypeValue<*>): LocalDateTime =
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                LocalDateTime.parse(value.value.toString())
//            } else {
//                DateFormat().parse(value.value.toString())
//            }
//
//        override fun encode(value: LocalDateTime): CustomTypeValue<*> {
//
//        }
//    }
}