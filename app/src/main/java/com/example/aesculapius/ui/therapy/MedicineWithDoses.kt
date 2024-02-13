package com.example.aesculapius.ui.therapy

import androidx.room.Embedded
import androidx.room.Relation

// для связи препаратов и доз
data class MedicineWithDoses(
    @Embedded val medicine: MedicineItem,
    @Relation(
        parentColumn = "idMedicine",
        entityColumn = "medicineId"
    )
    val doses: List<DoseItem>
)
