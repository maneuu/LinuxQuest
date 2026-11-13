package br.com.linuxquest.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import br.com.linuxquest.app.ui.navigation.MainScreen
import br.com.linuxquest.app.ui.theme.LinuxQuestTheme
import br.com.linuxquest.app.util.FirestoreVerifier
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Dispara a verificação assíncrona do Firestore ao iniciar
        lifecycleScope.launch {
            FirestoreVerifier.verifyAllCollections()
        }

        setContent {
            LinuxQuestTheme {
                MainScreen()
            }
        }
    }
}