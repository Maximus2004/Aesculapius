package com.example.aesculapius.database

import androidx.room.Dao
import androidx.room.Query
import com.example.aesculapius.ui.tests.MetricsItem
import com.example.aesculapius.ui.tests.ScoreItem
import com.example.aesculapius.ui.therapy.MedicineItem
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

// data access object
@Dao
interface ItemDAO {
    @Query("INSERT INTO medicines_items VALUES(NULL, :image, :medicineType, :name, :undername, :dose, :frequency, :startDate, :endDate, :isAccepted, :isSkipped, :realStartDate)")
    suspend fun insertMedicineItem(
        image: Int,
        medicineType: String,
        name: String,
        undername: String,
        dose: String,
        frequency: String,
        startDate: LocalDate,
        endDate: LocalDate,
        isAccepted: MutableList<Int>,
        isSkipped: MutableList<Int>,
        realStartDate: LocalDate,
    )

    @Query("UPDATE medicines_items SET isAccepted = isAccepted || ',' || :zero, isSkipped = isSkipped || ',' || :zero WHERE (isAccepted IS NOT NULL) AND (isSkipped IS NOT NULL)")
    suspend fun updateAllMedicines(zero: Int)

    @Query("SELECT * from medicines_items WHERE id = :medicineId")
    suspend fun getMedicineWithId(medicineId: Int): MedicineItem

    @Query("UPDATE medicines_items SET isAccepted = :isAccepted WHERE id = :medicineId")
    suspend fun acceptMedicine(medicineId: Int, isAccepted: MutableList<Int>)

    @Query("UPDATE medicines_items SET isSkipped = :isSkipped WHERE id = :medicineId")
    suspend fun skipMedicine(medicineId: Int, isSkipped: MutableList<Int>)

    @Query("UPDATE medicines_items SET frequency = :frequency, dose = :dose WHERE id = :medicineId")
    suspend fun updateMedicineItem(medicineId: Int, frequency: String, dose: String)

    @Query("DELETE from medicines_items WHERE id = :medicineId")
    suspend fun deleteMedicineItem(medicineId: Int)

    @Query("SELECT * from medicines_items WHERE (startDate <= :currentDate) AND (endDate > :currentDate)")
    suspend fun getMedicinesOnCurrentDate(currentDate: LocalDate): List<MedicineItem>

    @Query("SELECT * from medicines_items")
    suspend fun getAllMedicines(): List<MedicineItem>

    @Query("INSERT INTO score_items VALUES(NULL, :score, :date)")
    suspend fun insertASTTestScore(date: LocalDate, score: Int)

    @Query("SELECT * from score_items WHERE (date <= :endDate) AND (date >= :startDate)")
    fun getAllAstResultsInRange(startDate: LocalDate, endDate: LocalDate): Flow<List<ScoreItem>>

    @Query("SELECT * from score_items")
    suspend fun getAllAstResults(): List<ScoreItem>

    @Query("INSERT INTO metrics_items VALUES(NULL, ROUND(:metrics, 1), :date)")
    suspend fun insertMetrics(metrics: Float, date: LocalDate)

    @Query("UPDATE metrics_items SET metrics = ROUND((metrics + :metrics) / 6, 1) WHERE date = :date")
    suspend fun updateMetrics(metrics: Float, date: LocalDate)

    @Query("SELECT * from metrics_items WHERE (date <= :endDate) AND (date >= :startDate)")
    suspend fun getAllMetricsInRange(startDate: LocalDate, endDate: LocalDate): List<MetricsItem>

    @Query("SELECT * from metrics_items")
    suspend fun getAllMetrics(): List<MetricsItem>

    @Query("SELECT * from metrics_items WHERE date = :date")
    suspend fun getAllMetricsWithDate(date: LocalDate): List<MetricsItem>

    @Query("DELETE from metrics_items")
    suspend fun deleteAllMetrics()
}