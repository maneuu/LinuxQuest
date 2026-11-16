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
import br.com.linuxquest.app.ui.screens.AuthScreen
import br.com.linuxquest.app.ui.screens.CommandDetailScreen
import br.com.linuxquest.app.ui.screens.NotesScreen
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
    // true = abre a tela de login na aba "Criar Conta"
    var authStartWithCreate by rememberSaveable { mutableStateOf(true) }

    if (currentRoute == Screen.Auth.route) {
        // Login / Cadastro também abre sem a Bottom Navigation
        val closeAuth = { currentRoute = Screen.Profile.route }
        BackHandler(onBack = closeAuth)
        AuthScreen(
            startWithCreateAccount = authStartWithCreate,
            onBack = closeAuth,
            onSuccess = closeAuth
        )
    } else if (currentRoute == Screen.Mission.route) {
        // Tela de missão abre em tela separada sem a Bottom Navigation
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
        // Anotações fica "dentro" da aba Perfil
        val isNotesOpen = currentRoute == Screen.Notes.route
        val currentScreen = if (isNotesOpen) {
            Screen.Profile
        } else {
            Screen.items.find { it.route == currentRoute } ?: Screen.Track
        }

        if (isNotesOpen) {
            BackHandler { currentRoute = Screen.Profile.route }
        }

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
            if (isNotesOpen) {
                NotesScreen(modifier = modifier)
            } else when (currentScreen) {
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
                is Screen.Profile -> ProfileScreen(
                    onCreateAccount = {
                        authStartWithCreate = true
                        currentRoute = Screen.Auth.route
                    },
                    onLogin = {
                        authStartWithCreate = false
                        currentRoute = Screen.Auth.route
                    },
                    onOpenNotes = { currentRoute = Screen.Notes.route },
                    modifier = modifier
                )
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
