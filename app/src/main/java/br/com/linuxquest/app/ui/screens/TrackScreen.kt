package br.com.linuxquest.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.linuxquest.app.data.model.Lesson
import br.com.linuxquest.app.ui.screens.track.LevelWithLessons
import br.com.linuxquest.app.ui.screens.track.TrackUiState
import br.com.linuxquest.app.ui.screens.track.TrackViewModel
import br.com.linuxquest.app.ui.theme.BorderColor
import br.com.linuxquest.app.ui.theme.DarkSurface
import br.com.linuxquest.app.ui.theme.DarkSurfaceElevated
import br.com.linuxquest.app.ui.theme.JetBrainsMonoFontFamily
import br.com.linuxquest.app.ui.theme.PrimaryGreen
import br.com.linuxquest.app.ui.theme.TextPrimary
import br.com.linuxquest.app.ui.theme.TextSecondary

@Composable
fun TrackScreen(
    onOpenMission: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TrackViewModel = remember { TrackViewModel() }
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is TrackUiState.Loading -> LoadingView()
            is TrackUiState.Error -> ErrorView(
                message = state.message,
                onRetry = { viewModel.loadTrackData() }
            )
            is TrackUiState.Empty -> EmptyView(
                onRetry = { viewModel.loadTrackData() }
            )
            is TrackUiState.Success -> TrackContentView(
                levelsWithLessons = state.levelsWithLessons,
                onLessonClick = { _ ->
                    onOpenMission()
                }
            )
        }
    }
}

@Composable
private fun TrackContentView(
    levelsWithLessons: List<LevelWithLessons>,
    onLessonClick: (Lesson) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            HeaderBanner()
            Spacer(modifier = Modifier.height(24.dp))
        }

        levelsWithLessons.forEach { levelWithLessons ->
            item(key = "level_${levelWithLessons.level.id}") {
                LevelHeaderSection(levelWithLessons = levelWithLessons)
                Spacer(modifier = Modifier.height(20.dp))
            }

            itemsIndexed(
                items = levelWithLessons.lessons,
                key = { _, lesson -> "lesson_${lesson.id}" }
            ) { index, lesson ->
                val currentOffset = calculateOffset(index)
                val nextOffset = if (index < levelWithLessons.lessons.size - 1) {
                    calculateOffset(index + 1)
                } else null

                LessonNodeItem(
                    lesson = lesson,
                    currentOffset = currentOffset,
                    nextOffset = nextOffset,
                    onClick = { onLessonClick(lesson) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

private fun calculateOffset(index: Int): Dp {
    return when (index % 4) {
        0 -> 0.dp
        1 -> 52.dp
        2 -> 0.dp
        else -> (-52).dp
    }
}

@Composable
private fun HeaderBanner() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = DarkSurface,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(PrimaryGreen)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "TRILHA ATIVA",
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Trilha de Aprendizado",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Aprenda comandos Linux passo a passo",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun LevelHeaderSection(levelWithLessons: LevelWithLessons) {
    val level = levelWithLessons.level
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            color = DarkSurfaceElevated,
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "NÍVEL ${level.order} · ${level.title.uppercase()}",
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        }

        if (level.description.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = level.description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun LessonNodeItem(
    lesson: Lesson,
    currentOffset: Dp,
    nextOffset: Dp?,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Círculo do Nó + Rótulo da Lição
        Column(
            modifier = Modifier.offset(x = currentOffset),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(PrimaryGreen)
                    .border(3.dp, BorderColor, CircleShape)
                    .clickable { onClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = lesson.title,
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = lesson.title,
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(130.dp)
            )

            if (lesson.description.isNotEmpty()) {
                Text(
                    text = lesson.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(140.dp)
                )
            }
        }

        // Desenha a linha curvada em S conectando o nó atual ao próximo nó
        if (nextOffset != null) {
            CurvedPathCanvas(
                startOffset = currentOffset,
                endOffset = nextOffset,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CurvedPathCanvas(
    startOffset: Dp,
    endOffset: Dp,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerX = width / 2f

        val startX = centerX + startOffset.toPx()
        val startY = 0f

        val endX = centerX + endOffset.toPx()
        val endY = height

        val controlY1 = startY + height * 0.55f
        val controlY2 = endY - height * 0.55f

        val path = Path().apply {
            moveTo(startX, startY)
            cubicTo(
                startX, controlY1,
                endX, controlY2,
                endX, endY
            )
        }

        // Gradiente suave de verde (#22C55E) para tons ligeiramente mais escuros
        val strokeGradient = Brush.verticalGradient(
            colors = listOf(
                PrimaryGreen,
                PrimaryGreen.copy(alpha = 0.85f),
                PrimaryGreen.copy(alpha = 0.65f)
            ),
            startY = 0f,
            endY = height
        )

        // Camada 1: Glow verde translúcido exterior (brilho sutil)
        drawPath(
            path = path,
            color = PrimaryGreen.copy(alpha = 0.18f),
            style = Stroke(
                width = 14.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Camada 2: Glow médio
        drawPath(
            path = path,
            color = PrimaryGreen.copy(alpha = 0.35f),
            style = Stroke(
                width = 9.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Camada 3: Núcleo principal da curva com gradiente verde
        drawPath(
            path = path,
            brush = strokeGradient,
            style = Stroke(
                width = 4.5.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

@Composable
private fun LoadingView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = PrimaryGreen,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Carregando trilha...",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )
    }
}

@Composable
private fun ErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Erro ao carregar dados",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryGreen,
                contentColor = Color.Black
            )
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Tentar novamente")
        }
    }
}

@Composable
private fun EmptyView(
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Nenhum nível ou lição cadastrada.",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Verifique o cadastro de documentos no Firestore.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryGreen,
                contentColor = Color.Black
            )
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Recarregar")
        }
    }
}
