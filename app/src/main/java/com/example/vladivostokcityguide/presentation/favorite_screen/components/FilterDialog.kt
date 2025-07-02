package com.example.vladivostokcityguide.presentation.favorite_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.vladivostokcityguide.R
import com.example.vladivostokcityguide.app.ui.theme.NeonGreen
import com.example.vladivostokcityguide.domain.Filter
import com.example.vladivostokcityguide.domain.Order

@Composable
fun FilterDialog(
    onDismissRequest: () -> Unit,
    onApply: (Filter, Order) -> Unit,
    initialSortField: Filter?,
    initialSortOrder: Order?,
    availableSortFields: List<Filter>
) {
    var selectedSortField by rememberSaveable { mutableStateOf(initialSortField) }
    var selectedSortOrder by rememberSaveable {
        mutableStateOf(
            initialSortOrder ?: Order.DESCENDING
        )
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Сортировка",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Button(
                        onClick = { selectedSortOrder = selectedSortOrder.toggle() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = NeonGreen
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(id = selectedSortOrder.resName),
                                style = MaterialTheme.typography.labelLarge,
                            )
                            Icon(
                                imageVector = if (selectedSortOrder == Order.ASCENDING) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                                contentDescription = "Sort Order",
                                modifier = Modifier.height(13.dp)
                            )
                        }
                    }
                }

                availableSortFields.forEach { sortField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedSortField = sortField
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedSortField == sortField,
                            onClick = {
                                selectedSortField = sortField
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = NeonGreen
                            )
                        )
                        Text(text = stringResource(id = sortField.labelRes))
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = onDismissRequest, colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )) {
                        Text(text = "Отмена")
                    }
                    Button(
                        onClick = {
                            selectedSortField?.let {
                                onApply(it, selectedSortOrder)
                            }
                        },
                        enabled = selectedSortField != null &&
                                (selectedSortField != initialSortField || selectedSortOrder != initialSortOrder),
                        modifier = Modifier.padding(start = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonGreen,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(text = "Применить")
                    }
                }
            }
        }
    }
}