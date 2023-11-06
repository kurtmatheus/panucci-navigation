package br.com.alura.panucci

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import br.com.alura.panucci.navigation.DRINKS_ROUTE
import br.com.alura.panucci.navigation.HIGH_LIGHTS_LIST_ROUTE
import br.com.alura.panucci.navigation.MENU_ROUTE
import br.com.alura.panucci.navigation.PanucciNavHost
import br.com.alura.panucci.navigation.bottomAppBarItems
import br.com.alura.panucci.navigation.navigateToCheckout
import br.com.alura.panucci.navigation.navigateToDrinks
import br.com.alura.panucci.navigation.navigateToHighlightsList
import br.com.alura.panucci.navigation.navigateToMenu
import br.com.alura.panucci.ui.components.BottomAppBarItem
import br.com.alura.panucci.ui.components.PanucciBottomAppBar
import br.com.alura.panucci.ui.screens.*
import br.com.alura.panucci.ui.theme.PanucciTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            LaunchedEffect(Unit) {
                navController.addOnDestinationChangedListener { _, _, _ ->
                    val routes = navController.backQueue.map {
                        it.destination.route
                    }
                    Log.i("MainActivity", "onCreate: back stack - $routes")
                }
            }
            val backStackEntryState by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntryState?.destination
            val currentRoute = currentDestination?.route
            PanucciTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val selectedItem by remember(currentDestination) {
                        val item = when (currentRoute) {
                            HIGH_LIGHTS_LIST_ROUTE -> BottomAppBarItem.HighLightList
                            MENU_ROUTE -> BottomAppBarItem.Menu
                            DRINKS_ROUTE -> BottomAppBarItem.Drinks
                            else -> BottomAppBarItem.HighLightList
                        }
                        mutableStateOf(item)
                    }
                    val containsInBottomAppBarItems = when (currentRoute) {
                        HIGH_LIGHTS_LIST_ROUTE, MENU_ROUTE, DRINKS_ROUTE -> true
                        else -> false
                    }
                    val isShowFab = when (currentRoute) {
                        MENU_ROUTE, DRINKS_ROUTE -> true
                        else -> false
                    }
                    PanucciApp(
                        bottomAppBarItemSelected = selectedItem,
                        onBottomAppBarItemSelectedChange = { item ->
//                            val route = it.destination
//                            navController.navigate(route) {
//                                launchSingleTop = true
//                                popUpTo(route)
//                            }

                            val (route, navigate) = when (item) {
                                BottomAppBarItem.Drinks -> Pair(
                                    DRINKS_ROUTE,
                                    navController::navigateToDrinks
                                )
                                BottomAppBarItem.HighLightList -> Pair(
                                    HIGH_LIGHTS_LIST_ROUTE,
                                    navController::navigateToHighlightsList
                                )
                                BottomAppBarItem.Menu -> Pair(
                                    MENU_ROUTE,
                                    navController::navigateToMenu
                                )
                            }

                            val navOptions = navOptions {
                                launchSingleTop = true
                                popUpTo(route)
                            }

                            navigate(navOptions)
                        },
                        onFabClick = {
                            navController.navigateToCheckout()
                        },
                        isShowTopBar = containsInBottomAppBarItems,
                        isShowBottomBar = containsInBottomAppBarItems,
                        isShowFab = isShowFab
                    ) {
                        PanucciNavHost(navController = navController)
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanucciApp(
    bottomAppBarItemSelected: BottomAppBarItem = bottomAppBarItems.first(),
    onBottomAppBarItemSelectedChange: (BottomAppBarItem) -> Unit = {},
    onFabClick: () -> Unit = {},
    isShowTopBar: Boolean = false,
    isShowBottomBar: Boolean = false,
    isShowFab: Boolean = false,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            if (isShowTopBar) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Ristorante Panucci")
                    },
                )
            }
        },
        bottomBar = {
            if (isShowBottomBar) {
                PanucciBottomAppBar(
                    item = bottomAppBarItemSelected,
                    items = bottomAppBarItems,
                    onItemChange = onBottomAppBarItemSelectedChange,
                )
            }
        },
        floatingActionButton = {
            if (isShowFab) {
                FloatingActionButton(
                    onClick = onFabClick
                ) {
                    Icon(
                        Icons.Filled.PointOfSale,
                        contentDescription = null
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun PanucciAppPreview() {
    PanucciTheme {
        Surface {
            PanucciApp {}
        }
    }
}