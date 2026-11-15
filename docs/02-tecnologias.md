# 02. Tecnologias & Bibliotecas — LinuxQuest

O LinuxQuest é desenvolvido utilizando as tecnologias mais modernas recomendadas para o ecossistema Android nativo.

---

## 🛠️ Stack Tecnológica

| Tecnologia / Biblioteca | Versão / Ref | Função na Arquitetura |
| :--- | :--- | :--- |
| **Kotlin** | `2.2.10` | Linguagem de programação nativa do projeto. |
| **Android SDK** | Min SDK `24` / Target SDK `37` | Plataforma nativa Android. |
| **Jetpack Compose** | Compose BOM `2026.02.01` | Kit de ferramentas declarativo para construção de interfaces gráficas. |
| **Material 3** | Material 3 Jetpack | Sistema de design para componentes, cores, botões e superfícies. |
| **Material Icons Core** | Native Compose | Conjunto de ícones vetoriais do Material Design. |
| **ViewModel & StateFlow** | Lifecycle `2.11.0` | Arquitetura de UI reativa com retenção de estado durante mudanças de rota/orientação. |
| **Kotlin Coroutines** | via dependências do Lifecycle | Execução de tarefas assíncronas. A conversão de `Task` do Firebase para `suspend` é feita manualmente em `TaskExtensions.awaitTask()`. |
| **Firebase Cloud Firestore** | Firebase BOM `35.2.0` | Banco de dados NoSQL para níveis, lições, missões, comandos e conquistas. |
| **Retrofit + Gson** | Retrofit `3.0.0` | Cliente HTTP para consumir a API REST do Mankier (dicionário de comandos). |
| **Firebase Authentication** | Firebase BOM `35.2.0` | Dependência configurada no Gradle, ainda não utilizada no código. |
| **Gradle Kotlin DSL** | AGP `9.3.3` | Gerenciamento de build e dependências da aplicação. |
