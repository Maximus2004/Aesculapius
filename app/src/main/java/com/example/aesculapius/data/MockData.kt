package com.example.aesculapius.data

import com.example.aesculapius.R
import java.time.LocalTime
import java.time.LocalDate

val medicinesActive = mutableListOf(
    Medicine(
        image = R.drawable.medicines_example,
        name = "Аескулапиус",
        dose = 1,
        time = LocalTime.MIDNIGHT,
        startDate = LocalDate.now(),
        endDate = LocalDate.MAX
    ),
    Medicine(
        image = R.drawable.medicines_example,
        name = "Глазовыколупывательница",
        dose = 12,
        time = LocalTime.MIDNIGHT,
        startDate = LocalDate.now(),
        endDate = LocalDate.MAX
    ),
    Medicine(
        image = R.drawable.medicines_example,
        name = "Фурацилин",
        dose = 1,
        time = LocalTime.MIDNIGHT,
        startDate = LocalDate.now(),
        endDate = LocalDate.MAX
    ),
    Medicine(
        image = R.drawable.medicines_example,
        name = "Нозанекс",
        dose = 1,
        time = LocalTime.MIDNIGHT,
        startDate = LocalDate.now(),
        endDate = LocalDate.MAX
    )
)

val medicinesEnded = mutableListOf(
    Medicine(
        image = R.drawable.medicines_example,
        name = "Фурацилин",
        dose = 1,
        time = LocalTime.MIDNIGHT,
        startDate = LocalDate.now(),
        endDate = LocalDate.MAX
    ),
    Medicine(
        image = R.drawable.medicines_example,
        name = "Нозанекс",
        dose = 1,
        time = LocalTime.MIDNIGHT,
        startDate = LocalDate.now(),
        endDate = LocalDate.MAX
    )
)

// один из вариантов организации хранения данных о приёме лекарств
val medicines = listOf(
    MutableList(31) { MonthStructure(active = mutableListOf(), ended = medicinesEnded) },
    MutableList(29) { MonthStructure(active = medicinesActive, ended = medicinesEnded) },
    MutableList(31) { MonthStructure(active = mutableListOf(), ended = medicinesEnded) },
    MutableList(30) { MonthStructure(active = medicinesActive, ended = medicinesEnded) },
    MutableList(31) { MonthStructure(active = mutableListOf(), ended = medicinesEnded) },
    MutableList(30) { MonthStructure(active = medicinesActive, ended = medicinesEnded) },
    MutableList(31) { MonthStructure(active = mutableListOf(), ended = medicinesEnded) },
    MutableList(31) { MonthStructure(active = medicinesActive, ended = medicinesEnded) },
    MutableList(30) { MonthStructure(active = mutableListOf(), ended = medicinesEnded) },
    MutableList(31) { MonthStructure(active = medicinesActive, ended = medicinesEnded) },
    MutableList(30) { MonthStructure(active = mutableListOf(), ended = medicinesEnded) },
    MutableList(31) { MonthStructure(active = medicinesActive, ended = mutableListOf()) }
)