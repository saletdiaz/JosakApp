package edu.josakapp.proyectoJosakapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import edu.josakapp.proyectoJosakapp.ui.navigation.NavigationHost
import edu.josakapp.proyectoJosakapp.ui.theme.AplicationNewTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AplicationNewTheme{
                Surface {
                    val navController = rememberNavController()
                    NavigationHost(navController = navController)
                }
            }
        }
    }
}

