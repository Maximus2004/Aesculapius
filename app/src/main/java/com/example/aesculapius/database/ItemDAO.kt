package com.example.aesculapius.database

import androidx.room.Dao
import androidx.room.Query
import com.example.aesculapius.ui.therapy.MedicineItem
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

// data access object
@Dao
interface ItemDAO {
    @Query("INSERT INTO medicines_items VALUES(NULL, :image, :name, :undername, :dose, :frequency, :startDate, :endDate)")
    suspend fun insertMedicineItem(
        image: Int,
        name: String,
        undername: String,
        dose: String,
        frequency: String,
        startDate: LocalDate,
        endDate: LocalDate
    )

    @Query("SELECT * from medicines_items WHERE (startDate <= :currentDate) AND (endDate > :currentDate)")
    suspend fun getMedicinesOnCurrentDate(currentDate: LocalDate): List<MedicineItem>
}