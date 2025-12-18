package edu.josakapp.proyectoJosakapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.josakapp.proyectoJosakapp.ui.view.LoginScreen
import edu.josakapp.proyectoJosakapp.ui.view.HabitoScreen
import edu.josakapp.proyectoJosakapp.ui.viewmodel.SharedViewModel

@Composable
fun NavigationHost(navController: NavHostController){
    val vm: SharedViewModel = viewModel()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = NavScreens.NavMainScreen.ruta
    ) {
        composable (
            NavScreens.NavMainScreen.ruta
        ){
            LoginScreen(
                name = vm.name,
                onNameChange = vm::updateName,
                onGoSecondScreen = {navController.navigate(NavScreens.NavHabitoScreen.ruta)}
            )
        }

        /**Segunda pantalla*/
        composable ( NavScreens.NavHabitoScreen.ruta ) {
            val productos = vm.productos
            HabitoScreen()
        }
    }
}

