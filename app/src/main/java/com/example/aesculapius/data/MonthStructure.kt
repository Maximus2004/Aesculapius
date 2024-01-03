package com.example.aesculapius.data

import com.example.aesculapius.ui.therapy.Medicine

// хранит информацию о принимаемых лекрствах в каждый конкретный день
data class MonthStructure(
    val active: MutableList<Medicine>,
    val ended: MutableList<Medicine>
)
