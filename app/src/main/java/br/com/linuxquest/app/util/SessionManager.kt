package br.com.linuxquest.app.util

// Guarda quem está logado enquanto o app está aberto.
// userId = id do documento do usuário na coleção "users" (ex: "user_001").
// null = visitante (ninguém logado).
object SessionManager {
    var userId: String? = null
}
