package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import edu.josakapp.proyectoJosakapp.ui.components.AppScaffold
import edu.josakapp.proyectoJosakapp.R
import edu.josakapp.proyectoJosakapp.data.model.Product


@Composable
fun SecondScreen(name: String,productos: List<Product>) {
    AppScaffold(title = stringResource(R.string.segunda_pantalla)) { padding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Bienvenida",
                tint = androidx.compose.ui.graphics.Color.Green,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(text = stringResource(R.string.bienvenido_usuario, name))

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ){
                items(productos) { producto ->
                    Card (modifier = Modifier.fillMaxWidth(),
                          colors = CardDefaults.cardColors(containerColor = Color.White),
                          elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column (modifier = Modifier.weight(1f)){
                                Text(
                                    text = producto.nombre,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Precio: ${producto.precio} €",
                                    color = Color.Gray
                                )
                                Text(
                                    text = producto.descripcion,
                                    color = Color.DarkGray
                                )

                                AsyncImage(
                                    model = producto.cover,
                                    contentDescription = producto.nombre,
                                    modifier = Modifier.size(80.dp),
                                    contentScale = ContentScale.Crop
                                )

                            }
                        }
                    }
                }
            }
        }
    }
}