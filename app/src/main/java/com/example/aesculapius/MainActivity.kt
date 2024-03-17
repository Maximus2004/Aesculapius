package com.example.aesculapius

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.aesculapius.ui.home.HomeScreen
import com.example.aesculapius.ui.navigation.SignUpNavigation
import com.example.aesculapius.ui.profile.ProfileEvent
import com.example.aesculapius.ui.profile.ProfileViewModel
import com.example.aesculapius.ui.signup.SignUpUiState
import com.example.aesculapius.ui.theme.AesculapiusTheme
import com.example.aesculapius.worker.UserWorkerSchedule
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // инициализация библиотеки для работы с временем
        AndroidThreeTen.init(this)

        setContent {
            AesculapiusTheme {

                // здесь при запуске приложения инициализируются переменные, хранящиеся в DataStore Preferences
                val profileViewModel: ProfileViewModel = hiltViewModel()
                val userUiState: SignUpUiState by profileViewModel.userUiState.collectAsState()

                // так как id - это String, хранящий в себе id текущего пользователя, мы должны обработать его состояние
                when (userUiState.id) {
                    "" -> {
                        SignUpNavigation(
                            // в окончание регистрации меняет id и устанавливает напоминания
                            onEndRegistration = {
                                profileViewModel.onEvent(ProfileEvent.OnSaveNewUser(
                                    it.copy(id = UserRemoteDataRepository.getUserId())
                                ))
                            }
                        )
                    }

                    null -> ImageGifDisplay()

                    else -> {
                        val inputData = Data.Builder().putString("userId", userUiState.id).build()
                        val workRequest = OneTimeWorkRequestBuilder<UserWorkerSchedule>().setInputData(inputData).build()
                        WorkManager.getInstance(this).enqueue(workRequest)

                        HomeScreen(
                            userUiState = userUiState,
                            onProfileEvent = profileViewModel::onEvent
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