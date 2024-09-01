package com.routinealarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.routinealarm.GlobalData.Companion.appContext
import com.routinealarm.helpers.Prefs
import com.routinealarm.helpers.SoundManager
import com.routinealarm.ui.theme.RoutineAlarmTheme

class GlobalData {
    companion object {
        lateinit var appContext: MainActivity
        lateinit var appViewModel: ViewModel
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoutineAlarmTheme {
                MainWidget()
            }
        }
        appContext = this
        Prefs.init(appContext)
        SoundManager.init(appContext)
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
    viewModel: ViewModel = viewModel()
) {
    GlobalData.appViewModel = viewModel
    AlarmList(
        model = viewModel
    )
}
