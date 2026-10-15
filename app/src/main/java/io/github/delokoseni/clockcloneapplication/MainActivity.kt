package io.github.delokoseni.clockcloneapplication

import android.os.Bundle
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import io.github.delokoseni.clockcloneapplication.ui.theme.ClockCloneApplicationTheme

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
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Text(
                text = currentDestination.label,
                modifier = Modifier.padding(innerPadding)
            )
        }
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