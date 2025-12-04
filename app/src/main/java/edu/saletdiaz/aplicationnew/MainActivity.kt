package edu.saletdiaz.aplicationnew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import edu.saletdiaz.aplicationnew.ui.navigation.NavigationHost
import edu.saletdiaz.aplicationnew.ui.theme.AplicationNewTheme
import edu.saletdiaz.aplicationnew.ui.view.HomeScreen


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

