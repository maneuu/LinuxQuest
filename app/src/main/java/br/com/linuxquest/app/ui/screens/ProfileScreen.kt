package br.com.linuxquest.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.linuxquest.app.ui.screens.profile.MenuItem
import br.com.linuxquest.app.ui.screens.profile.ProfileTopBar
import br.com.linuxquest.app.ui.screens.profile.ProfileViewModel
import br.com.linuxquest.app.ui.screens.profile.SectionTitle
import br.com.linuxquest.app.ui.theme.BorderColor
import br.com.linuxquest.app.ui.theme.DarkSurface
import br.com.linuxquest.app.ui.theme.JetBrainsMonoFontFamily
import br.com.linuxquest.app.ui.theme.PrimaryGreen
import br.com.linuxquest.app.ui.theme.TextPrimary
import br.com.linuxquest.app.ui.theme.TextSecondary

@Composable
fun ProfileScreen(
    onCreateAccount: () -> Unit,
    onLogin: () -> Unit,
    onOpenNotes: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current

    // Recarrega o perfil toda vez que a tela aparece
    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    // Botões que ainda não têm tela própria
    val comingSoon = {
        Toast.makeText(context, "Em breve!", Toast.LENGTH_SHORT).show()
    }

    Column(modifier = modifier.fillMaxSize()) {
        ProfileTopBar()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (viewModel.isLoggedIn) {
                LoggedInProfile(
                    username = viewModel.username,
                    memberSince = viewModel.memberSince,
                    notesCount = viewModel.notesCount,
                    onOpenNotes = onOpenNotes,
                    onComingSoon = comingSoon,
                    onLogout = { viewModel.logout() }
                )
            } else {
                GuestProfile(
                    onCreateAccount = onCreateAccount,
                    onLogin = onLogin,
                    onComingSoon = comingSoon
                )
            }
        }
    }
}

// ---------- Usuário logado ----------

@Composable
private fun LoggedInProfile(
    username: String,
    memberSince: String,
    notesCount: Int,
    onOpenNotes: () -> Unit,
    onComingSoon: () -> Unit,
    onLogout: () -> Unit
) {
    Text(
        text = "user@linuxquest:~$",
        color = PrimaryGreen,
        fontFamily = JetBrainsMonoFontFamily,
        fontSize = 12.sp
    )
    Text(
        text = username,
        color = TextPrimary,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = "@$username • Membro desde $memberSince",
        color = TextSecondary,
        fontFamily = JetBrainsMonoFontFamily,
        fontSize = 12.sp
    )

    Spacer(modifier = Modifier.height(24.dp))
    SectionTitle("MENU PRINCIPAL")

    MenuCard {
        MenuItem(
            icon = Icons.Default.Edit,
            title = "Minhas Anotações",
            extra = "$notesCount notas",
            onClick = onOpenNotes
        )
        HorizontalDivider(color = BorderColor)
        MenuItem(icon = Icons.Default.Settings, title = "Configurações", onClick = onComingSoon)
        HorizontalDivider(color = BorderColor)
        MenuItem(icon = Icons.Default.Email, title = "Ajuda & Dúvidas", onClick = onComingSoon)
        HorizontalDivider(color = BorderColor)
        MenuItem(
            icon = Icons.Default.Info,
            title = "Sobre o LinuxQuest",
            extra = "v1.0",
            onClick = onComingSoon
        )
    }

    Spacer(modifier = Modifier.height(24.dp))

    TextButton(
        onClick = onLogout,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = null,
            tint = TextSecondary
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "Sair da conta",
            color = TextSecondary,
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 12.sp
        )
    }
}

// ---------- Visitante (sem login) ----------

@Composable
private fun GuestProfile(
    onCreateAccount: () -> Unit,
    onLogin: () -> Unit,
    onComingSoon: () -> Unit
) {
    // Cartão "guest@linuxquest"
    MenuCard {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "guest@linuxquest",
                    color = TextPrimary,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Modo Visitante", color = TextSecondary, fontSize = 12.sp)
            }
            Box(
                modifier = Modifier
                    .background(BorderColor, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "CONVIDADO",
                    color = TextSecondary,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 10.sp
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Cartão "Progresso não salvo"
    MenuCard {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Progresso não salvo",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Crie ou acesse sua conta para salvar suas atividades e histórico.",
                color = TextSecondary,
                fontSize = 13.sp
            )
            Button(
                onClick = onCreateAccount,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    contentColor = Color.Black
                )
            ) {
                Text(text = "CRIAR CONTA", fontFamily = JetBrainsMonoFontFamily)
            }
            OutlinedButton(
                onClick = onLogin,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, BorderColor)
            ) {
                Text(
                    text = "ENTRAR",
                    color = TextPrimary,
                    fontFamily = JetBrainsMonoFontFamily
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(24.dp))
    SectionTitle("PREFERÊNCIAS DA SESSÃO")

    MenuCard {
        MenuItem(
            icon = Icons.Default.Settings,
            title = "Configurações",
            subtitle = "Tema, sons e preferências",
            onClick = onComingSoon
        )
        HorizontalDivider(color = BorderColor)
        MenuItem(
            icon = Icons.Default.Email,
            title = "Suporte & Dúvidas",
            subtitle = "Central de ajuda e documentação",
            onClick = onComingSoon
        )
    }
}

// Cartão escuro com borda, usado em vários lugares
@Composable
private fun MenuCard(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = DarkSurface,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BorderColor)
    ) {
        Column { content() }
    }
}
