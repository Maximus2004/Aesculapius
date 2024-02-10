package com.example.aesculapius.ui.therapy

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.aesculapius.data.medicinesAerosol
import com.example.aesculapius.data.medicinesPowder
import com.example.aesculapius.data.medicinesTablets
import com.example.aesculapius.ui.TopBar
import com.example.aesculapius.ui.navigation.NavigationDestination

object EditMedicineScreen : NavigationDestination {
    override val route = "EditMedicineScreen"
}

@Composable
fun EditMedicineScreen(
    onClickDeleteMedicine: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    turnOffBars: () -> Unit,
    onClickUpdateMedicineItem: (Int, String, String) -> Unit,
    medicine: MedicineItem,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentMedicine: MutableState<Medicine?> = remember { mutableStateOf(null) }
    var selectedDosesIndex by remember {
        mutableIntStateOf(currentMedicine.value?.doses?.indexOf(medicine.dose) ?: 0)
    }
    var selectedFrequencyIndex by remember {
        mutableIntStateOf(currentMedicine.value?.frequency?.indexOf(medicine.frequency) ?: 0)
    }

    LaunchedEffect(key1 = Unit) {
        turnOffBars()
        when (medicine.medicineType) {
            "аэрозоль" -> {
                medicinesAerosol.forEach { medicineAerosol ->
                    if (medicineAerosol.name == medicine.name)
                        currentMedicine.value = medicineAerosol
                }
            }
            "порошок" -> {
                medicinesPowder.forEach { medicinePowder ->
                    if (medicinePowder.name == medicine.name)
                        currentMedicine.value = medicinePowder
                }
            }
            "таблетки" -> {
                medicinesTablets.forEach { medicinesTablet ->
                    if (medicinesTablet.name == medicine.name)
                        currentMedicine.value = medicinesTablet
                }
            }
        }
    }

    Scaffold(topBar = {
        TopBar(
            onNavigateBack = onNavigateBack,
            text = "Редактировать препарат",
            existHelpButton = false
        )
    }) { paddingValues ->
        Column(
            modifier = modifier.padding(
                top = paddingValues.calculateTopPadding() + 32.dp,
                start = 24.dp,
                end = 24.dp
            ), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = medicine.name,
                style = MaterialTheme.typography.displayMedium,
                color = Color.Black
            )
            Text(
                text = "${medicine.medicineType}, ${medicine.undername}",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF79747E)
            )
            DropdownMenu(
                menuName = "Дозировка",
                menuList = currentMedicine.value?.doses ?: listOf(medicine.dose),
                onCloseAction = { index -> selectedDosesIndex = index },
                initialIndex = selectedDosesIndex,
                modifier = Modifier.padding(top = 32.dp, bottom = 34.dp)
            )
            DropdownMenu(
                menuName = "Кратность приёма",
                menuList = currentMedicine.value?.frequency ?: listOf(medicine.frequency),
                onCloseAction = { index -> selectedFrequencyIndex = index },
                initialIndex = selectedFrequencyIndex
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    onClickUpdateMedicineItem(
                        medicine.id,
                        currentMedicine.value?.frequency?.get(selectedFrequencyIndex) ?: medicine.frequency,
                        currentMedicine.value?.doses?.get(selectedDosesIndex) ?: medicine.dose
                    )
                    Toast.makeText(context, "Лекарство успешно сохранено", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .padding(bottom = 17.dp)
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
            TextButton(
                onClick = {
                    onClickDeleteMedicine(medicine.id)
                    Toast.makeText(context, "Лекарство успешно удалено", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.padding(bottom = 48.dp)
            ) {
                Text(
                    text = "Удалить препарат",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}