package edu.josakapp.proyectoJosakapp.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

/**Esta clase sera la plantilla para las pantallas*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold (title: String ,
                 screen: @Composable (PaddingValues) -> Unit)  {
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = title) }
            )
        }
    ){ innerPadding -> screen(innerPadding)  }
}