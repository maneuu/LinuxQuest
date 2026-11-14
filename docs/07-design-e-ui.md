# 07. Design & UI — LinuxQuest

O LinuxQuest utiliza um **Tema Escuro** centralizado construído sobre as diretrizes do Material Design 3.

---

## 🎨 Paleta de Cores (`ui/theme/Color.kt`)

| Nome da Cor | Código HEX | Utilização |
| :--- | :--- | :--- |
| **DarkBackground** | `#10141A` | Fundo principal de todas as telas. |
| **DarkSurface** | `#161B22` | Cards, superfícies e container da barra inferior. |
| **DarkSurfaceElevated** | `#1C2026` | Badges, cabeçalhos do terminal e itens elevados. |
| **PrimaryGreen** | `#22C55E` | Botões primários, nós da trilha e textos de destaque. |
| **TextPrimary** | `#F0F6FC` | Títulos e texto principal de alta legibilidade. |
| **TextSecondary** | `#8B949E` | Subtítulos, rótulos e itens inativos da navegação. |
| **BorderColor** | `#30363D` | Bordas de cards, divisores e borda dos botões. |

---

## 🔤 Tipografia (`ui/theme/Type.kt`)

- **Interface Geral**: Inter (`FontFamily.Default`) em estilos `bodyLarge`, `bodyMedium`, `titleLarge` e `labelMedium`.
- **Terminal & Comandos**: JetBrains Mono (`FontFamily.Monospace`) em `TechnicalTextStyle` para sintaxe de comandos, prompts e trechos de código.

---

## 🧩 Componentes Visuais Destacados

1. **Barra de Navegação Inferior (`LinuxQuestBottomNavigation`)**:
   - Fundo `#161B22` com borda superior `#30363D`.
   - Item selecionado com ícone e texto em verde `#22C55E`.
2. **Caminho Curvado da Trilha (`CurvedPathCanvas`)**:
   - Desenho vetorial em `Canvas` com curvas Bézier cúbicas (`cubicTo`), extremidades arredondadas e efeito de brilho (*glow*) em camadas.
3. **Janela Simulada de Terminal**:
   - Container escuro estilo janela Bash com barra de botões coloridos de controle (vermelho, amarelo e verde).
