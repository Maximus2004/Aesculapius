package com.example.aesculapius.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aesculapius.data.onboardingList
import com.example.aesculapius.ui.theme.AesculapiusTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

object OnboardingScreen : NavigationDestination {
    override val route = "OnboardingScreen"
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(onClickEnd: () -> Unit, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = modifier.fillMaxSize()) {
        TextButton(
            onClick = { onClickEnd() },
            Modifier.align(Alignment.End).padding(top = 45.dp, end = 35.dp)
        ) {
            Text(text = "Пропустить", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.weight(1f))
        HorizontalPager(
            state = pagerState,
            count = 6,
            modifier = Modifier
                .wrapContentSize()
                .height(200.dp)
        ) { page ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 42.dp)
            ) {
                Text(
                    text = onboardingList[page].title,
                    modifier = Modifier.padding(bottom = 23.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = onboardingList[page].description,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        DotsMenu(
            totalDots = 6,
            selectedIndex = pagerState.currentPage,
            modifier = Modifier.padding(bottom = 48.dp, top = 114.dp)
        )
        ContinueButton(
            onClick = {
                if (pagerState.currentPage < 5)
                    coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                else
                    onClickEnd()
            },
            buttonText = if (pagerState.currentPage < 5) "Дальше" else "Начать",
            modifier = Modifier
                .size(width = 266.dp, height = 53.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun DotsMenu(totalDots: Int, selectedIndex: Int, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center
    ) {
        items(totalDots) { index ->
            if (index == selectedIndex)
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(color = Color.Black)
                )
            else
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(color = Color.LightGray)
                )
            if (index != totalDots - 1) Spacer(modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

@Composable
fun ContinueButton(onClick: () -> Unit, buttonText: String, modifier: Modifier = Modifier) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RectangleShape,
        modifier = modifier
    ) {
        Text(text = buttonText, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun OnBoardingScreenPreview() {
    AesculapiusTheme {
        OnboardingScreen(onClickEnd = {})
    }
}