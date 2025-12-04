package edu.saletdiaz.aplicationnew.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import edu.saletdiaz.aplicationnew.data.model.Product
import edu.saletdiaz.aplicationnew.data.repository.ProductRepository
import java.io.BufferedReader
import java.io.InputStreamReader


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

