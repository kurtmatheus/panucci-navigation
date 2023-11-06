package br.com.alura.panucci.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.alura.panucci.sampledata.sampleProducts
import br.com.alura.panucci.ui.screens.HighlightsListScreen

const val HIGH_LIGHTS_LIST_ROUTE = "highlight"

fun NavGraphBuilder.highLightsListScreen(navController: NavHostController) {
    composable(HIGH_LIGHTS_LIST_ROUTE) {
        HighlightsListScreen(
            products = sampleProducts,
            onNavigateToDetails = { product ->
                navController.navigateToProductDetails(product.id)
            },
            onNavigateToCheckout = {
                navController.navigateToCheckout()
            },
        )
    }
}

fun NavController.navigateToHighlightsList(
    navOptions: NavOptions? = null
) {
    navigate(HIGH_LIGHTS_LIST_ROUTE, navOptions)
}