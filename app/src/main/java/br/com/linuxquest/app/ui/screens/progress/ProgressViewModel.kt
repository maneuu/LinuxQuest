package br.com.linuxquest.app.ui.screens.progress

import br.com.linuxquest.app.util.SessionManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.linuxquest.app.data.model.Achievement
import br.com.linuxquest.app.data.model.Level
import br.com.linuxquest.app.data.repository.AchievementRepository
import br.com.linuxquest.app.data.repository.LessonRepository
import br.com.linuxquest.app.data.repository.LevelRepository
import br.com.linuxquest.app.data.repository.ProgressRepository
import kotlinx.coroutines.launch

// Um nível com quantas lições o usuário já fez nele
data class LevelProgress(
    val level: Level,
    val doneLessons: Int,
    val totalLessons: Int
)

// Uma conquista e quanto falta para desbloquear
data class AchievementProgress(
    val achievement: Achievement,
    val current: Int
) {
    val unlocked: Boolean
        get() = current >= achievement.requirement
}

class ProgressViewModel : ViewModel() {

    private val progressRepository = ProgressRepository()
    private val levelRepository = LevelRepository()
    private val lessonRepository = LessonRepository()
    private val achievementRepository = AchievementRepository()

    var isLoggedIn by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    var xp by mutableIntStateOf(0)
    var lessonsDone by mutableIntStateOf(0)
    var levels by mutableStateOf<List<LevelProgress>>(emptyList())
    var achievements by mutableStateOf<List<AchievementProgress>>(emptyList())

    fun loadProgress() {
        isLoggedIn = SessionManager.userId != null
        if (!isLoggedIn) return

        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            try {
                val progress = progressRepository.getProgress()
                xp = progress.xp
                lessonsDone = progress.completedLessons.size

                // Para cada nível, conta quantas das suas lições já foram feitas
                val levelList = levelRepository.getLevels().getOrThrow()
                levels = levelList.map { level ->
                    val lessons = lessonRepository.getLessonsByLevelId(level.id).getOrDefault(emptyList())
                    val done = lessons.count { progress.completedLessons.contains(it.id) }
                    LevelProgress(level, done, lessons.size)
                }

                // Conquistas: compara o requisito com o que o usuário já tem
                val achievementList = achievementRepository.getAchievements().getOrDefault(emptyList())
                achievements = achievementList.map { achievement ->
                    val current = when (achievement.type) {
                        "missions_completed", "mission_count" -> progress.completedMissions.size
                        "lessons_completed", "lesson_count" -> progress.completedLessons.size
                        "xp", "xp_total" -> progress.xp
                        else -> 0
                    }
                    AchievementProgress(achievement, current)
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Erro ao carregar progresso"
            }
            isLoading = false
        }
    }
}
