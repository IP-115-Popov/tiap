package ru.sergey.tiap.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.sergey.tiap.presentation.screens.ChainScreen
import ru.sergey.tiap.presentation.screens.DKAScreen
import ru.sergey.tiap.viewmodel.DKAScreenViewModel
import ru.sergey.tiap.viewmodel.DKAScreenViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            //val dkaViewModel = DKAScreenViewModel(this)
        val dkaViewModel = ViewModelProvider(this, DKAScreenViewModelFactory(this))
            .get(DKAScreenViewModel::class.java)
        setContent {
            Main(this, dkaViewModel)
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Main(context: Context, dkaViewModel : DKAScreenViewModel) {
    val navController = rememberNavController()
    Column {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.ChainScreen.route,
            modifier = Modifier.fillMaxHeight(0.9f)
        ) {
            composable(NavRoutes.ChainScreen.route) { ChainScreen() }
            composable(NavRoutes.DKAScreen.route) { DKAScreen(vm = dkaViewModel) }
        }
        BottomNavigationBar(
            navController = navController, modifier = Modifier.fillMaxHeight()
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, modifier: Modifier = Modifier) {
    NavigationBar(modifier) {
        val backStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry.value?.destination?.route

        NavBarItems.BarItems.forEach { navItem ->
            NavigationBarItem(selected = currentRoute == navItem.route, onClick = {
                navController.navigate(navItem.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }, icon = {
                Icon(
                    imageVector = navItem.image, contentDescription = navItem.title
                )
            }, label = {
                Text(text = navItem.title)
            })
        }
    }
}

data class BarItem(
    val title: String, val image: ImageVector, val route: String
)

object NavBarItems {
    val BarItems = listOf(
        BarItem(
            title = "TaskScreen", image = Icons.Filled.Home, route = NavRoutes.ChainScreen.route
        ), BarItem(
            title = "AstronomicalScreen",
            image = Icons.Filled.Edit,
            route = NavRoutes.DKAScreen.route
        )
    )
}

sealed class NavRoutes(val route: String) {
    object ChainScreen : NavRoutes("ChainScreen")
    object DKAScreen : NavRoutes("DKAScreen")
}