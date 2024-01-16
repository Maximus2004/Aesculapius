package com.example.aesculapius.ui.tests

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "metrics_items")
data class MetricsItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val metrics: Float,
    val date: LocalDate
)
