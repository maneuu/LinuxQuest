# 06. Funcionamento da Aplicação — LinuxQuest

Detalhamento técnico dos principais fluxos de execução do aplicativo.

---

## ⚙️ Fluxos de Funcionamento

### 1. Inicialização do App (`MainActivity` & `FirestoreVerifier`)
- O aplicativo inicializa ativando o layout `edgeToEdge()`.
- Em background (`lifecycleScope.launch`), o `FirestoreVerifier.verifyAllCollections()` testa a conectividade com as 5 coleções e registra o resultado no Logcat sob a tag `LinuxQuestFirestore`.
- O `setContent` renderiza a `MainScreen` envolta pelo `LinuxQuestTheme`.

---

### 2. Navegação (`MainScreen` & `LinuxQuestBottomNavigation`)
- Controla a aba ativa com `rememberSaveable`.
- A barra inferior fixa exibe 4 abas: **Trilha**, **Dicionário**, **Progresso** e **Perfil**.
- Quando a rota ativa é `Screen.Mission.route`, a `MainScreen` alterna para `MissionScreen` e **oculta totalmente a Bottom Navigation bar**.
- O mesmo vale para `Screen.CommandDetail.route` (detalhes de um comando do Dicionário), que volta para a aba Dicionário pelo botão de voltar.

---

### 3. Renderização da Trilha (`TrackScreen` & `TrackViewModel`)
- O `TrackViewModel` consulta os níveis ativos (`LevelRepository.getLevels()`) e busca as lições de cada nível (`LessonRepository.getLessonsByLevelId()`).
- Mapeia o resultado para o estado `TrackUiState.Success(levelsWithLessons)`.
- Os nós das lições são posicionados horizontalmente de forma alternada (Centro, Direita, Centro, Esquerda).
- O componente `CurvedPathCanvas` desenha um caminho contínuo em curvas Bézier cúbicas (`cubicTo`) por trás dos botões das lições.

---

### 4. Execução de Missão (`MissionScreen` & `MissionViewModel`)
- Carrega a missão no Firestore correspondente ao `lessonId` selecionado.
- Apresenta as 4 etapas em fluxo sequencial (**Cenário** ➔ **Aprendizado** ➔ **Terminal** ➔ **Feedback**).
- No terminal, normaliza o comando inserido (`trim().replace(Regex("\\s+"), " ")`) e compara com `expectedCommand`. Ao acertar, simula a saída no terminal (`expectedOutput`) e permite prosseguir para o feedback final.

---

### 5. Dicionário de Comandos (`DictionaryScreen` & `DictionaryViewModel`)
- A busca consome a API REST do Mankier (`https://www.mankier.com/api/v2/`) via Retrofit (`MankierApi` / `DictionaryRepository`). O conteúdo da API é em inglês.
- O texto digitado passa por `debounce` de 400 ms antes de disparar a requisição `GET mans/?q=`.
- Os filtros (**Todos**, **Comandos**, **Admin**, **Config**, **Dev**) usam a seção do manual (`man 1`, `man 8`, `man 5`, `man 2/3`) e são aplicados localmente, sem nova requisição.
- A lista mostra apenas um resumo (nome, seção e descrição curta). Ao tocar num resultado, abre a `CommandDetailScreen`.
- Na tela de detalhes, o `CommandDetailViewModel` carrega a man page (`GET mans/{nome.seção}`), exibe os exemplos **TL;DR** num terminal estilizado e lista as seções do manual em cards expansíveis, carregados sob demanda (`GET mans/{nome.seção}/sections/{id}`).
