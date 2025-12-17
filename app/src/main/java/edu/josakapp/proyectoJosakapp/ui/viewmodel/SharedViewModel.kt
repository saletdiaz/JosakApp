package edu.josakapp.proyectoJosakapp.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import edu.josakapp.proyectoJosakapp.data.model.Product
import edu.josakapp.proyectoJosakapp.data.repository.ProductRepository


class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ProductRepository(application)

    var name by mutableStateOf("")
        private set

    fun updateName(value: String) {
        name = value
    }

    // 👇 Aquí ya usas el repositorio
    val productos: List<Product> by lazy {
        repository.readRawFile()
    }
}

