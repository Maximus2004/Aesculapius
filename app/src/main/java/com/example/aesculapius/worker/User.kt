package com.example.aesculapius.worker

import com.example.aesculapius.ui.tests.MetricsItem
import com.example.aesculapius.ui.tests.ScoreItem
import com.example.aesculapius.ui.therapy.MedicineItem
import java.time.LocalDate

data class User(
    val name: String,
    val surname: String,
    val patronymic: String,
    val height: Float,
    val weight: Float,
    val birthDate: String,
    val medicines: List<MedicineItem>,
    val metrics: List<MetricsItem>,
    val astTests: List<ScoreItem>
)
