package com.example.aesculapius.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class UserWorkerSchedule (context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        // вызов периодического бэкапа результатов тестов и метрик
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = PeriodicWorkRequestBuilder<UserWorker>(30, TimeUnit.MINUTES)
            .setInputData(inputData).setConstraints(constraints).build()
        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork("userWork", ExistingPeriodicWorkPolicy.KEEP, workRequest)
        return Result.success()
    }
}
