package edu.josakapp.proyectoJosakapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import edu.josakapp.proyectoJosakapp.data.di.AppModule

class ForgotPasswordViewModel : ViewModel() {

    private val authService = AppModule.authService

    fun sendResetEmail(
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        authService.resetPassword(email)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error desconocido")
            }
    }
}
