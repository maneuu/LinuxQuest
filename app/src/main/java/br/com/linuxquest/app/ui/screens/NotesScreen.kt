package br.com.linuxquest.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.linuxquest.app.data.model.Note
import br.com.linuxquest.app.ui.screens.profile.NotesViewModel
import br.com.linuxquest.app.ui.screens.profile.ProfileTopBar
import br.com.linuxquest.app.ui.theme.BorderColor
import br.com.linuxquest.app.ui.theme.DarkSurface
import br.com.linuxquest.app.ui.theme.JetBrainsMonoFontFamily
import br.com.linuxquest.app.ui.theme.PrimaryGreen
import br.com.linuxquest.app.ui.theme.TextPrimary
import br.com.linuxquest.app.ui.theme.TextSecondary

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = viewModel()
) {
    // Busca as anotações no Firestore quando a tela abre
    LaunchedEffect(Unit) {
        viewModel.loadNotes()
    }

    val notes = viewModel.filteredNotes()

    Column(modifier = modifier.fillMaxSize()) {
        ProfileTopBar()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "~/notes",
                color = PrimaryGreen,
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 13.sp
            )
            Text(
                text = "${viewModel.notes.size} arquivos indexados",
                color = TextSecondary,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Campo de busca (filtra pelo título ou conteúdo)
            OutlinedTextField(
                value = viewModel.searchText,
                onValueChange = { viewModel.searchText = it },
                placeholder = { Text("Buscar anotação...", color = TextSecondary) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = notesFieldColors()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Lista ocupa o espaço que sobrar
            Box(modifier = Modifier.weight(1f)) {
                val error = viewModel.errorMessage
                if (viewModel.isLoading) {
                    CircularProgressIndicator(
                        color = PrimaryGreen,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (error != null) {
                    Text(
                        text = error,
                        color = Color(0xFFF85149),
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (notes.isEmpty()) {
                    Text(
                        text = "Nenhuma anotação encontrada",
                        color = TextSecondary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(notes) { note ->
                            NoteItem(note = note, onClick = { viewModel.openNote(note) })
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { viewModel.openNewNote() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    contentColor = Color.Black
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Criar Nova Anotação", fontFamily = JetBrainsMonoFontFamily)
            }
        }
    }

    if (viewModel.showDialog) {
        NoteDialog(viewModel)
    }
}

@Composable
private fun NoteItem(note: Note, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = DarkSurface,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, BorderColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = PrimaryGreen,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = note.title,
                color = TextPrimary,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = TextSecondary
            )
        }
    }
}

// Janela para criar, editar ou excluir uma anotação
@Composable
private fun NoteDialog(viewModel: NotesViewModel) {
    val isNew = viewModel.editingNote == null

    AlertDialog(
        onDismissRequest = { viewModel.closeDialog() },
        containerColor = DarkSurface,
        title = {
            Text(
                text = if (isNew) "Nova anotação" else "Editar anotação",
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = viewModel.dialogTitle,
                    onValueChange = { viewModel.dialogTitle = it },
                    label = { Text("Título") },
                    singleLine = true,
                    colors = notesFieldColors()
                )
                OutlinedTextField(
                    value = viewModel.dialogContent,
                    onValueChange = { viewModel.dialogContent = it },
                    label = { Text("Conteúdo") },
                    minLines = 4,
                    colors = notesFieldColors()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { viewModel.saveNote() },
                enabled = viewModel.dialogTitle.isNotBlank()
            ) {
                Text("Salvar", color = PrimaryGreen)
            }
        },
        dismissButton = {
            Row {
                // Só dá para excluir uma nota que já existe
                if (!isNew) {
                    TextButton(onClick = { viewModel.deleteNote() }) {
                        Text("Excluir", color = Color(0xFFF85149))
                    }
                }
                TextButton(onClick = { viewModel.closeDialog() }) {
                    Text("Cancelar", color = TextSecondary)
                }
            }
        }
    )
}

@Composable
private fun notesFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = DarkSurface,
    unfocusedContainerColor = DarkSurface,
    focusedBorderColor = PrimaryGreen,
    unfocusedBorderColor = BorderColor,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedLabelColor = PrimaryGreen,
    unfocusedLabelColor = TextSecondary,
    cursorColor = PrimaryGreen
)
