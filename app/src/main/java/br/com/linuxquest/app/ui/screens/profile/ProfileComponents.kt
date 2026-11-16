package br.com.linuxquest.app.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.linuxquest.app.ui.theme.BorderColor
import br.com.linuxquest.app.ui.theme.DarkSurface
import br.com.linuxquest.app.ui.theme.JetBrainsMonoFontFamily
import br.com.linuxquest.app.ui.theme.PrimaryGreen
import br.com.linuxquest.app.ui.theme.TextPrimary
import br.com.linuxquest.app.ui.theme.TextSecondary

// Barra do topo, ex: ">_ PERFIL"
@Composable
fun ProfileTopBar(title: String = "PERFIL") {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(DarkSurface, RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = ">_",
                    color = PrimaryGreen,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title,
                color = TextPrimary,
                fontFamily = JetBrainsMonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
        HorizontalDivider(color = BorderColor)
    }
}

// Título pequeno de seção, ex: "MENU PRINCIPAL"
@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        color = TextSecondary,
        fontFamily = JetBrainsMonoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

// Uma linha clicável do menu: ícone + texto + (extra) + seta
@Composable
fun MenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    subtitle: String? = null,
    extra: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryGreen,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = TextPrimary, fontSize = 14.sp)
            if (subtitle != null) {
                Text(text = subtitle, color = TextSecondary, fontSize = 12.sp)
            }
        }

        if (extra != null) {
            Box(
                modifier = Modifier
                    .background(BorderColor, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = extra,
                    color = TextSecondary,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = TextSecondary
        )
    }
}
