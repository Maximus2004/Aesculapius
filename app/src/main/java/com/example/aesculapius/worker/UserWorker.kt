package com.example.aesculapius.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.aesculapius.database.UserRemoteDataRepository

class UserWorker (context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    lateinit var userRemoteDataRepository: UserRemoteDataRepository

    override suspend fun doWork(): Result {
        userRemoteDataRepository.updateUser(inputData.getString("userId") ?: "")
        return Result.success()
    }
}