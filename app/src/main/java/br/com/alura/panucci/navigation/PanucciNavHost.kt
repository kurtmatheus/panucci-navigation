package br.com.alura.panucci.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.alura.panucci.ui.components.BottomAppBarItem
import br.com.alura.panucci.ui.components.BottomAppBarItem.Drinks
import br.com.alura.panucci.ui.components.BottomAppBarItem.HighLightList
import br.com.alura.panucci.ui.components.BottomAppBarItem.Menu

@Composable
fun PanucciNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = HIGH_LIGHTS_LIST_ROUTE
    ) {
        highLightsListScreen(navController)
        menuScreen(navController)
        drinksScreen(navController)
        productDetailsScreen(navController)
        checkoutScreen(navController)
    }
}

val bottomAppBarItems = listOf(
    HighLightList,
    Menu,
    Drinks
)