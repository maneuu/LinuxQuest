package br.com.linuxquest.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.linuxquest.app.ui.screens.profile.ProfileTopBar
import br.com.linuxquest.app.ui.screens.progress.AchievementProgress
import br.com.linuxquest.app.ui.screens.progress.LevelProgress
import br.com.linuxquest.app.ui.screens.progress.ProgressViewModel
import br.com.linuxquest.app.ui.theme.BorderColor
import br.com.linuxquest.app.ui.theme.DarkSurface
import br.com.linuxquest.app.ui.theme.JetBrainsMonoFontFamily
import br.com.linuxquest.app.ui.theme.PrimaryGreen
import br.com.linuxquest.app.ui.theme.TextPrimary
import br.com.linuxquest.app.ui.theme.TextSecondary

@Composable
fun ProgressScreen(
    modifier: Modifier = Modifier,
    viewModel: ProgressViewModel = viewModel()
) {
    // Recarrega toda vez que a aba é aberta
    LaunchedEffect(Unit) {
        viewModel.loadProgress()
    }

    Column(modifier = modifier.fillMaxSize()) {
        ProfileTopBar(title = "PROGRESSO")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "> sys_stat --metric=progresso",
                color = PrimaryGreen,
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 12.sp
            )
            Text(
                text = "Seu Progresso",
                color = TextPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Acompanhe sua evolução no Linux",
                color = TextSecondary,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            val error = viewModel.errorMessage
            if (!viewModel.isLoggedIn) {
                MessageCard("Entre na sua conta (aba Perfil) para salvar e ver seu progresso.")
            } else if (viewModel.isLoading) {
                CircularProgressIndicator(
                    color = PrimaryGreen,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else if (error != null) {
                MessageCard(error)
            } else {
                // Cartões de números
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        title = "XP total",
                        value = "${viewModel.xp} XP",
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Lições feitas",
                        value = "${viewModel.lessonsDone}",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                SectionHeader("Progresso por Nível")

                viewModel.levels.forEach { levelProgress ->
                    LevelCard(levelProgress)
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Spacer(modifier = Modifier.height(14.dp))
                SectionHeader("Conquistas")

                if (viewModel.achievements.isEmpty()) {
                    Text(text = "Nenhuma conquista cadastrada", color = TextSecondary)
                }
                viewModel.achievements.forEach { achievementProgress ->
                    AchievementCard(achievementProgress)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        color = TextPrimary,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

// Cartão escuro com borda (base dos outros cartões)
@Composable
private fun DarkCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = DarkSurface,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BorderColor)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
private fun MessageCard(text: String) {
    DarkCard {
        Text(text = text, color = TextSecondary, fontSize = 14.sp)
    }
}

@Composable
private fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    DarkCard(modifier = modifier) {
        Column {
            Text(text = title, color = TextSecondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun LevelCard(item: LevelProgress) {
    // Evita divisão por zero se o nível não tiver lições
    val percent = if (item.totalLessons > 0) item.doneLessons * 100 / item.totalLessons else 0

    val status = when {
        item.totalLessons > 0 && item.doneLessons == item.totalLessons -> "Concluído"
        item.doneLessons > 0 -> "Em andamento"
        else -> "Não iniciado"
    }

    DarkCard {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Nível ${item.level.order} - ${item.level.title}",
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = status,
                    color = if (percent == 100) PrimaryGreen else TextSecondary,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { percent / 100f },
                color = PrimaryGreen,
                trackColor = BorderColor,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${item.doneLessons}/${item.totalLessons} lições • $percent%",
                color = TextSecondary,
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun AchievementCard(item: AchievementProgress) {
    DarkCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (item.unlocked) Icons.Default.Star else Icons.Default.Lock,
                contentDescription = null,
                tint = if (item.unlocked) PrimaryGreen else TextSecondary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.achievement.name,
                    color = if (item.unlocked) TextPrimary else TextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = item.achievement.description,
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                // Conquista ainda bloqueada: mostra quanto falta
                if (!item.unlocked && item.achievement.requirement > 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { item.current.toFloat() / item.achievement.requirement },
                        color = PrimaryGreen,
                        trackColor = BorderColor,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            if (item.unlocked) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Desbloqueada",
                    tint = PrimaryGreen,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
