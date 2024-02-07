package com.example.aesculapius.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.aesculapius.database.UserRemoteDataRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

class UserWorker (context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    lateinit var userRemoteDataRepository: UserRemoteDataRepository

    override suspend fun doWork(): Result {
        Log.i("TAGTAG", inputData.getString("userId") ?: "")
        userRemoteDataRepository.updateUser(inputData.getString("userId") ?: "")
        return Result.success()
    }
}