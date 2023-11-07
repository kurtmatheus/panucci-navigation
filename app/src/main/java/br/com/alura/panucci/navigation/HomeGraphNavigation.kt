package br.com.alura.panucci.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import androidx.navigation.navigation
import br.com.alura.panucci.ui.components.BottomAppBarItem

internal const val HOME_ROUTE = "home"

fun NavGraphBuilder.homeGraph(navController: NavHostController) {
    navigation(
        startDestination = HIGH_LIGHTS_LIST_ROUTE,
        route = HOME_ROUTE
    ) {
        highLightsListScreen(navController)
        menuScreen(navController)
        drinksScreen(navController)
    }
}

fun NavController.navigateToHomeGraph() {
    navigate(HOME_ROUTE)
}

fun NavController.navigateSingleTopWithPopUpTo(
    item: BottomAppBarItem,
) {
    val (route, navigate) = when (item) {
        BottomAppBarItem.Drinks -> Pair(
            DRINKS_ROUTE,
            ::navigateToDrinks
        )

        BottomAppBarItem.HighLightList -> Pair(
            HIGH_LIGHTS_LIST_ROUTE,
            ::navigateToHighlightsList
        )

        BottomAppBarItem.Menu -> Pair(
            MENU_ROUTE,
            ::navigateToMenu
        )
    }

    val navOptions = navOptions {
        launchSingleTop = true
        popUpTo(route)
    }

    navigate(navOptions)
}