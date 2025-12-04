package edu.saletdiaz.aplicationnew.data.repository

import android.content.Context
import edu.saletdiaz.aplicationnew.data.model.Product
import edu.saletdiaz.aplicationnew.R


/**Clase encargada de leer y cargar los productos por medio de un fichero*/
class ProductRepository (val context: Context){
    fun readRawFile(): MutableList<Product>{
        val products = mutableListOf<Product>()

        /**Control de errores*/
        try {
            val inputStream = context.resources.openRawResource(R.raw.products)
            val reader = inputStream.bufferedReader()

            reader.useLines {lines ->
                lines.drop(1).forEach { line ->
                    val parts = line.split(",")
                    if(parts.size >= 6) {
                        val id = parts[0]
                        val nombre = parts[1]
                        val descripcion = parts[2]
                        val stock = parts[3].toIntOrNull() ?: 0
                        val cover = parts[4]
                        val precio = parts[5].toFloatOrNull() ?: 0f

                        products.add(Product(id, nombre, descripcion, stock, cover, precio))
                    }
                }
            }
        } catch(e: Exception){
            e.printStackTrace()
            return mutableListOf()
        }
        return products
    }
}