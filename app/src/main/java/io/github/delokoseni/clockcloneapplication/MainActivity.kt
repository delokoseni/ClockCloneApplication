package io.github.delokoseni.clockcloneapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import io.github.delokoseni.clockcloneapplication.ui.theme.ClockCloneApplicationTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.remember

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClockCloneApplicationTheme {
                ClockCloneApplicationApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun ClockCloneApplicationApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.ALARM_CLOCKS) }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            painterResource(it.icon),
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AppHeader(title = currentDestination.label)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    if (currentDestination == AppDestinations.ALARM_CLOCKS) {
                        Box(
                            modifier = Modifier.align(Alignment.BottomEnd)
                        ) {
                            AddAlarmButton(
                                onClick = {
                                    showTimePicker = true
                                }
                            )
                        }
                    }
                }
            }
        }
        if (showTimePicker) {
            AlarmTimePickerDialog(
                onDismiss = {
                    showTimePicker = false
                },
                onConfirm = { hour, minute ->
                    showTimePicker = false
                    Log.d("AlarmClock", "Выбрано время: $hour:$minute")
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmTimePickerDialog(onDismiss: () -> Unit, onConfirm: (Int, Int) -> Unit) {
    val timeState = rememberTimePickerState(is24Hour = true)

    TimePickerDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Выбор времени")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        timeState.hour,
                        timeState.minute
                    )
                }
            ) {
                Text("ОК")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    ) {
        TimePicker(state = timeState)
    }
}

enum class AppDestinations(
    val label: String,
    val icon: Int,
) {
    ALARM_CLOCKS("Булильники", R.drawable.ic_alarm_clock),
    WORLD_CLOCKS("Мировые часы", R.drawable.ic_globe),
    TIMERS("Таймеры", R.drawable.ic_hourglass),
    STOPWATCH("Секундомер", R.drawable.ic_timer),
    SLEEP_MODE("Режим сна", R.drawable.ic_person_in_bed)
}

@Composable
fun AppHeader(title: String) {
    var menuExpanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )

        Box {
            IconButton(
                onClick = { menuExpanded = true }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_more_vert),
                    contentDescription = "Дополнительное меню"
                )
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Заставка") },
                    onClick = { menuExpanded = false }
                )

                DropdownMenuItem(
                    text = { Text("Настройки") },
                    onClick = { menuExpanded = false }
                )

                DropdownMenuItem(
                    text = { Text("Политика конфиденциальности") },
                    onClick = { menuExpanded = false }
                )

                DropdownMenuItem(
                    text = { Text("Отправить отзыв") },
                    onClick = { menuExpanded = false }
                )

                DropdownMenuItem(
                    text = { Text("Справка") },
                    onClick = { menuExpanded = false }
                )
            }
        }
    }
}

@Composable
fun AddAlarmButton(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add),
            contentDescription = "Создать будильник"
        )
    }
}