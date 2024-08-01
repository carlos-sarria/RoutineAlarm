package com.routinealarm

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


enum class FlowRoutes {
    Config,
    List
}

@Composable
fun NavigationApp(modifier: Modifier = Modifier, viewModel: ViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = FlowRoutes.List.name
    ) {
        composable(route = FlowRoutes.Config.name+"/{arg}") { backStackEntry ->
            //val arg = backStackEntry.arguments?.getInt("arg") ?: 0 // getInt() does not work ?
            val arg = backStackEntry.arguments?.getString("arg").toString().toInt()
            AlarmSetup (
                modifier = modifier,
                model = viewModel,
                alarmId = arg,
                onClick = {
                    navController.navigate(FlowRoutes.List.name)
                }
            )
        }
        composable(route = FlowRoutes.List.name) {
            AlarmList(
                alarmList = viewModel.alarms,
                model = viewModel,
                onListButtonClicked = { alarmId ->
                    navController.navigate(FlowRoutes.Config.name + "/$alarmId")}
            )
        }
    }
}

