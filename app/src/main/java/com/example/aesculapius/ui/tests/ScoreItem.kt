package com.example.aesculapius.ui.tests

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "score_items")
data class ScoreItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val score: Int,
    val date: LocalDate
)
