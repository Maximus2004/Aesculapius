package com.example.aesculapius.database

import android.util.Log
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
        medicineType: String,
        name: String,
        undername: String,
        dose: String,
        frequency: String,
        startDate: LocalDate,
        endDate: LocalDate,
        realStartDate: LocalDate
    ) {
        itemDAO.insertMedicineItem(image, medicineType, name, undername, dose, frequency, startDate, endDate, mutableListOf(0), mutableListOf(0), realStartDate)
    }

    suspend fun updateAllMedicines() {
        itemDAO.updateAllMedicines(0)
    }

    suspend fun acceptMedicine(medicineId: Int, isMorningMedicine: Boolean) {
        val newIsAccepted = itemDAO.getMedicineWithId(medicineId).isAccepted
        newIsAccepted[newIsAccepted.lastIndex] = (newIsAccepted.last().or(if (isMorningMedicine) 2 else 1))
        Log.i("TAGTAG", newIsAccepted.toString())
        itemDAO.acceptMedicine(medicineId, newIsAccepted)
    }

    suspend fun skipMedicine(medicineId: Int, isMorningMedicine: Boolean) {
        val newIsSkipped = itemDAO.getMedicineWithId(medicineId).isSkipped
        newIsSkipped[newIsSkipped.lastIndex] = (newIsSkipped.last().or(if (isMorningMedicine) 2 else 1))
        Log.i("TAGTAG", newIsSkipped.toString())
        itemDAO.skipMedicine(medicineId, newIsSkipped)
    }

    suspend fun updateMedicineItem(medicineId: Int, frequency: String, dose: String) {
        itemDAO.updateMedicineItem(medicineId, frequency, dose)
    }

    suspend fun deleteMedicineItem(medicineId: Int) {
        itemDAO.deleteMedicineItem(medicineId)
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
        return itemDAO.getAllAstResultsInRange(
            startDate = LocalDate.now().minusYears(1),
            endDate = LocalDate.now()
        )
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