package com.example.aesculapius.database

import com.example.aesculapius.ui.tests.MetricsItem
import com.example.aesculapius.ui.tests.ScoreItem
import com.example.aesculapius.ui.therapy.MedicineItem
import kotlinx.coroutines.flow.Flow
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

    suspend fun insertASTTestScore(date: LocalDate, score: Int) {
        itemDAO.insertASTTestScore(date, score)
    }

    fun getAllASTResults(): Flow<List<ScoreItem>> {
        return itemDAO.getAllASTResults()
    }

    suspend fun getAllMetrics(startDate: LocalDate, endDate: LocalDate): List<MetricsItem> {
        return itemDAO.getAllMetrics(startDate, endDate)
    }

    suspend fun insertMetrics(metrics: Float, date: LocalDate) {
        return itemDAO.insertMetrics(metrics, date)
    }
}