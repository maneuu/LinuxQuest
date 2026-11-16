package br.com.linuxquest.app.ui.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.linuxquest.app.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    // true = aba "Criar Conta", false = aba "Já tenho conta"
    var isCreatingAccount by mutableStateOf(true)

    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun changeTab(createAccount: Boolean) {
        isCreatingAccount = createAccount
        errorMessage = null
    }

    fun submit(onSuccess: () -> Unit) {
        // Validações simples antes de chamar o Firebase
        if (isCreatingAccount && username.isBlank()) {
            errorMessage = "Digite um identificador"
            return
        }
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Preencha e-mail e senha"
            return
        }
        if (password.length < 6) {
            errorMessage = "A senha precisa ter pelo menos 6 caracteres"
            return
        }

        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            try {
                if (isCreatingAccount) {
                    authRepository.createAccount(username.trim(), email.trim(), password)
                } else {
                    authRepository.login(email.trim(), password)
                }
                password = ""
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Algo deu errado, tente novamente"
            }
            isLoading = false
        }
    }
}
