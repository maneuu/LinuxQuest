package br.com.linuxquest.app.util

import android.util.Log
import br.com.linuxquest.app.data.repository.AchievementRepository
import br.com.linuxquest.app.data.repository.CommandRepository
import br.com.linuxquest.app.data.repository.LessonRepository
import br.com.linuxquest.app.data.repository.LevelRepository
import br.com.linuxquest.app.data.repository.MissionRepository

object FirestoreVerifier {
    private const val TAG = "LinuxQuestFirestore"

    suspend fun verifyAllCollections() {
        Log.d(TAG, "=== Iniciando verificação do Firestore ===")

        val levelRepo = LevelRepository()
        val lessonRepo = LessonRepository()
        val missionRepo = MissionRepository()
        val commandRepo = CommandRepository()
        val achievementRepo = AchievementRepository()

        // 1. Levels
        levelRepo.getLevels().fold(
            onSuccess = { levels ->
                Log.d(TAG, "[LEVELS] Sucesso! ${levels.size} níveis encontrados:")
                levels.forEach { level ->
                    Log.d(TAG, "   - ID: ${level.id} | Título: ${level.title} | Ordem: ${level.order} | Ativo: ${level.active}")
                }
            },
            onFailure = { error ->
                Log.e(TAG, "[LEVELS] Erro ao consultar coleção: ${error.message}", error)
            }
        )

        // 2. Lessons
        lessonRepo.getLessonsByLevelId("level_01").fold(
            onSuccess = { lessons ->
                Log.d(TAG, "[LESSONS] Sucesso! ${lessons.size} lições encontradas para level_01:")
                lessons.forEach { lesson ->
                    Log.d(TAG, "   - ID: ${lesson.id} | LevelID: ${lesson.levelId} | Título: ${lesson.title}")
                }
            },
            onFailure = { error ->
                Log.e(TAG, "[LESSONS] Erro ao consultar coleção: ${error.message}", error)
            }
        )

        // 3. Missions
        missionRepo.getMissionsByLessonId("lesson_01").fold(
            onSuccess = { missions ->
                Log.d(TAG, "[MISSIONS] Sucesso! ${missions.size} missões encontradas para lesson_01:")
                missions.forEach { mission ->
                    Log.d(TAG, "   - ID: ${mission.id} | LessonID: ${mission.lessonId} | Título: ${mission.title} | XP: ${mission.xpReward}")
                }
            },
            onFailure = { error ->
                Log.e(TAG, "[MISSIONS] Erro ao consultar coleção: ${error.message}", error)
            }
        )

        // 4. Commands
        commandRepo.getCommands().fold(
            onSuccess = { commands ->
                Log.d(TAG, "[COMMANDS] Sucesso! ${commands.size} comandos encontrados:")
                commands.forEach { command ->
                    Log.d(TAG, "   - ID: ${command.id} | Nome: ${command.name} | Categoria: ${command.category}")
                }
            },
            onFailure = { error ->
                Log.e(TAG, "[COMMANDS] Erro ao consultar coleção: ${error.message}", error)
            }
        )

        // 5. Achievements
        achievementRepo.getAchievements().fold(
            onSuccess = { achievements ->
                Log.d(TAG, "[ACHIEVEMENTS] Sucesso! ${achievements.size} conquistas encontradas:")
                achievements.forEach { achievement ->
                    Log.d(TAG, "   - ID: ${achievement.id} | Nome: ${achievement.name} | Tipo: ${achievement.type}")
                }
            },
            onFailure = { error ->
                Log.e(TAG, "[ACHIEVEMENTS] Erro ao consultar coleção: ${error.message}", error)
            }
        )

        Log.d(TAG, "=== Verificação concluída ===")
    }
}
