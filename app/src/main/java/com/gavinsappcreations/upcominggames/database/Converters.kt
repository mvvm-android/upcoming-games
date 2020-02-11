package com.gavinsappcreations.upcominggames.database

import androidx.room.TypeConverter


class Converters {

    @TypeConverter
    fun listToString(list: List<String>): String {
        val stringBuilder = StringBuilder()
        for (item in list) {
            stringBuilder.append(item).append(",")
        }
        return stringBuilder.toString()
    }

    @TypeConverter
    fun stringToList(string: String): List<String> {
        return string.split(",")
    }
}