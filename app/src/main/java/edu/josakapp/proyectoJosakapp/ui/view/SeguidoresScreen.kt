package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import edu.josakapp.proyectoJosakapp.R
import edu.josakapp.proyectoJosakapp.converter.base64ToBitmap
import edu.josakapp.proyectoJosakapp.data.model.User
import edu.josakapp.proyectoJosakapp.ui.components.SettingsScaffold
import edu.josakapp.proyectoJosakapp.ui.viewmodel.UserViewModel

@Composable
fun SeguidoresScreen(
    userViewModel: UserViewModel,
    userId: String,
    initialTab: Int = 0,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(initialTab) }
    val seguidores by userViewModel.seguidoresList.collectAsState()
    val siguiendo by userViewModel.siguiendoList.collectAsState()

    val colorCeleste = Color(0xFF03A9F4)

    // Cargar ambas listas al entrar
    LaunchedEffect(userId) {
        userViewModel.loadSeguidoresList(userId)
        userViewModel.loadSiguiendoList(userId)
    }

    SettingsScaffold(title = "RED SOCIAL", onBackClick = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Tabs: Seguidores / Siguiendo
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = colorCeleste,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = colorCeleste
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            "Seguidores (${seguidores.size})",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            "Siguiendo (${siguiendo.size})",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }

            // Contenido según tab seleccionado
            val currentList = if (selectedTab == 0) seguidores else siguiendo
            val emptyMessage = if (selectedTab == 0)
                "Aún no tienes seguidores" else "Aún no sigues a nadie"

            if (currentList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "😔",
                            fontSize = 48.sp
                        )
                        Text(
                            text = emptyMessage,
                            fontSize = 16.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(currentList) { user ->
                        SocialUserCard(user = user)
                    }
                }
            }
        }
    }
}

@Composable
private fun SocialUserCard(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Foto de perfil
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = Color.LightGray.copy(alpha = 0.2f),
                border = BorderStroke(1.dp, Color(0xFF03A9F4).copy(alpha = 0.5f))
            ) {
                AsyncImage(
                    model = user.fotoPerfil?.takeIf { it.isNotBlank() }?.let { base64ToBitmap(it) }
                        ?: R.drawable.ic_person_placeholder,
                    contentDescription = "Foto de ${user.nombre_usuario}",
                    placeholder = painterResource(R.drawable.ic_person_placeholder),
                    error = painterResource(R.drawable.ic_person_placeholder),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Info del usuario
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.nombre_usuario,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = "Nivel ${user.nivel} · ${user.xp_total} XP",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
