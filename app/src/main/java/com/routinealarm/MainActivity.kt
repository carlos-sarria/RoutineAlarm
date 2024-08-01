package com.routinealarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.routinealarm.ui.theme.RoutineAlarmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoutineAlarmTheme {
                MainWidget(modifier = Modifier)
            }
        }
        Prefs.init(this)
    }
}

@Preview(showBackground = true, widthDp = 480, heightDp = 900)
@Composable
fun MainActivityPreview() {
    RoutineAlarmTheme {
        MainWidget()
    }
}

@Composable
fun MainWidget(
    modifier: Modifier = Modifier,
    viewModel: ViewModel = viewModel()
) {
    NavigationApp(modifier, viewModel)
}