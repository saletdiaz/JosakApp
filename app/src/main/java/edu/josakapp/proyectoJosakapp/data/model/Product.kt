package edu.josakapp.proyectoJosakapp.data.model

/**Clase encargada de almacenar los datos necesarios del producto*/
data class Product (val id: String,
    val nombre: String,
    val descripcion: String,
    val stock: Int,
    val cover: String,
    val precio: Float)