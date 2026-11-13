package br.com.linuxquest.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector,
) {
    object Track : Screen("trilha", "Trilha", Icons.AutoMirrored.Filled.List)
    object Practice : Screen("praticar", "Praticar", Icons.Default.PlayArrow)
    object Progress : Screen("progresso", "Progresso", Icons.Default.Star)
    object Profile : Screen("perfil", "Perfil", Icons.Default.Person)

    companion object {
        val items: List<Screen>
            get() = listOf(Track, Practice, Progress, Profile)
    }
}
