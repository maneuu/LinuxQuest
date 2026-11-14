# 05. Diagramas — LinuxQuest

Diagramas explicativos em Mermaid representando o modelo de dados, arquitetura e fluxo de exercícios.

---

## 📊 1. Diagrama de Entidade-Relacionamento (ER)

```mermaid
erDiagram
    LEVELS ||--|{ LESSONS : "contém (levelId)"
    LESSONS ||--|{ MISSIONS : "contém (lessonId)"
    MISSIONS ||--o| MISSIONS : "encadeia (nextMissionId)"
    COMMANDS {
        string id PK
        string name
        string category
        string syntax
    }
    ACHIEVEMENTS {
        string id PK
        string name
        string type
    }
```

---

## 🏛️ 2. Diagrama de Arquitetura do Sistema

```mermaid
graph TD
    UI[Jetpack Compose UI] -->|Observa UiState via StateFlow| VM[ViewModels]
    VM -->|Executa rotinas em Coroutines| REPO[Repositories]
    REPO -->|Converter Task para Suspend| UTIL[TaskExtensions.awaitTask]
    UTIL -->|Acessa Firestore Singleton| FP[FirestoreProvider]
    FP -->|Consulta Leitura NoSQL| FS[(Firebase Cloud Firestore)]
```

---

## 🔄 3. Diagrama de Fluxo de uma Missão

```mermaid
flowchart TD
    A[Usuário clica em uma Lição na Trilha] --> B[MissionViewModel carrega Missão do Firestore]
    B --> C[Etapa 1: Cenário]
    C -->|Clique em Avançar| D[Etapa 2: Aprendizado]
    D -->|Clique em Ir para o Terminal| E[Etapa 3: Terminal Prático Simulado]
    E -->|Digita comando e Clica em Executar| F{Comando Normalizado == expectedCommand?}
    F -- Não --> G[Exibe Mensagem de Erro e Dica]
    G --> E
    F -- Sim --> H[Exibe Saída Esperada & Mensagem de Sucesso]
    H -->|Clique em Continuar| I[Etapa 4: Feedback]
    I --> J{Possui nextMissionId?}
    J -- Sim --> K[Carrega Próxima Missão]
    K --> C
    J -- Não --> L[Conclui o Nível e Retorna à Trilha]
```
