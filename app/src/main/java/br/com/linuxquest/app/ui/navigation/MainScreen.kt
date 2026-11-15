package br.com.linuxquest.app.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import br.com.linuxquest.app.ui.screens.CommandDetailScreen
import br.com.linuxquest.app.ui.screens.DictionaryScreen
import br.com.linuxquest.app.ui.screens.MissionScreen
import br.com.linuxquest.app.ui.screens.ProfileScreen
import br.com.linuxquest.app.ui.screens.ProgressScreen
import br.com.linuxquest.app.ui.screens.TrackScreen

@Composable
fun MainScreen() {
    var currentRoute by rememberSaveable { mutableStateOf(Screen.Track.route) }
    var selectedLessonId by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedCommandPageId by rememberSaveable { mutableStateOf<String?>(null) }

    // Tela de missão abre em tela separada sem a Bottom Navigation
    if (currentRoute == Screen.Mission.route) {
        MissionScreen(
            lessonId = selectedLessonId,
            onClose = {
                currentRoute = Screen.Track.route
            }
        )
    } else if (currentRoute == Screen.CommandDetail.route && selectedCommandPageId != null) {
        // Detalhes do comando também abrem sem a Bottom Navigation
        val closeCommand = { currentRoute = Screen.Dictionary.route }
        BackHandler(onBack = closeCommand)
        CommandDetailScreen(
            pageId = selectedCommandPageId!!,
            onBack = closeCommand
        )
    } else {
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
                is Screen.Track -> TrackScreen(
                    onOpenMission = { lessonId ->
                        selectedLessonId = lessonId
                        currentRoute = Screen.Mission.route
                    },
                    modifier = modifier
                )
                is Screen.Dictionary -> DictionaryScreen(
                    onOpenCommand = { pageId ->
                        selectedCommandPageId = pageId
                        currentRoute = Screen.CommandDetail.route
                    },
                    modifier = modifier
                )
                is Screen.Progress -> ProgressScreen(modifier = modifier)
                is Screen.Profile -> ProfileScreen(modifier = modifier)
                else -> TrackScreen(
                    onOpenMission = { lessonId ->
                        selectedLessonId = lessonId
                        currentRoute = Screen.Mission.route
                    },
                    modifier = modifier
                )
            }
        }
    }
}
