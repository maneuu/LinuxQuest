package br.com.linuxquest.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import br.com.linuxquest.app.ui.screens.PracticeScreen
import br.com.linuxquest.app.ui.screens.ProfileScreen
import br.com.linuxquest.app.ui.screens.ProgressScreen
import br.com.linuxquest.app.ui.screens.TrackScreen

@Composable
fun MainScreen() {
    var currentRoute by rememberSaveable { mutableStateOf(Screen.Track.route) }

    val currentScreen = Screen.items.find { it.route == currentRoute } ?: Screen.Track

    Scaffold(
        bottomBar = {
            LinuxQuestBottomNavigation(
                currentScreen = currentScreen,
                onScreenSelected = { screen ->
                    currentRoute = screen.route
                }
            )
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (currentScreen) {
            is Screen.Track -> TrackScreen(modifier = modifier)
            is Screen.Practice -> PracticeScreen(modifier = modifier)
            is Screen.Progress -> ProgressScreen(modifier = modifier)
            is Screen.Profile -> ProfileScreen(modifier = modifier)
        }
    }
}
