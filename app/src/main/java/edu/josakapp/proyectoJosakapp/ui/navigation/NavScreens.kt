package edu.josakapp.proyectoJosakapp.ui.navigation

sealed class NavScreens(val ruta: String) {
    object NavMainScreen: NavScreens("main")
    object NavSecondScreen: NavScreens("second")

    /**Implementación de registerScreen la cual se encargara de registrar
     * y ForgotPasswordScreen la cual se encargara de recuperar
     * si se olvida la contraseña
     */
    object NavRegisterScreen: NavScreens("register")
    object NavForgotPasswordScreen: NavScreens("forgotPassword")

}