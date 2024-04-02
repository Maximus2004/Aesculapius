package com.example.aesculapius.ui.therapy

import android.widget.Toast
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptionsBuilder
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
    onTherapyEvent: (TherapyEvent) -> Unit,
    onNavigate: (String, NavOptionsBuilder.() -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentMedicineType by remember { mutableStateOf(CurrentMedicineType.Aerosol) }
    lateinit var currentMedicineItem: Medicine
    var selectedItemIndex by remember { mutableIntStateOf(0) }
    var selectedDosesIndex by remember { mutableIntStateOf(0) }
    var selectedFrequencyIndex by remember { mutableIntStateOf(0) }
    var isFrequencyChoosen by remember { mutableStateOf(true) }
    var isDoseChoosen by remember { mutableStateOf(true) }

    Scaffold(topBar = {
        TopBar(onNavigateBack = { onNavigateBack() }, text = stringResource(R.string.new_medicine))
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
                        .clickable {
                            if (currentMedicineType != CurrentMedicineType.Aerosol) {
                                currentMedicineType = CurrentMedicineType.Aerosol
                                isDoseChoosen = true
                                isFrequencyChoosen = true
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor =
                    if (currentMedicineType == CurrentMedicineType.Aerosol) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.errorContainer
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.aerosol_icon),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                            colorFilter = ColorFilter.tint(
                                if (currentMedicineType == CurrentMedicineType.Aerosol) MaterialTheme.colorScheme.tertiaryContainer
                                else MaterialTheme.colorScheme.primary
                            ),
                            alignment = Alignment.TopCenter
                        )
                        Text(
                            text = stringResource(R.string.aerosol),
                            color =
                            if (currentMedicineType == CurrentMedicineType.Aerosol) MaterialTheme.colorScheme.tertiaryContainer
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
                        .clickable {
                            if (currentMedicineType != CurrentMedicineType.Powder) {
                                currentMedicineType = CurrentMedicineType.Powder
                                isDoseChoosen = true
                                isFrequencyChoosen = true
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor =
                    if (currentMedicineType == CurrentMedicineType.Powder) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.errorContainer
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.powder_icon),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                            colorFilter = ColorFilter.tint(
                                if (currentMedicineType == CurrentMedicineType.Powder) MaterialTheme.colorScheme.tertiaryContainer
                                else MaterialTheme.colorScheme.primary
                            ),
                            alignment = Alignment.TopCenter
                        )
                        Text(
                            text = stringResource(R.string.powder),
                            color =
                            if (currentMedicineType == CurrentMedicineType.Powder) MaterialTheme.colorScheme.tertiaryContainer
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
                        .clickable {
                            if (currentMedicineType != CurrentMedicineType.Tablets) {
                                currentMedicineType = CurrentMedicineType.Tablets
                                isDoseChoosen = true
                                isFrequencyChoosen = true
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor =
                    if (currentMedicineType == CurrentMedicineType.Tablets) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.errorContainer
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.tablets_icon),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                            colorFilter = ColorFilter.tint(
                                if (currentMedicineType == CurrentMedicineType.Tablets) MaterialTheme.colorScheme.tertiaryContainer
                                else MaterialTheme.colorScheme.primary
                            ),
                            alignment = Alignment.TopCenter
                        )
                        Text(
                            text = stringResource(R.string.tablets),
                            color =
                            if (currentMedicineType == CurrentMedicineType.Tablets) MaterialTheme.colorScheme.tertiaryContainer
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
                        menuName = stringResource(R.string.naming),
                        menuList = List(medicinesAerosol.size) { index -> medicinesAerosol[index].name },
                        onCloseAction = { index ->
                            selectedItemIndex = index
                            isDoseChoosen = false
                            isFrequencyChoosen = false
                        },
                        modifier = Modifier.padding(top = 48.dp),
                    )
                    DropdownMenu(
                        menuName = stringResource(id = R.string.dose),
                        menuList = medicinesAerosol[selectedItemIndex].doses,
                        onCloseAction = { index ->
                            selectedDosesIndex = index
                            isDoseChoosen = true
                        },
                        modifier = Modifier.padding(top = 40.dp),
                        isChoosen = isDoseChoosen
                    )
                    DropdownMenu(
                        menuName = stringResource(id = R.string.frequency),
                        menuList = medicinesAerosol[selectedItemIndex].frequency,
                        onCloseAction = { index ->
                            selectedFrequencyIndex = index
                            isFrequencyChoosen = true
                        },
                        modifier = Modifier.padding(top = 40.dp),
                        isChoosen = isFrequencyChoosen
                    )
                    currentMedicineItem = medicinesAerosol[selectedItemIndex]
                }

                CurrentMedicineType.Powder -> {
                    DropdownMenu(
                        menuName = stringResource(R.string.naming),
                        menuList = List(medicinesPowder.size) { index -> medicinesPowder[index].name },
                        onCloseAction = { index ->
                            selectedItemIndex = index
                            isDoseChoosen = false
                            isFrequencyChoosen = false
                        },
                        modifier = Modifier.padding(top = 48.dp),
                    )
                    DropdownMenu(
                        menuName = stringResource(id = R.string.dose),
                        menuList = medicinesPowder[selectedItemIndex].doses,
                        onCloseAction = { index ->
                            selectedDosesIndex = index
                            isDoseChoosen = true
                        },
                        modifier = Modifier.padding(top = 40.dp),
                        isChoosen = isDoseChoosen
                    )
                    DropdownMenu(
                        menuName = stringResource(id = R.string.frequency),
                        menuList = medicinesPowder[selectedItemIndex].frequency,
                        onCloseAction = { index ->
                            selectedFrequencyIndex = index
                            isFrequencyChoosen = true
                        },
                        modifier = Modifier.padding(top = 40.dp),
                        isChoosen = isFrequencyChoosen
                    )
                    currentMedicineItem = medicinesPowder[selectedItemIndex]
                }

                CurrentMedicineType.Tablets -> {
                    DropdownMenu(
                        menuName = stringResource(R.string.naming),
                        menuList = List(medicinesTablets.size) { index -> medicinesTablets[index].name },
                        onCloseAction = { index ->
                            selectedItemIndex = index
                            isDoseChoosen = false
                            isFrequencyChoosen = false
                        },
                        modifier = Modifier.padding(top = 48.dp),
                    )
                    DropdownMenu(
                        menuName = stringResource(id = R.string.dose),
                        menuList = medicinesTablets[selectedItemIndex].doses,
                        onCloseAction = { index ->
                            selectedDosesIndex = index
                            isDoseChoosen = true
                        },
                        modifier = Modifier.padding(top = 40.dp),
                        isChoosen = isDoseChoosen
                    )
                    DropdownMenu(
                        menuName = stringResource(id = R.string.frequency),
                        menuList = medicinesTablets[selectedItemIndex].frequency,
                        onCloseAction = { index ->
                            selectedFrequencyIndex = index
                            isFrequencyChoosen = true
                        },
                        modifier = Modifier.padding(top = 40.dp),
                        isChoosen = isFrequencyChoosen
                    )
                    currentMedicineItem = medicinesTablets[selectedItemIndex]
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    if (!isFrequencyChoosen || !isDoseChoosen)
                        Toast.makeText(context, context.getString(R.string.choose_dose_frequency), Toast.LENGTH_SHORT).show()
                    else {
                        onTherapyEvent(
                            TherapyEvent.OnAddMedicineItem(
                                currentMedicineType,
                                currentMedicineItem.name,
                                currentMedicineItem.undername,
                                currentMedicineItem.doses[selectedDosesIndex],
                                currentMedicineItem.frequency[selectedFrequencyIndex],
                                LocalDate.now(),
                                LocalDate.now().plusMonths(1)
                            )
                        )
                        onNavigate(TherapyScreen.route) {
                            popUpTo(TherapyScreen.route) { inclusive = false }
                        }
                    }
                },
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .height(56.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.save),
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
    initialIndex: Int = 0,
    isChoosen: Boolean = true
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
            value = if (isChoosen) selectedItem else "",
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
                .background(color = MaterialTheme.colorScheme.tertiaryContainer)
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
                        color =
                        if (selectedItem != selectionOption) MaterialTheme.colorScheme.tertiaryContainer
                        else MaterialTheme.colorScheme.outline
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
        NewMedicineScreen(onNavigateBack = {}, onNavigate = { _, _ -> }, onTherapyEvent = { _ -> })
    }
}