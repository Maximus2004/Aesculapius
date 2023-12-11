package com.example.aesculapius.data

import com.example.aesculapius.R
import com.example.aesculapius.ui.home.NavigationItemContent
import com.example.aesculapius.ui.home.PageType
import com.example.aesculapius.ui.statistics.GraphicTypeContent
import com.example.aesculapius.ui.statistics.GraphicTypes
import com.example.aesculapius.ui.tests.Question
import com.example.aesculapius.ui.tests.Test
import java.time.LocalDate

data class OnboardingItem(
    val title: String,
    val description: String
)

enum class Hours {
    Morning,
    Evening
}

enum class TestType {
    AST,
    Recommendations,
    Metrics
}

val navigationItemContentList = listOf(
    NavigationItemContent(
        pageType = PageType.Statistics,
        icon = R.drawable.statistics_icon,
        pageName = "Статистика",
        isHelpButton = true
    ),
    NavigationItemContent(
        pageType = PageType.Therapy,
        icon = R.drawable.therapy_icon,
        pageName = "Базисная терапия",
        isHelpButton = false
    ),
    NavigationItemContent(
        pageType = PageType.Tests,
        icon = R.drawable.tests_icon,
        pageName = "Отчётность",
        isHelpButton = true
    ),
    NavigationItemContent(
        pageType = PageType.Profile,
        icon = R.drawable.profile_icon,
        pageName = "Профиль и прочее",
        isHelpButton = false
    )
)

val graphicsNavigationItemContentList = listOf(
    GraphicTypeContent(graphicTypes = GraphicTypes.Week, nameOfType = "неделя"),
    GraphicTypeContent(graphicTypes = GraphicTypes.Month, nameOfType = "месяц"),
    GraphicTypeContent(graphicTypes = GraphicTypes.ThreeMonths, nameOfType = "3 месяца"),
    GraphicTypeContent(graphicTypes = GraphicTypes.HalfYear, nameOfType = "полгода"),
    GraphicTypeContent(graphicTypes = GraphicTypes.Year, nameOfType = "год"),
)

val astTest = Test(
    listOfQuestion = mutableListOf(
        Question(
            questionText = "Как часто за последние 4 недели астма мешала Вам выполнять обычный объем работы в учебном заведении, на работе или дома?",
            answersList = mutableListOf("Все время", "Очень часто", "Иногда", "Редко", "Никогда")
        ),
        Question(
            questionText = "Как часто за последние 4 недели Вы отмечали у себя затрудненное дыхание?",
            answersList = mutableListOf("Чаще, чем раз в неделю", "1 раз в неделю", "От 3 до 6 раз в неделю", "1 или два раза в неделю", "Ни разу")
        ),
        Question(
            questionText = "Как часто за последние 4 недели Вы просыпались ночью или раньше, чем обычно, из-за симптомов астмы (кашля, свистящего или затрудненного дыхания, чувства стеснения или боли в груди)?",
            answersList = mutableListOf("4 ночи в неделю или чаще", "2 - 3 ночи в неделю", "1 раз в неделю", "1 или 2 раза", "Ни разу")
        ),
        Question(
            questionText = "Как часто за последнюю неделю Вы использовали быстродействующий ингалятор (например, Сальбутамол, Вентолин, Беродуал, Атровент) или небулайзер (аэрозольный аппарат) с лекарством (например, Беротек, Беродуал, Вентолин небулы)?",
            answersList = mutableListOf("3 раза в день или чаще", "1 или 2 раза в день", "2 или 3 раза в неделю", "1 раз в неделю или реже ", "Ни разу")
        ),
        Question(
            questionText = "Как бы Вы оценили, насколько Вам удавалось контролировать астму за последнюю неделю?",
            answersList = mutableListOf("Совсем не удавалось контролировать", "Плохо удавалось контролировать", "В некоторой степени удавалось контролировать", "Хорошо удавалось контролировать", "Полностью удавалось контролировать")
        )
    ),
    targetTime = LocalDate.now(),
    badResult = "Вам не удавалось контролировать астму. Ваш врач может посоветовать Вам, какие меры нужно применять, чтобы добиться улучшения контроля над Вашим заболеванием.",
    mediumResult = "За последние 4 недели Вы хорошо контролировали астму, но не полностью. Ваш врач поможет Вам добиться полного контроля.",
    goodResult = "Вы полностью контролировали астму за последние 4 недели. У Вас не было симптомов астмы и связанных с ней ограничений. Проконсультируйтесь с врачом, если ситуация изменится."
)

val recTest = Test(
    listOfQuestion = mutableListOf(
        Question(
            questionText = "Забываете ли Вы делать ингаляции?",
            answersList = mutableListOf("Никогда", "Редко", "Иногда", "Часто", "Всегда")
        ),
        Question(
            questionText = "Пропускали ли Вы ингаляции за последние 2 недели?",
            answersList = mutableListOf("Никогда", "Редко", "Иногда", "Часто", "Всегда")
        ),
        Question(
            questionText = "Бывает ли так, что Вы прекращаете ингаляции, если чувствуешь себя хорошо (БЕЗ консультации с врачом)?",
            answersList = mutableListOf("Никогда", "Редко", "Иногда", "Часто", "Всегда")
        ),
        Question(
            questionText = "Бывает ли так, что Вы не хотите делать ингаляции каждый день?",
            answersList = mutableListOf("Никогда", "Редко", "Иногда", "Часто", "Всегда")
        ),
        Question(
            questionText = "Вы хорошо знаете, как правильно пользоваться Вашим ингалятором (техника ингаляции)?",
            answersList = mutableListOf("Да", "Нет")
        ),
        Question(
            questionText = "Возникают ли у Вас сложности с использованием ингалятора?",
            answersList = mutableListOf("Никогда", "Редко", "Иногда", "Часто", "Всегда")
        ),
        Question(
            questionText = "Вам удобно пользоваться ингалятором?",
            answersList = mutableListOf("Да", "Нет")
        ),
        Question(
            questionText = "Как Вы считаете, помогает ли Вам лечение?",
            answersList = mutableListOf("Да", "Нет")
        ),
        Question(
            questionText = "Меняете ли Вы препарат или количество доз самостоятельно (или с родителями) БЕЗ консультации с врачом?",
            answersList = mutableListOf("Да", "Нет")
        )
    ),
    targetTime = LocalDate.now(),
    badResult = "Результатов пока нет",
    mediumResult = "Результатов пока нет",
    goodResult = "Результатов пока нет"
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
