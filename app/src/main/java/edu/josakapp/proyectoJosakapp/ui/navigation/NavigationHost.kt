package edu.josakapp.proyectoJosakapp.ui.navigation

import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.josakapp.proyectoJosakapp.ui.view.LoginScreen
import edu.josakapp.proyectoJosakapp.ui.view.SecondScreen
import edu.josakapp.proyectoJosakapp.ui.viewmodel.SharedViewModel
import edu.josakapp.proyectoJosakapp.R
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

