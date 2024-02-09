package com.example.aesculapius.database

import com.example.aesculapius.ui.tests.MetricsItem
import com.example.aesculapius.ui.tests.ScoreItem
import com.example.aesculapius.ui.therapy.MedicineItem
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/** [AesculapiusRepository] репозиторий для Room */
@Singleton
class AesculapiusRepository @Inject constructor(private val itemDAO: ItemDAO) {
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

    suspend fun getAllMedicines(): List<MedicineItem> {
        return itemDAO.getAllMedicines()
    }

    suspend fun getMedicinesOnCurrentDate(currentDate: LocalDate): List<MedicineItem> {
        return itemDAO.getMedicinesOnCurrentDate(currentDate)
    }

    suspend fun getAmountActiveMedicines(currentDate: LocalDate): Int {
        return itemDAO.getMedicinesOnCurrentDate(currentDate).size
    }

    suspend fun insertAstTestScore(date: LocalDate, score: Int) {
        itemDAO.insertASTTestScore(date, score)
    }

    fun getAllASTResultsInRange(): Flow<List<ScoreItem>> {
        return itemDAO.getAllAstResultsInRange(startDate = LocalDate.now().minusYears(1), endDate = LocalDate.now())
    }

    suspend fun getAllASTResults(): List<ScoreItem> {
        return itemDAO.getAllAstResults()
    }

    suspend fun getAllMetrics(): List<MetricsItem> {
        return itemDAO.getAllMetrics()
    }

    suspend fun getAllMetricsInRange(startDate: LocalDate, endDate: LocalDate): List<MetricsItem> {
        return itemDAO.getAllMetricsInRange(startDate, endDate)
    }

    suspend fun deleteAllMetrics() {
        itemDAO.deleteAllMetrics()
    }

    suspend fun insertMetrics(metrics: Float, date: LocalDate) {
        return itemDAO.insertMetrics(metrics, date)
    }

    suspend fun updateMetrics(metrics: Float, date: LocalDate) {
        return itemDAO.updateMetrics(metrics, date)
    }

    suspend fun getAllMetricsWithDate(date: LocalDate): List<MetricsItem> {
        return itemDAO.getAllMetricsWithDate(date)
    }
}