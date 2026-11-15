# 03. Arquitetura & Organização dos Pacotes — LinuxQuest

O LinuxQuest adota a arquitetura recomendada pelo Android (Clean Architecture simplificada com separação de responsabilidades entre as camadas de **Dados** e **Interface**).

---

## 📂 Árvore de Diretórios do Projeto

```text
LinuxQuest/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/br/com/linuxquest/app/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── data/
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── Achievement.kt
│   │   │   │   │   │   ├── Command.kt
│   │   │   │   │   │   ├── Lesson.kt
│   │   │   │   │   │   ├── Level.kt
│   │   │   │   │   │   ├── ManPage.kt
│   │   │   │   │   │   └── Mission.kt
│   │   │   │   │   ├── remote/
│   │   │   │   │   │   ├── FirestoreProvider.kt
│   │   │   │   │   │   └── api/
│   │   │   │   │   │       ├── MankierApi.kt
│   │   │   │   │   │       └── RetrofitProvider.kt
│   │   │   │   │   └── repository/
│   │   │   │   │       ├── AchievementRepository.kt
│   │   │   │   │       ├── CommandRepository.kt
│   │   │   │   │       ├── DictionaryRepository.kt
│   │   │   │   │       ├── LessonRepository.kt
│   │   │   │   │       ├── LevelRepository.kt
│   │   │   │   │       └── MissionRepository.kt
│   │   │   │   ├── ui/
│   │   │   │   │   ├── navigation/
│   │   │   │   │   │   ├── LinuxQuestBottomNavigation.kt
│   │   │   │   │   │   ├── MainScreen.kt
│   │   │   │   │   │   └── Screen.kt
│   │   │   │   │   ├── screens/
│   │   │   │   │   │   ├── CommandDetailScreen.kt
│   │   │   │   │   │   ├── DictionaryScreen.kt
│   │   │   │   │   │   ├── MissionScreen.kt
│   │   │   │   │   │   ├── ProfileScreen.kt
│   │   │   │   │   │   ├── ProgressScreen.kt
│   │   │   │   │   │   ├── TrackScreen.kt
│   │   │   │   │   │   ├── command/
│   │   │   │   │   │   │   ├── CommandDetailUiState.kt
│   │   │   │   │   │   │   └── CommandDetailViewModel.kt
│   │   │   │   │   │   ├── dictionary/
│   │   │   │   │   │   │   ├── DictionaryUiState.kt
│   │   │   │   │   │   │   └── DictionaryViewModel.kt
│   │   │   │   │   │   ├── mission/
│   │   │   │   │   │   │   ├── MissionUiState.kt
│   │   │   │   │   │   │   └── MissionViewModel.kt
│   │   │   │   │   │   └── track/
│   │   │   │   │   │       ├── TrackUiState.kt
│   │   │   │   │   │       └── TrackViewModel.kt
│   │   │   │   │   └── theme/
│   │   │   │   │       ├── Color.kt
│   │   │   │   │       ├── Theme.kt
│   │   │   │   │       └── Type.kt
│   │   │   │   └── util/
│   │   │   │       ├── FirestoreVerifier.kt
│   │   │   │       └── TaskExtensions.kt
│   │   │   └── google-services.json
│   │   └── build.gradle.kts
│   └── build.gradle.kts
└── settings.gradle.kts
```

---

## 🏛️ Responsabilidade dos Pacotes

- **`data/model`**: Contém as `data classes` Kotlin atreladas às coleções do Firestore (`@DocumentId` e construtores padrão).
- **`data/remote`**: Provedor singleton (`FirestoreProvider`) com a instância do `FirebaseFirestore` e constantes das coleções.
- **`data/remote/api`**: Interface Retrofit (`MankierApi`) e provedor (`RetrofitProvider`) da API REST do Mankier, usada pelo Dicionário.
- **`data/repository`**: Camada de repositórios assíncronos (`LevelRepository`, `LessonRepository`, `MissionRepository`, `CommandRepository`, `AchievementRepository`).
- **`ui/navigation`**: Controle de rotas (`Screen`), componente `LinuxQuestBottomNavigation` e container principal `MainScreen`.
- **`ui/screens`**: Telas e ViewModels em Jetpack Compose (`TrackScreen`, `MissionScreen`, `DictionaryScreen`, `ProgressScreen`, `ProfileScreen`).
- **`ui/theme`**: Centralização de estilo (`Color.kt`, `Theme.kt`, `Type.kt`).
- **`util`**: Utilitários como `TaskExtensions.kt` (conversão de `Task` para Coroutine) e `FirestoreVerifier.kt`.
