package edu.josakapp.proyectoJosakapp.ui.navigation

sealed class NavScreens(val ruta: String) {
    object NavMainScreen: NavScreens("main")
    object NavSecondScreen: NavScreens("second")
}