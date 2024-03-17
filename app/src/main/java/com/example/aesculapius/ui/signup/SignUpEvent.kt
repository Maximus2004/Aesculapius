package com.example.aesculapius.ui.signup

import java.time.LocalDate
import java.time.LocalDateTime

sealed interface SignUpEvent {
    data class OnNameChanged(val name: String): SignUpEvent
    data class OnSurnameChanged(val surname: String): SignUpEvent
    data class OnPatronymicChanged(val patronymic: String): SignUpEvent
    data class OnHeightChanged(val height: String): SignUpEvent
    data class OnWeightChanged(val weight: String): SignUpEvent
    data class OnBirthdayChanged(val birthday: LocalDate): SignUpEvent
    data class OnEveningReminderChanged(val eveningReminder: LocalDateTime): SignUpEvent
    data class OnMorningReminderChanged(val morningReminder: LocalDateTime): SignUpEvent
}