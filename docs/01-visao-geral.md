# 01. Visão Geral do Projeto — LinuxQuest

O **LinuxQuest** é um aplicativo mobile nativo Android projetado para tornar o aprendizado de comandos da linha de comando Linux (terminal Bash) simples, intuitivo e prático.

---

## 🎯 Proposta e Objetivos

Muitos estudantes e profissionais iniciantes enfrentam dificuldades para aprender comandos de terminal devido à falta de interatividade e ao receio de danificar sistemas reais. O LinuxQuest resolve esse problema ao combinar:

1. **Aprendizado Gamificado**: Uma trilha visual no estilo mapa de jogo (inspirada no Duolingo) que guia o aluno passo a passo por níveis progressivos de dificuldade.
2. **Terminal Totalmente Simulado**: Um ambiente prático de digitação e execução de comandos que valida a sintaxe do usuário em tempo real sem qualquer risco ao dispositivo.
3. **Ensino em Etapas Lógicas**: Missões divididas em 4 fases sequenciais (**Cenário** ➔ **Aprendizado** ➔ **Terminal Prático** ➔ **Feedback**).

---

## ⭐ Funcionalidades Principais

- **Trilha de Aprendizado Visual**: Mapa de nós de lições conectados por curvas Bézier fluida em gradiente verde.
- **Integração em Tempo Real com Cloud Firestore**: Leitura dinâmica de níveis, lições, missões, comandos e conquistas diretamente do banco NoSQL.
- **Terminal Interativo Simulado**: Suporte a prompt customizado, campo de digitação, validação de sintaxe (com remoção automática de espaços extras) e exibição de saída simulada.
- **Dicionário de Comandos**: Busca de comandos com filtros por categoria, consumindo a API REST do Mankier via Retrofit, e tela de detalhes com exemplos e seções do manual.
- **Barra de Navegação Inferior**: Acesso rápido às abas **Trilha**, **Dicionário**, **Progresso** e **Perfil**, com ocultamento automático durante a realização de uma missão.
- **Verificação Automática no Startup**: Teste de conectividade com as coleções do Firestore ao iniciar a aplicação.
