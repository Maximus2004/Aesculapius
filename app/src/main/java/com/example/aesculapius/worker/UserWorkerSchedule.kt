package com.example.aesculapius.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.aesculapius.database.UserRemoteDataRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

class UserWorkerSchedule (context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        // вызов периодического бэкапа результатов тестов и метрик
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val yourWorkRequest = PeriodicWorkRequestBuilder<UserWorker>(30, TimeUnit.MINUTES).setInputData(inputData).setConstraints(constraints).build()
        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork("userWork", ExistingPeriodicWorkPolicy.KEEP, yourWorkRequest)
        return Result.success()
    }
}
