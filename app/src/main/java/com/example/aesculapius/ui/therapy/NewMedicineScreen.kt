package com.example.aesculapius.ui.therapy

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aesculapius.R
import com.example.aesculapius.data.CurrentMedicineType
import com.example.aesculapius.data.medicinesAerosol
import com.example.aesculapius.data.medicinesPowder
import com.example.aesculapius.data.medicinesTablets
import com.example.aesculapius.ui.TopBar
import com.example.aesculapius.ui.navigation.NavigationDestination
import com.example.aesculapius.ui.theme.AesculapiusTheme
import java.time.LocalDate

object NewMedicineScreen : NavigationDestination {
    override val route = "NewMedicineScreen"
}

@Composable
fun NewMedicineScreen(
    onNavigateBack: () -> Unit,
    onClickFinishButton: (
        image: Int,
        medicineType: String,
        name: String,
        undername: String,
        dose: String,
        frequency: String,
        startDate: LocalDate,
        endDate: LocalDate
    ) -> Unit,
    currentDate: LocalDate,
    turnOffBars: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentMedicineType by remember { mutableStateOf(CurrentMedicineType.Aerosol) }
    lateinit var currentMedicineItem: Medicine
    var selectedItemIndex by remember { mutableIntStateOf(0) }
    var selectedDosesIndex by remember { mutableIntStateOf(0) }
    var selectedFrequencyIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = Unit) { turnOffBars() }

    Scaffold(topBar = {
        TopBar(onNavigateBack = { onNavigateBack() }, text = "Новый препарат")
    }) { paddingValues ->
        Column(
            modifier = modifier.padding(
                top = paddingValues.calculateTopPadding(),
                start = 24.dp,
                end = 24.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.padding(top = 24.dp)) {
                Card(
                    modifier = Modifier
                        .size(96.dp)
                        .clickable { currentMedicineType = CurrentMedicineType.Aerosol },
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor =
                    if (currentMedicineType == CurrentMedicineType.Aerosol) MaterialTheme.colorScheme.primary
                    else Color(0xFFE3E0EA)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.therapy_icon),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                            colorFilter = ColorFilter.tint(if (currentMedicineType == CurrentMedicineType.Aerosol) Color.White else MaterialTheme.colorScheme.primary),
                            alignment = Alignment.TopCenter
                        )
                        Text(
                            text = "аэрозоль",
                            color =
                            if (currentMedicineType == CurrentMedicineType.Aerosol) Color.White
                            else MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                Spacer(Modifier.weight(1f))

                Card(
                    modifier = Modifier
                        .size(96.dp)
                        .clickable { currentMedicineType = CurrentMedicineType.Powder },
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor =
                    if (currentMedicineType == CurrentMedicineType.Powder) MaterialTheme.colorScheme.primary
                    else Color(0xFFE3E0EA)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.therapy_icon),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                            colorFilter = ColorFilter.tint(if (currentMedicineType == CurrentMedicineType.Powder) Color.White else MaterialTheme.colorScheme.primary),
                            alignment = Alignment.TopCenter
                        )
                        Text(
                            text = "порошок",
                            color =
                            if (currentMedicineType == CurrentMedicineType.Powder) Color.White
                            else MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                Spacer(Modifier.weight(1f))

                Card(
                    modifier = Modifier
                        .size(96.dp)
                        .clickable { currentMedicineType = CurrentMedicineType.Tablets },
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor =
                    if (currentMedicineType == CurrentMedicineType.Tablets) MaterialTheme.colorScheme.primary
                    else Color(0xFFE3E0EA)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.therapy_icon),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                            colorFilter = ColorFilter.tint(if (currentMedicineType == CurrentMedicineType.Tablets) Color.White else MaterialTheme.colorScheme.primary),
                            alignment = Alignment.TopCenter
                        )
                        Text(
                            text = "таблетки",
                            color =
                            if (currentMedicineType == CurrentMedicineType.Tablets) Color.White
                            else MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            when (currentMedicineType) {
                CurrentMedicineType.Aerosol -> {
                    DropdownMenu(
                        menuName = "Название",
                        menuList = List(medicinesAerosol.size) { index -> medicinesAerosol[index].name },
                        onCloseAction = { index -> selectedItemIndex = index },
                        modifier = Modifier.padding(top = 48.dp)
                    )
                    DropdownMenu(
                        menuName = "Дозировка",
                        menuList = medicinesAerosol[selectedItemIndex].doses,
                        onCloseAction = { index -> selectedDosesIndex = index },
                        modifier = Modifier.padding(top = 40.dp)
                    )
                    DropdownMenu(
                        menuName = "Кратность приёма",
                        menuList = medicinesAerosol[selectedItemIndex].frequency,
                        onCloseAction = { index -> selectedFrequencyIndex = index },
                        modifier = Modifier.padding(top = 40.dp)
                    )
                    currentMedicineItem = medicinesAerosol[selectedItemIndex]
                }

                CurrentMedicineType.Powder -> {
                    DropdownMenu(
                        menuName = "Название",
                        menuList = List(medicinesPowder.size) { index -> medicinesPowder[index].name },
                        onCloseAction = { index -> selectedItemIndex = index },
                        modifier = Modifier.padding(top = 48.dp)
                    )
                    DropdownMenu(
                        menuName = "Дозировка",
                        menuList = medicinesPowder[selectedItemIndex].doses,
                        onCloseAction = { index -> selectedDosesIndex = index },
                        modifier = Modifier.padding(top = 40.dp)
                    )
                    DropdownMenu(
                        menuName = "Кратность приёма",
                        menuList = medicinesPowder[selectedItemIndex].frequency,
                        onCloseAction = { index -> selectedFrequencyIndex = index },
                        modifier = Modifier.padding(top = 40.dp)
                    )
                    currentMedicineItem = medicinesPowder[selectedItemIndex]
                }

                CurrentMedicineType.Tablets -> {
                    DropdownMenu(
                        menuName = "Название",
                        menuList = List(medicinesTablets.size) { index -> medicinesTablets[index].name },
                        onCloseAction = { index -> selectedItemIndex = index },
                        modifier = Modifier.padding(top = 48.dp)
                    )
                    DropdownMenu(
                        menuName = "Дозировка",
                        menuList = medicinesTablets[selectedItemIndex].doses,
                        onCloseAction = { index -> selectedDosesIndex = index },
                        modifier = Modifier.padding(top = 40.dp)
                    )
                    DropdownMenu(
                        menuName = "Кратность приёма",
                        menuList = medicinesTablets[selectedItemIndex].frequency,
                        onCloseAction = { index -> selectedFrequencyIndex = index },
                        modifier = Modifier.padding(top = 40.dp)
                    )
                    currentMedicineItem = medicinesTablets[selectedItemIndex]
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    val medicineType =
                        when (currentMedicineType) {
                            CurrentMedicineType.Tablets -> "таблетки"
                            CurrentMedicineType.Powder -> "порошок"
                            CurrentMedicineType.Aerosol -> "аэрозоль"
                        }
                    onClickFinishButton(
                        currentMedicineItem.image,
                        medicineType,
                        currentMedicineItem.name,
                        currentMedicineItem.undername,
                        currentMedicineItem.doses[selectedDosesIndex],
                        currentMedicineItem.frequency[selectedFrequencyIndex],
                        currentDate,
                        currentDate.plusMonths(1),
                    )
                },
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .height(56.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Сохранить",
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenu(
    menuName: String,
    menuList: List<String>,
    onCloseAction: (Int) -> Unit,
    modifier: Modifier = Modifier,
    initialIndex: Int = 0
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(menuList[initialIndex]) }
    val scrollState = rememberScrollState()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedItem,
            label = { Text(text = menuName, color = MaterialTheme.colorScheme.primary) },
            onValueChange = {},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            singleLine = true,
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .wrapContentHeight()
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            ),
            textStyle = MaterialTheme.typography.bodyLarge
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .scrollable(state = scrollState, orientation = Orientation.Vertical)
                .background(color = Color.White)
                .heightIn(max = 240.dp)
        ) {
            menuList.forEachIndexed { index, selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        selectedItem = selectionOption
                        onCloseAction(index)
                        expanded = false
                    },
                    modifier = Modifier.background(
                        color = if (selectedItem != selectionOption) Color.White else Color(
                            0x146750A4
                        )
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewMedicineScreenPreview() {
    AesculapiusTheme {
        //NewMedicineScreen(onNavigateBack = {})
    }
}