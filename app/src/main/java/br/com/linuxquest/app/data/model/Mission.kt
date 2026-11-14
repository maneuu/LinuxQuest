package br.com.linuxquest.app.data.model

import com.google.firebase.firestore.DocumentId

data class Mission(
    @DocumentId
    val id: String = "",
    val lessonId: String = "",
    val title: String = "",
    val order: Int = 0,
    val xpReward: Int = 0,
    val steps: MissionSteps = MissionSteps()
)

data class MissionSteps(
    val scenario: ScenarioStep = ScenarioStep(),
    val learning: LearningStep = LearningStep(),
    val terminal: TerminalStep = TerminalStep(),
    val feedback: FeedbackStep = FeedbackStep()
)

data class ScenarioStep(
    val text: String = ""
)

data class LearningStep(
    val command: String = "",
    val text: String = ""
)

data class TerminalStep(
    val prompt: String = "user@linuxquest:~$",
    val instruction: String = "",
    val expectedCommand: String = "",
    val expectedOutput: String = "",
    val hint: String = "",
    val successMessage: String = ""
)

data class FeedbackStep(
    val text: String = "",
    val nextMissionId: String = ""
)
