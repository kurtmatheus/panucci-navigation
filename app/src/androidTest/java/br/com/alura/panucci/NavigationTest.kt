package br.com.alura.panucci

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import br.com.alura.panucci.navigation.PanucciNavHost
import br.com.alura.panucci.navigation.checkoutRoute
import br.com.alura.panucci.navigation.drinksRoute
import br.com.alura.panucci.navigation.highlightsListRoute
import br.com.alura.panucci.navigation.menuRoute
import br.com.alura.panucci.navigation.navigateToCheckout
import br.com.alura.panucci.navigation.navigateToHomeGraph
import br.com.alura.panucci.navigation.navigateToProductDetails
import br.com.alura.panucci.navigation.productDetailsRoute
import br.com.alura.panucci.navigation.productIdArgument
import br.com.alura.panucci.sampledata.sampleProducts
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val TAG = "panucci app"

class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var navController: TestNavHostController

    @Before
    fun setupAppNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            PanucciApp(navController = navController)
        }
    }

    @Test
    fun appNavHost_verifyStartDestination() {
        composeTestRule.onRoot().printToLog(TAG)
        composeTestRule
            .onNodeWithText("Destaques do dia")
            .assertIsDisplayed()
    }

    @Test
    fun appNavHost_verifyIfMenuIsDisplayed(){
        composeTestRule.onRoot().printToLog(TAG)
        composeTestRule.onNodeWithText("Menu")
            .performClick()

        composeTestRule.onAllNodesWithText("Menu")
            .assertCountEquals(2)

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, menuRoute)
    }

    @Test
    fun appNavHost_verifyIfProductDetailsScreenIsDisplayedFromHighlightsScreen(){
        composeTestRule.onRoot().printToLog(TAG)
        composeTestRule.onAllNodesWithContentDescription("highlights product card")
            .onFirst()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, "$productDetailsRoute/{$productIdArgument}")
    }

    @Test
    fun appNavHost_verifyIfFailScreenIsDisplayedFromHighlightsScreen(){
        composeTestRule.onRoot().printToLog(TAG)
        composeTestRule.onAllNodesWithContentDescription("highlights product card")
            .onFirst()
            .performClick()

        composeTestRule.waitUntil(3000) {
            composeTestRule.onAllNodesWithText("Falha ao buscar o produto")
                .fetchSemanticsNodes().size == 1
        }

        composeTestRule.onNodeWithText("Falha ao buscar o produto")
            .assertIsDisplayed()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, "$productDetailsRoute/{$productIdArgument}")
    }

    @Test
    fun appNavHost_verifyIfCheckoutIsDisplayedFromHighlightsScreen(){
        composeTestRule.onRoot().printToLog(TAG)
        composeTestRule.onAllNodesWithContentDescription("highlight screen checkout button")
            .onFirst()
            .performClick()

        composeTestRule.onNodeWithText("Pedido")
            .assertIsDisplayed()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, checkoutRoute)
    }

    @Test
    fun appNavHost_verifyIfCheckoutIsDisplayedFromMenuScreen(){
        composeTestRule.onRoot().printToLog(TAG)
        composeTestRule.onNodeWithText("Menu")
            .performClick()

        composeTestRule.onNodeWithContentDescription("floating action button")
            .performClick()

        composeTestRule.onNodeWithText("Pedido")
            .assertIsDisplayed()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, checkoutRoute)
    }

    @Test
    fun appNavHost_verifyIfCheckoutScreenIsDIsplayedFromProductDetails(){
        composeTestRule.onRoot().printToLog(TAG)

        composeTestRule.runOnUiThread {
            navController.navigateToProductDetails(sampleProducts.first().id)
        }

        composeTestRule.waitUntil(3000) {
            composeTestRule.onAllNodesWithText("Pedir")
                .fetchSemanticsNodes().size == 1
        }

        composeTestRule.onNodeWithText("Pedir")
            .performClick()

        composeTestRule.onNodeWithText("Pedido")
            .assertIsDisplayed()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, checkoutRoute)
    }

    @Test
    fun appNavHost_verifyIfFabIsDisplayedOnlyInMenuOrDrinksDestination(){
        composeTestRule.onRoot().printToLog(TAG)

        composeTestRule.onNodeWithText("Menu")
            .performClick()

        var route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, menuRoute)

        composeTestRule.onNodeWithContentDescription("bottom app bar")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Bebidas")
            .performClick()

        route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, drinksRoute)

        composeTestRule.onNodeWithContentDescription("bottom app bar")
            .assertIsDisplayed()

        composeTestRule.onAllNodesWithContentDescription("drinks product card")
            .onFirst()
            .performClick()

        composeTestRule.onNodeWithContentDescription("bottom app bar")
            .assertDoesNotExist()

    }

    @Test
    fun appNavHost_verifyIfBottomAppBarOrTopAppBarIsDisplayedOnlyInHomeGraphNavigation() {
        composeTestRule.onRoot().printToLog(TAG)

        composeTestRule.runOnUiThread {
            navController.navigateToHomeGraph()
        }

        composeTestRule.onNodeWithContentDescription("bottom app bar")
            .assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("top app bar")
            .assertIsDisplayed()


        composeTestRule.runOnUiThread {
            navController.navigateToProductDetails(sampleProducts.first().id)
        }

        composeTestRule.waitUntil(3000) {
            composeTestRule.onAllNodesWithText("Pedir")
                .fetchSemanticsNodes().size == 1
        }

        composeTestRule.onNodeWithContentDescription("bottom app bar")
            .assertDoesNotExist()

        composeTestRule.onNodeWithContentDescription("top app bar")
            .assertDoesNotExist()

    }

    @Test
    fun appNavHost_verifyIfSnackbarIsDisplayedWhenFinishTheOrder(){
        composeTestRule.onRoot().printToLog(TAG)

        composeTestRule.runOnUiThread {
            navController.navigateToCheckout()
        }

        composeTestRule.waitUntil(3000) {
            composeTestRule.onAllNodesWithText("Pedir")
                .fetchSemanticsNodes().size == 1
        }

        composeTestRule.onNodeWithText("Pedir")
            .performClick()

        composeTestRule.onNodeWithText("Pedido Realizado Com Sucesso")
            .assertIsDisplayed()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, highlightsListRoute)
    }
}