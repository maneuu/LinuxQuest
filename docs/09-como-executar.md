# 09. Como Executar — LinuxQuest

Instruções para compilar e executar o projeto LinuxQuest em seu ambiente local.

---

## 📋 Pré-requisitos

1. **Android Studio**: versão estável recente (testado no 2026.2.2). Versões antigas, como Ladybug, não suportam o AGP `9.3.3` usado no projeto.
2. **JDK**: JDK 25 (definido em `gradle/gradle-daemon-jvm.properties`). O JDK embutido no Android Studio já atende.
3. **Dispositivo/Emulador**: Dispositivo físico ou AVD rodando Android API 24 (Android 7.0) ou superior.
4. **Arquivo Firebase**: `google-services.json` válido posicionado em `LinuxQuest/app/google-services.json`. O arquivo está no `.gitignore` e é compartilhado fora do repositório.
5. **Internet**: necessária para o Firestore e para a API do Mankier (Dicionário).

---

## ⚙️ Passos de Configuração

1. Clone ou abra a pasta do repositório no **Android Studio**.
2. Aguarde a indexação e sincronização inicial do Gradle.
3. Se necessário, execute o Sync do Gradle manualmente (**File > Sync Project with Gradle Files**).
4. Selecione a configuração de execução **`app`** na barra superior.
5. Clique no botão **Run ('app')** ou pressione `Shift + F10`.
