# LinuxQuest

> Aplicativo Android nativo focado no ensino gamificado de comandos do terminal Linux (Bash), utilizando uma trilha visual interativa e um terminal prático totalmente simulado.

---

## Funcionalidades

- **Trilha de aprendizado**: mapa de lições no estilo Duolingo, com níveis e lições carregados do Cloud Firestore.
- **Missões em 4 etapas**: Cenário ➔ Aprendizado ➔ Terminal Prático ➔ Feedback.
- **Terminal simulado**: o aluno digita o comando, o app valida e mostra a saída esperada. Nada é executado no dispositivo.
- **Dicionário de comandos**: busca com filtros por categoria (Todos, Admin, Config, Dev) consumindo a API REST do [Mankier](https://www.mankier.com/api) via Retrofit, com tela de detalhes (exemplos TL;DR e seções do manual).
- **Progresso e Perfil**: telas acessíveis pela barra de navegação inferior.

---

## Tecnologias

| Área | Tecnologia |
| :--- | :--- |
| Linguagem | Kotlin `2.2.10` |
| Interface | Jetpack Compose + Material 3 |
| Arquitetura | MVVM com ViewModel, StateFlow e Repositories |
| Banco de dados | Firebase Cloud Firestore |
| API REST | Retrofit `3.0.0` + Gson (API do Mankier) |
| Build | Gradle Kotlin DSL, AGP `9.3.3` |
| Plataforma | Min SDK `24` · Target SDK `37` |

---

## Como executar

### Pré-requisitos
- **Android Studio** estável recente (testado no 2026.2.2). Ele já traz o JDK 25 exigido pelo projeto.
- Emulador ou celular com **Android 7.0 (API 24)** ou superior.
- Arquivo **`google-services.json`** do Firebase do projeto.

### Passo a passo
1. Clone o repositório:
   ```bash
   git clone git@github.com:maneuu/LinuxQuest.git
   ```
2. Coloque o `google-services.json` dentro da pasta **`app/`**:
   ```
   LinuxQuest/app/google-services.json
   ```
   > **Atenção:** esse arquivo dá acesso ao banco, está no `.gitignore` e **não deve ser commitado**. Peça para alguém da equipe.
3. No Android Studio, vá em **File → Open** e selecione a pasta `LinuxQuest` (a pasta inteira, não um arquivo).
4. Aguarde o **Gradle Sync** terminar. Se o botão de Run ficar cinza, rode **File → Sync Project with Gradle Files**.
5. Escolha o emulador/dispositivo na barra superior e clique em **Run 'app'** (`Shift + F10`).

---

## Estrutura

```text
app/src/main/java/br/com/linuxquest/app/
├── data/
│   ├── model/        # data classes do Firestore e da API
│   ├── remote/       # FirestoreProvider e cliente Retrofit (api/)
│   └── repository/   # acesso a dados (Firestore e Mankier)
├── ui/
│   ├── navigation/   # rotas, MainScreen e barra inferior
│   ├── screens/      # telas Compose + ViewModels/UiStates
│   └── theme/        # cores, tipografia e tema escuro
└── util/             # extensões e verificação do Firestore
```

---

## Documentação

A documentação completa está na pasta [`docs/`](docs):

| Documento | Conteúdo |
| :--- | :--- |
| [01 · Visão geral](docs/01-visao-geral.md) | Proposta, objetivos e funcionalidades |
| [02 · Tecnologias](docs/02-tecnologias.md) | Stack e bibliotecas |
| [03 · Arquitetura](docs/03-arquitetura.md) | Árvore de diretórios e responsabilidade dos pacotes |
| [04 · Banco de dados](docs/04-banco-de-dados.md) | Coleções do Firestore e exemplos |
| [05 · Diagramas](docs/05-diagramas.md) | ER, arquitetura e fluxo da missão |
| [06 · Funcionamento](docs/06-funcionamento.md) | Fluxo de cada tela |
| [07 · Design e UI](docs/07-design-e-ui.md) | Paleta, tipografia e componentes |
| [08 · Segurança e regras](docs/08-seguranca-e-regras.md) | Terminal simulado e credenciais |
| [09 · Como executar](docs/09-como-executar.md) | Configuração do ambiente |
| [10 · Status do projeto](docs/10-status-do-projeto.md) | O que está pronto e o que falta |

