package com.example.aesculapius

import android.app.PendingIntent
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.size.Size
import com.example.aesculapius.database.UserRemoteDataRepository
import com.example.aesculapius.notifications.MetricsAlarm
import com.example.aesculapius.ui.home.HomeScreen
import com.example.aesculapius.ui.navigation.SignUpNavigation
import com.example.aesculapius.ui.profile.ProfileViewModel
import com.example.aesculapius.ui.signup.SignUpUiState
import com.example.aesculapius.ui.theme.AesculapiusTheme
import com.example.aesculapius.worker.UserWorkerSchedule
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.AndroidEntryPoint
import java.time.ZoneId

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        setContent {
            AesculapiusTheme {
                val profileViewModel: ProfileViewModel = hiltViewModel()
                val userId by profileViewModel.userId.collectAsState()
                val morningReminder by profileViewModel.morningReminder.collectAsState()
                val eveningReminder by profileViewModel.eveningReminder.collectAsState()
                val astTestDate by profileViewModel.ASTTestDate.collectAsState()
                val recommendationTestDate by profileViewModel.recommendationTestDate.collectAsState()
                val user: SignUpUiState by profileViewModel.user.collectAsState()

                when (userId) {
                    "" -> {
                        SignUpNavigation(
                            morningReminder = morningReminder,
                            eveningReminder = eveningReminder,
                            saveMorningReminder = { profileViewModel.saveMorningTime(it) },
                            saveEveningReminder = { profileViewModel.saveEveningTime(it) },
                            onEndRegistration = {
                                profileViewModel.changeUserAtFirst(UserRemoteDataRepository.getUserId(), it)

                                val morningTimeMillis = morningReminder.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                                val morningIntent = Intent(this, MetricsAlarm::class.java).apply {
                                    putExtra("title", "Утреннее напоминание")
                                    putExtra("message", "Не забудьте ввести метрики с пикфлоуметра!")
                                }
                                val morningPendingIntent = PendingIntent.getBroadcast(
                                    this,
                                    0,
                                    morningIntent,
                                    PendingIntent.FLAG_IMMUTABLE
                                )
                                profileViewModel.setMorningNotification(
                                    morningTimeMillis,
                                    morningPendingIntent
                                )

                                val eveningTimeMillis = eveningReminder.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                                val eveningIntent = Intent(this, MetricsAlarm::class.java).apply {
                                    putExtra("title", "Вечернее напоминание")
                                    putExtra("message", "Не забудьте ввести метрики с пикфлоуметра!")
                                }
                                val eveningPendingIntent = PendingIntent.getBroadcast(
                                    this,
                                    1,
                                    eveningIntent,
                                    PendingIntent.FLAG_IMMUTABLE
                                )
                                profileViewModel.setEveningNotification(
                                    eveningTimeMillis,
                                    eveningPendingIntent
                                )
                            }
                        )
                    }

                    null -> ImageGifDisplay()

                    else -> {
                        val inputData = Data.Builder().putString("userId", userId).build()
                        val workRequest = OneTimeWorkRequestBuilder<UserWorkerSchedule>().setInputData(inputData).build()
                        WorkManager.getInstance(this).enqueue(workRequest)

                        HomeScreen(
                            morningReminder = morningReminder,
                            eveningReminder = eveningReminder,
                            saveMorningReminder = {
                                profileViewModel.saveMorningTime(it)
                                val morningTimeMillis = it.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                                val morningIntent = Intent(this, MetricsAlarm::class.java).apply {
                                    putExtra("title", "Утреннее напоминание")
                                    putExtra("message", "Не забудьте ввести метрики с пикфлоуметра!")
                                }
                                val morningPendingIntent = PendingIntent.getBroadcast(
                                    this,
                                    0,
                                    morningIntent,
                                    PendingIntent.FLAG_IMMUTABLE
                                )
                                profileViewModel.setMorningNotification(
                                    morningTimeMillis,
                                    morningPendingIntent
                                )
                            },
                            saveEveningReminder = {
                                profileViewModel.saveEveningTime(it)
                                val eveningTimeMillis = it.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                                val eveningIntent = Intent(this, MetricsAlarm::class.java).apply {
                                    putExtra("title", "Вечернее напоминание")
                                    putExtra("message", "Не забудьте ввести метрики с пикфлоуметра!")
                                }
                                val eveningPendingIntent = PendingIntent.getBroadcast(
                                    this,
                                    1,
                                    eveningIntent,
                                    PendingIntent.FLAG_IMMUTABLE
                                )
                                profileViewModel.setEveningNotification(
                                    eveningTimeMillis,
                                    eveningPendingIntent
                                )
                            },
                            recommendationTestDate = recommendationTestDate,
                            astTestDate = astTestDate,
                            saveAstDate = { profileViewModel.saveAstTestDate(it) },
                            saveRecommendationDate = { profileViewModel.saveRecommendationTestDate(it) },
                            user = user,
                            onSaveNewUser = { profileViewModel.updateUserProfile(it, userId!!) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ImageGifDisplay() {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = rememberAsyncImagePainter(
                coil.request.ImageRequest.Builder(context).data(data = R.drawable.downloading_dif)
                    .apply(block = {
                        size(Size.ORIGINAL)
                    }).build(), imageLoader = imageLoader
            ),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}