package br.com.alura.panucci.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.alura.panucci.sampledata.sampleProducts
import br.com.alura.panucci.ui.screens.MenuListScreen

const val MENU_ROUTE = "menu"

fun NavGraphBuilder.menuScreen(navController: NavHostController) {
    composable(MENU_ROUTE) {
        MenuListScreen(
            products = sampleProducts,
            onNavigateToDetails = { product ->
                navController.navigateToProductDetails(product.id)
            },
        )
    }
}

fun NavController.navigateToMenu(
    navOptions: NavOptions? = null
) {
    navigate(MENU_ROUTE, navOptions)
}