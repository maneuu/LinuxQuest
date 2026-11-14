# 04. Banco de Dados (Firestore) — LinuxQuest

O aplicativo utiliza o **Firebase Cloud Firestore** (banco de dados NoSQL) estruturado em cinco coleções principais.

---

## 🗄️ Coleções do Banco de Dados

### 1. Coleção `levels`
Representa as seções ou níveis principais da trilha de aprendizado.
- `id` (String): ID do documento no Firestore (ex: `"level_01"`).
- `title` (String): Título do nível.
- `description` (String): Descrição curta.
- `order` (Int): Número para ordenação da trilha.
- `requiredXp` (Int): Quantidade de XP exigida.
- `totalLessons` (Int): Total de lições associadas ao nível.
- `active` (Boolean): Flag de habilitação do nível.

*Exemplo JSON Document (`levels/level_01`):*
```json
{
  "title": "Primeiros Passos no Terminal",
  "description": "Aprenda a se orientar no ambiente de linha de comando.",
  "order": 1,
  "requiredXp": 0,
  "totalLessons": 3,
  "active": true
}
```

---

### 2. Coleção `lessons`
Representa as lições pertencentes a um nível.
- `id` (String): ID do documento (ex: `"lesson_01"`).
- `levelId` (String): ID do nível pai (chave estrangeira atrelada a `levels`).
- `title` (String): Título da lição.
- `description` (String): Breve explicação da lição.
- `order` (Int): Ordem de exibição dentro do nível.

*Exemplo JSON Document (`lessons/lesson_01`):*
```json
{
  "levelId": "level_01",
  "title": "whoami & pwd",
  "description": "Identificação de usuário e diretório atual",
  "order": 1
}
```

---

### 3. Coleção `missions`
Representa o desafio interativo da lição.
- `id` (String): ID do documento (ex: `"mission_01"`).
- `lessonId` (String): ID da lição pai (atrelada a `lessons`).
- `title` (String): Título da missão.
- `order` (Int): Ordem da missão na lição.
- `xpReward` (Int): Pontos de XP concedidos.
- `steps` (Objeto com 4 etapas):
  - `scenario.text`: Contexto do cenário.
  - `learning.command`: Comando principal ensinado.
  - `learning.text`: Explicação teórica do comando.
  - `terminal.prompt`: Prompt customizado simulado.
  - `terminal.instruction`: Instrução do exercício no terminal.
  - `terminal.expectedCommand`: Comando exato esperado para validação.
  - `terminal.expectedOutput`: Saída que será exibida ao acertar o comando.
  - `terminal.hint`: Dica pedagógica opcional.
  - `terminal.successMessage`: Mensagem de confirmação de sucesso.
  - `feedback.text`: Explicação de fechamento pedagógico.
  - `feedback.nextMissionId`: ID da próxima missão a carregar (ou `""` para finalizar o nível).

*Exemplo JSON Document (`missions/mission_01`):*
```json
{
  "lessonId": "lesson_01",
  "title": "Identificando o Usuário Atual",
  "order": 1,
  "xpReward": 20,
  "steps": {
    "scenario": {
      "text": "Você acessou um servidor Linux remoto via SSH e precisa verificar qual usuário está conectado na sessão ativa antes de executar tarefas do sistema."
    },
    "learning": {
      "command": "whoami",
      "text": "O comando whoami imprime no terminal o nome do usuário atualmente autenticado na sessão."
    },
    "terminal": {
      "prompt": "user@linuxquest:~$ ",
      "instruction": "Digite o comando que exibe o nome do usuário ativo na sessão atual.",
      "expectedCommand": "whoami",
      "expectedOutput": "linuxuser",
      "hint": "O comando significa 'who am i' sem espaços.",
      "successMessage": "Excelente! O terminal confirmou a identidade do usuário logado."
    },
    "feedback": {
      "text": "Você aprendeu a utilizar o comando whoami para identificar o usuário ativo no sistema Linux.",
      "nextMissionId": ""
    }
  }
}
```

---

### 4. Coleção `commands`
Utilizada para buscas no Dicionário de comandos Linux.
- `id` (String): ID do documento (ex: `"pwd"`).
- `name` (String): Nome do comando.
- `category` (String): Categoria do comando (ex: `"Navegação"`).
- `description` (String): Explicação técnica detalhada.
- `syntax` (String): Sintaxe de utilização.
- `example` (String): Exemplo prático de uso.

---

### 5. Coleção `achievements`
Contém as definições de conquistas.
- `id` (String): ID do documento (ex: `"first_mission"`).
- `name` (String): Nome da conquista.
- `description` (String): Texto explicativo do objetivo.
- `requirement` (Int): Quantidade exigida para desbloqueio.
- `type` (String): Tipo da conquista (ex: `"mission_count"`).
