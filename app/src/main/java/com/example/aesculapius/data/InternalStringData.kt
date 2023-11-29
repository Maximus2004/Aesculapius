package com.example.aesculapius.data

import com.example.aesculapius.R
import com.example.aesculapius.ui.home.NavigationItemContent
import com.example.aesculapius.ui.home.PageType

data class OnboardingItem(
    val title: String,
    val description: String
)

enum class Hours {
    Morning,
    Evening
}

val navigationItemContentList = listOf(
    NavigationItemContent(pageType = PageType.Statistics, icon = R.drawable.statistics_icon),
    NavigationItemContent(pageType = PageType.Therapy, icon = R.drawable.therapy_icon),
    NavigationItemContent(pageType = PageType.Tests, icon = R.drawable.tests_icon),
    NavigationItemContent(pageType = PageType.Profile, icon = R.drawable.profile_icon)
)

val onboardingList = listOf(
    OnboardingItem(
        title = "«Aesculapius»",
        description = "Ваш надежный спутник для эффективного контроля астмы и поддержания здоровья легких."
    ),
    OnboardingItem(
        title = "Настройка напоминаний",
        description = "Создавайте напоминания для ввода метрик с пикфлоуметра. Утро, день, вечер - установите удобное время."
    ),
    OnboardingItem(
        title = "Ввод показаний",
        description = "Трижды в день вносите показания с вашего пикфлоуметра. Каждое измерение состоит из трех показаний."
    ),
    OnboardingItem(
        title = "Медицинское тестирование",
        description = "Проходите еженедельное тестирование в форме анкеты, которое поможет оценить ваше общее состояние. "
    ),
    OnboardingItem(
        title = "Анализ метрик",
        description = "Просматривайте свои метрики за выбранный период. Данные представлены в виде таблиц и графиков для удобного анализа."
    ),
    OnboardingItem(
        title = "Готовы начать?",
        description = "Контролируйте вашу астму и улучшайте здоровье вместе с «Aesculapius»"
    )
)

val days = mapOf(
    "янв" to 31,
    "фев" to 28,
    "мар" to 31,
    "апр" to 30,
    "май" to 31,
    "июн" to 30,
    "июл" to 31,
    "авг" to 31,
    "сен" to 30,
    "окт" to 31,
    "ноя" to 30,
    "дек" to 31
)

val months =
    listOf("янв", "фев", "мар", "апр", "май", "июн", "июл", "авг", "сен", "окт", "ноя", "дек")
