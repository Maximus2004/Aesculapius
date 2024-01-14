package com.example.aesculapius.database

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Converters {

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDate? {
        return if (value == null) null else LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE)
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): String? {
        return date?.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }
}