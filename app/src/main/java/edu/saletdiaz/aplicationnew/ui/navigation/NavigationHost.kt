package edu.saletdiaz.aplicationnew.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.saletdiaz.aplicationnew.ui.view.HomeScreen
import edu.saletdiaz.aplicationnew.ui.view.SecondScreen
import edu.saletdiaz.aplicationnew.ui.viewmodel.SharedViewModel

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
            HomeScreen(
                name = vm.name,
                onNameChange = vm::updateName,
                onGoSecondScreen = {navController.navigate(NavScreens.NavSecondScreen.ruta)}
            )
        }

        /**Segunda pantalla*/
        composable ( NavScreens.NavSecondScreen.ruta ) {
            val productos = vm.productos
            SecondScreen(name = vm.name, productos = productos)
        }
    }
}