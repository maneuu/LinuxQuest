# 08. Segurança & Regras de Negócio — LinuxQuest

Regras de desenvolvimento e medidas de segurança aplicadas ao aplicativo.

---

## 🛡️ Regras de Segurança e Execução

1. **Terminal 100% Simulado**:
   - O terminal interativo do aplicativo é **uma simulação visual gráfica**.
   - Nenhum comando é repassado ao shell nativo do dispositivo Android.
   - Todo o comportamento é controlado pela lógica de comparação de strings no `MissionViewModel`.

2. **Normalização de Comandos**:
   - A validação de comandos no terminal desconsidera espaços adicionais nas extremidades ou múltiplos espaços entre argumentos:
     ```kotlin
     private fun normalizeCommand(command: String): String {
         return command.trim().replace(Regex("\\s+"), " ")
     }
     ```

3. **Proteção de Credenciais**:
   - Credenciais e chaves do Firebase são mantidas no arquivo `google-services.json` configurado no diretório da aplicação.
   - Nenhuma chave secreta ou privada é exposta no código-fonte Kotlin ou na documentação.
