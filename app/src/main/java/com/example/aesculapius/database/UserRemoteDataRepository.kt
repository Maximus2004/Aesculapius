package com.example.aesculapius.database

import com.example.aesculapius.ui.signup.SignUpUiState
import com.example.aesculapius.worker.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

const val USERS_COLLECTION_REF = "users"

/** [UserRemoteDataRepository] репозиторий для Firestore Database */
@Singleton
class UserRemoteDataRepository @Inject constructor(private val aesculapiusRepository: AesculapiusRepository) {
    companion object {
        val usersRef: CollectionReference = Firebase.firestore.collection(USERS_COLLECTION_REF)
        fun getUserId() = usersRef.document().id
    }

    /**
     * [addUserAtFirst] добавление пользователя в Firestore Database впервые
     */
    fun addUserAtFirst(signUpUiState: SignUpUiState) {
        val user = User(
            name = signUpUiState.name,
            surname = signUpUiState.surname,
            patronymic = signUpUiState.patronymic,
            height = signUpUiState.height.toFloat(),
            weight = signUpUiState.weight.toFloat(),
            birthDate = signUpUiState.birthday.toString(),
            morningReminder = signUpUiState.morningReminder.toString(),
            eveningReminder = signUpUiState.eveningReminder.toString(),
            astTestDate = signUpUiState.astTestDate,
            recommendationTestDate = signUpUiState.recommendationTestDate,
            astTests = listOf(),
            medicines = listOf(),
            metrics = listOf()
        )
        usersRef.document(signUpUiState.id!!).set(user)
    }

    /**
     * [pullUserData] извлечение информации о пользователе через id
     */
    suspend fun pullUserData(userId: String): User {
        if (userId.isEmpty()) return User()
        var user = User()
        usersRef.document(userId).get()
            .addOnSuccessListener {
                user = it?.toObject(User::class.java) ?: User()
            }.await()
        return user
    }

    /**
     * [updateUserProfile] обновление данных пользователя в Firestore Database
     */
    fun updateUserProfile(signUpUiState: SignUpUiState) {
        usersRef.document(signUpUiState.id!!).update(
            "birthDate", signUpUiState.birthday.toString(),
            "name", signUpUiState.name,
            "surname", signUpUiState.surname,
            "patronymic", signUpUiState.patronymic,
            "height", signUpUiState.height.toFloat(),
            "weight", signUpUiState.weight.toFloat()
        )
    }

    /**
     * [updateUser] переносит статистику пользователя в Firestore Database по его userId
     */
    suspend fun updateUser(userId: String) {
        val metricsList = aesculapiusRepository.getAllMetrics().map { metricsItem ->
            hashMapOf(
                "metrics" to metricsItem.metrics,
                "date" to metricsItem.date.toString()
            )
        }
        val astTestsList = aesculapiusRepository.getAllASTResults().map { scoreItem ->
            hashMapOf(
                "score" to scoreItem.score,
                "date" to scoreItem.date.toString()
            )
        }
        val medicinesList = aesculapiusRepository.getAllMedicines().map { medicineItem ->
            hashMapOf(
                "name" to medicineItem.name,
                "undername" to medicineItem.undername,
                "dose" to medicineItem.dose,
                "startDate" to medicineItem.startDate.toString(),
                "endDate" to medicineItem.endDate.toString(),
                "frequency" to medicineItem.frequency
            )
        }
        usersRef.document(userId)
            .update("medicines", medicinesList, "metrics", metricsList, "astTests", astTestsList)
    }
}