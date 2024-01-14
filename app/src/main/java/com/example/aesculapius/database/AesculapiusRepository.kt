package com.example.aesculapius.database

import com.example.aesculapius.ui.therapy.MedicineItem
import java.time.LocalDate
import javax.inject.Inject

class AesculapiusRepository @Inject constructor(val itemDAO: ItemDAO) {
    suspend fun insertMedicineItem(
        image: Int,
        name: String,
        undername: String,
        dose: String,
        frequency: String,
        startDate: LocalDate,
        endDate: LocalDate
    ) {
        itemDAO.insertMedicineItem(image, name, undername, dose, frequency, startDate, endDate)
    }

    suspend fun getMedicinesOnCurrentDate(currentDate: LocalDate): List<MedicineItem> {
        return itemDAO.getMedicinesOnCurrentDate(currentDate)
    }

    suspend fun getAmountActiveMedicines(currentDate: LocalDate): Int {
        return itemDAO.getMedicinesOnCurrentDate(currentDate).size
    }
}