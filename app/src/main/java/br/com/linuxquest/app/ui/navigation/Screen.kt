package br.com.linuxquest.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector,
) {
    object Track : Screen("trilha", "Trilha", Icons.AutoMirrored.Filled.List)
    object Dictionary : Screen("dicionario", "Dicionário", Icons.Default.Search)
    object Progress : Screen("progresso", "Progresso", Icons.Default.Star)
    object Profile : Screen("perfil", "Perfil", Icons.Default.Person)
    object Mission : Screen("missao", "Missão", Icons.Default.Close)
    object CommandDetail : Screen("comando", "Comando", Icons.Default.Search)

    companion object {
        // Apenas as 4 abas visíveis na Bottom Navigation
        val items: List<Screen>
            get() = listOf(Track, Dictionary, Progress, Profile)
    }
}
