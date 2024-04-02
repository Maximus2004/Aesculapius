package com.example.aesculapius.ui.tests

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aesculapius.R
import com.example.aesculapius.database.Converters
import com.example.aesculapius.ui.navigation.NavigationDestination
import com.example.aesculapius.ui.profile.ProfileEvent
import com.example.aesculapius.ui.theme.AesculapiusTheme
import com.example.aesculapius.ui.theme.onErrorContainer
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object TestsScreen : NavigationDestination {
    override val route = "TestsScreen"
}

@Composable
fun TestsScreen(
    astTestDate: String,
    recommendationTestDate: String,
    morningReminder: LocalDateTime,
    eveningReminder: LocalDateTime,
    onProfileEvent: (ProfileEvent) -> Unit,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var now by remember { mutableStateOf(LocalDateTime.now()) }
    var refreshing by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit, key2 = refreshing) {
        now = LocalDateTime.now()
        if (morningReminder.plusMinutes(6).isBefore(now))
            onProfileEvent(ProfileEvent.OnSaveMorningTime(
                morningReminder.plusDays(Duration.between(morningReminder, now).toDays() + 1)
            ))
        if (Duration.between(now, morningReminder).toMinutes() >= 1440)
            onProfileEvent(ProfileEvent.OnSaveMorningTime(
                morningReminder.minusDays(Duration.between(now, morningReminder).toDays())
            ))
        if (eveningReminder.plusMinutes(6).isBefore(now))
            onProfileEvent(ProfileEvent.OnSaveEveningTime(
                eveningReminder.plusDays(Duration.between(eveningReminder, now).toDays() + 1)
            ))
        if (Duration.between(now, eveningReminder).toMinutes() >= 1440)
            onProfileEvent(ProfileEvent.OnSaveEveningTime(
                eveningReminder.minusDays(Duration.between(now, eveningReminder).toDays())
            ))
        if (astTestDate != "" && Converters.stringToDate(astTestDate).isBefore(LocalDate.now()))
            onProfileEvent(ProfileEvent.OnSaveAstTestDate(
                Converters.stringToDate(astTestDate).plusMonths(
                    ChronoUnit.MONTHS.between(Converters.stringToDate(astTestDate), LocalDate.now()) + 1
                )
            ))
        if (astTestDate != "" && ChronoUnit.MONTHS.between(LocalDate.now().plusDays(1), Converters.stringToDate(astTestDate)) >= 1)
            onProfileEvent(ProfileEvent.OnSaveAstTestDate(
                Converters.stringToDate(astTestDate).minusMonths(
                    ChronoUnit.MONTHS.between(LocalDate.now(), Converters.stringToDate(astTestDate))
                )
            ))
        if (recommendationTestDate != "" && Converters.stringToDate(recommendationTestDate).isBefore(LocalDate.now()))
            onProfileEvent(ProfileEvent.OnSaveRecommendationTestDate(
                Converters.stringToDate(recommendationTestDate).plusMonths(
                    ChronoUnit.MONTHS.between(Converters.stringToDate(astTestDate), LocalDate.now()) + 1
                )
            ))
        if (recommendationTestDate != "" && ChronoUnit.MONTHS.between(LocalDate.now().plusDays(1), Converters.stringToDate(recommendationTestDate)) >= 1)
            onProfileEvent(ProfileEvent.OnSaveRecommendationTestDate(
                Converters.stringToDate(recommendationTestDate).minusMonths(
                    ChronoUnit.MONTHS.between(LocalDate.now(), Converters.stringToDate(recommendationTestDate))
                )
            ))
        refreshing = false
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(refreshing),
        onRefresh = { refreshing = true },
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = trigger,
                scale = true,
                backgroundColor = MaterialTheme.colorScheme.background
            )
        }
    ) {
        LazyColumn(modifier = modifier) {
            item {
                Text(
                    text = stringResource(R.string.tests_metrics),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(bottom = 24.dp, top = 10.dp)
                        .fillMaxWidth()
                )
                Text(
                    text = stringResource(R.string.regular_do_tests),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 40.dp, start = 8.dp, end = 8.dp)
                )
                Box() {
                    Card(
                        elevation = 0.dp,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(R.string.input_psv),
                                style = MaterialTheme.typography.headlineLarge,
                                modifier = Modifier.padding(bottom = 4.dp),
                                color = MaterialTheme.colorScheme.onError
                            )
                            Text(
                                text = stringResource(R.string.mark_lungs),
                                style = MaterialTheme.typography.labelSmall
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if ((LocalDateTime.now()
                                        .isAfter(morningReminder) && LocalDateTime.now()
                                        .isBefore(morningReminder.plusMinutes(6)))
                                    || (LocalDateTime.now()
                                        .isAfter(eveningReminder) && LocalDateTime.now()
                                        .isBefore(eveningReminder.plusMinutes(6)))
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.time_icon),
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 8.dp, top = 8.dp)
                                    )
                                    Text(
                                        text = stringResource(R.string.its_time),
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                    Spacer(Modifier.weight(1f))
                                    Card(
                                        modifier = Modifier
                                            .height(40.dp)
                                            .width(81.dp)
                                            .clickable { onNavigate(MetricsOnboardingScreen.route) },
                                        shape = RoundedCornerShape(12.dp),
                                        backgroundColor = MaterialTheme.colorScheme.primary
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.begin),
                                                style = MaterialTheme.typography.headlineMedium,
                                                textAlign = TextAlign.Center,
                                                color = MaterialTheme.colorScheme.tertiaryContainer
                                            )
                                        }
                                    }
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.time_icon),
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 8.dp, top = 8.dp)
                                    )
                                    Text(
                                        text = Converters.countShortDuration(
                                            now,
                                            morningReminder,
                                            eveningReminder
                                        ),
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                    Spacer(Modifier.weight(1f))
                                    Card(
                                        modifier = Modifier
                                            .height(40.dp)
                                            .width(106.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        backgroundColor = MaterialTheme.colorScheme.secondary
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = stringResource(R.string.unable),
                                                style = MaterialTheme.typography.headlineMedium,
                                                textAlign = TextAlign.Center,
                                                color = MaterialTheme.colorScheme.tertiaryContainer
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if ((LocalDateTime.now().isAfter(morningReminder) && LocalDateTime.now()
                            .isBefore(morningReminder.plusMinutes(6)))
                        || (LocalDateTime.now()
                            .isAfter(eveningReminder) && LocalDateTime.now()
                            .isBefore(eveningReminder.plusMinutes(6)))
                    )
                        Canvas(
                            modifier = Modifier
                                .padding(start = 9.dp)
                                .size(16.dp)
                                .align(Alignment.TopEnd)
                        ) {
                            drawCircle(
                                color = onErrorContainer,
                                center = center
                            )
                        }
                }
                Box {
                    Card(
                        elevation = 0.dp,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(top = 16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(id = R.string.rec_test_name),
                                style = MaterialTheme.typography.headlineLarge,
                                modifier = Modifier.padding(bottom = 4.dp),
                                color = MaterialTheme.colorScheme.onError
                            )
                            Text(
                                text = stringResource(R.string.rec_test_explanation),
                                style = MaterialTheme.typography.labelSmall
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (Converters.dateToStringWithFormat(LocalDate.now()) == recommendationTestDate || recommendationTestDate == "") {
                                    Icon(
                                        painter = painterResource(id = R.drawable.time_icon),
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 8.dp, top = 8.dp)
                                    )
                                    Text(
                                        text = stringResource(id = R.string.its_time),
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                    Spacer(Modifier.weight(1f))
                                    Card(
                                        modifier = Modifier
                                            .height(40.dp)
                                            .width(81.dp)
                                            .clickable { onNavigate(RecommendationsOnboardingScreen.route) },
                                        shape = RoundedCornerShape(12.dp),
                                        backgroundColor = MaterialTheme.colorScheme.primary
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.begin),
                                                style = MaterialTheme.typography.headlineMedium,
                                                textAlign = TextAlign.Center,
                                                color = MaterialTheme.colorScheme.tertiaryContainer
                                            )
                                        }
                                    }
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.time_icon),
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 8.dp, top = 8.dp)
                                    )
                                    Text(
                                        text = Converters.countLongDuration(
                                            now.toLocalDate(),
                                            recommendationTestDate
                                        ),
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                    Spacer(Modifier.weight(1f))
                                    Card(
                                        modifier = Modifier
                                            .height(40.dp)
                                            .width(106.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        backgroundColor = MaterialTheme.colorScheme.secondary
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.unable),
                                                style = MaterialTheme.typography.headlineMedium,
                                                textAlign = TextAlign.Center,
                                                color = MaterialTheme.colorScheme.tertiaryContainer
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (Converters.dateToStringWithFormat(LocalDate.now()) == recommendationTestDate || recommendationTestDate == "")
                        Canvas(
                            modifier = Modifier
                                .padding(top = 13.dp, start = 9.dp)
                                .size(16.dp)
                                .align(Alignment.TopEnd)
                        ) {
                            drawCircle(
                                color = onErrorContainer,
                                center = center
                            )
                        }
                }

                Box {
                    Card(
                        elevation = 0.dp,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(top = 16.dp, bottom = 20.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(id = R.string.ast_test_name),
                                style = MaterialTheme.typography.headlineLarge,
                                modifier = Modifier.padding(bottom = 4.dp),
                                color = MaterialTheme.colorScheme.onError
                            )
                            Text(
                                text = stringResource(R.string.ast_test_explantion),
                                style = MaterialTheme.typography.labelSmall
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (Converters.dateToStringWithFormat(LocalDate.now()) == astTestDate || astTestDate == "") {
                                    Icon(
                                        painter = painterResource(id = R.drawable.time_icon),
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 8.dp, top = 8.dp)
                                    )
                                    Text(
                                        text = stringResource(id = R.string.its_time),
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                    Spacer(Modifier.weight(1f))
                                    Card(
                                        modifier = Modifier
                                            .height(40.dp)
                                            .width(81.dp)
                                            .clickable { onNavigate(AstTestOnboardingScreen.route) },
                                        shape = RoundedCornerShape(12.dp),
                                        backgroundColor = MaterialTheme.colorScheme.primary
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.begin),
                                                style = MaterialTheme.typography.headlineMedium,
                                                textAlign = TextAlign.Center,
                                                color = MaterialTheme.colorScheme.tertiaryContainer
                                            )
                                        }
                                    }
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.time_icon),
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 8.dp, top = 8.dp)
                                    )
                                    Text(
                                        text = Converters.countLongDuration(
                                            now.toLocalDate(),
                                            astTestDate
                                        ),
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                    Spacer(Modifier.weight(1f))
                                    Card(
                                        modifier = Modifier
                                            .height(40.dp)
                                            .width(106.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        backgroundColor = MaterialTheme.colorScheme.secondary
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Недоступно",
                                                style = MaterialTheme.typography.headlineMedium,
                                                textAlign = TextAlign.Center,
                                                color = MaterialTheme.colorScheme.tertiaryContainer
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (Converters.dateToStringWithFormat(LocalDate.now()) == astTestDate || astTestDate == "")
                        Canvas(
                            modifier = Modifier
                                .padding(top = 13.dp, start = 9.dp)
                                .size(16.dp)
                                .align(Alignment.TopEnd)
                        ) {
                            drawCircle(
                                color = onErrorContainer,
                                center = center
                            )
                        }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun TestsScreenPreview() {
    AesculapiusTheme {
        TestsScreen(
            astTestDate = Converters.dateToStringWithFormat(LocalDate.now()),
            recommendationTestDate = Converters.dateToStringWithFormat(LocalDate.now()),
            morningReminder = LocalDateTime.now(),
            eveningReminder = LocalDateTime.now(),
            onProfileEvent = {},
            onNavigate = {}
        )
    }
}