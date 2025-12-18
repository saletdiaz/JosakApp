package edu.josakapp.proyectoJosakapp.ui.navigation

sealed class NavScreens(val ruta: String) {

    object NavMainScreen: NavScreens("main")

    object NavHabitoScreen: NavScreens("haibito")
    object NavRankingScreen: NavScreens("rangking")
    object NavTiendaScreen: NavScreens("tienta")
    object NavPinguinoScreen: NavScreens("pinguino")
    object NavAjusteScreen: NavScreens("ajuste")
}