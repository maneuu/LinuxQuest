package br.com.linuxquest.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.linuxquest.app.ui.screens.profile.AuthViewModel
import br.com.linuxquest.app.ui.theme.BorderColor
import br.com.linuxquest.app.ui.theme.DarkBackground
import br.com.linuxquest.app.ui.theme.DarkSurface
import br.com.linuxquest.app.ui.theme.JetBrainsMonoFontFamily
import br.com.linuxquest.app.ui.theme.PrimaryGreen
import br.com.linuxquest.app.ui.theme.TextPrimary
import br.com.linuxquest.app.ui.theme.TextSecondary

@Composable
fun AuthScreen(
    startWithCreateAccount: Boolean,
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    // Abre na aba certa (Criar Conta ou Entrar)
    LaunchedEffect(startWithCreateAccount) {
        viewModel.changeTab(startWithCreateAccount)
    }

    var showPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .statusBarsPadding()
    ) {
        // Barra do topo com botão de voltar
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = TextPrimary
                )
            }
            Text(
                text = "Sessão Do Terminal",
                color = TextPrimary,
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 13.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = if (viewModel.isCreatingAccount) "Crie sua Conta" else "Bem-vindo de volta",
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Conecte-se para desbloquear todas as trilhas, salvar progresso e sincronizar seus comandos.",
                color = TextSecondary,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Abas: Criar Conta / Já tenho conta
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurface, RoundedCornerShape(10.dp))
                    .padding(4.dp)
            ) {
                AuthTab(
                    text = "Criar Conta",
                    selected = viewModel.isCreatingAccount,
                    onClick = { viewModel.changeTab(true) },
                    modifier = Modifier.weight(1f)
                )
                AuthTab(
                    text = "Já tenho conta",
                    selected = !viewModel.isCreatingAccount,
                    onClick = { viewModel.changeTab(false) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // O identificador só aparece no cadastro
            if (viewModel.isCreatingAccount) {
                FieldLabel("IDENTIFICADOR SHELL")
                TerminalTextField(
                    value = viewModel.username,
                    onValueChange = { viewModel.username = it },
                    placeholder = "@sysadmin_pro"
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            FieldLabel("ENDEREÇO DE E-MAIL")
            TerminalTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                placeholder = "seu@dominio.com.br",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            FieldLabel("SENHA DO USUÁRIO")
            TerminalTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                placeholder = "••••••••",
                keyboardType = KeyboardType.Password,
                isPassword = !showPassword,
                trailing = {
                    TextButton(onClick = { showPassword = !showPassword }) {
                        Text(
                            text = if (showPassword) "ocultar" else "ver",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            )

            // Mensagem de erro
            val error = viewModel.errorMessage
            if (error != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = error, color = Color(0xFFF85149), fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.submit(onSuccess) },
                enabled = !viewModel.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    contentColor = Color.Black
                )
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = if (viewModel.isCreatingAccount) {
                            "Criar Conta ($ sudo useradd)"
                        } else {
                            "Entrar ($ login)"
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun AuthTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = if (selected) BorderColor else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) PrimaryGreen else TextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        color = TextSecondary,
        fontFamily = JetBrainsMonoFontFamily,
        fontSize = 11.sp,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun TerminalTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    trailing: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder, color = TextSecondary) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = trailing,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = DarkSurface,
            unfocusedContainerColor = DarkSurface,
            focusedBorderColor = PrimaryGreen,
            unfocusedBorderColor = BorderColor,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            cursorColor = PrimaryGreen
        )
    )
}
