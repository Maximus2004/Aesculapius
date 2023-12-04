package com.example.aesculapius.data

// хранит информацию о принимаемых лекрствах в каждый конкретный день
data class MonthStructure(
    val active: MutableList<Medicine>,
    val ended: MutableList<Medicine>
)
