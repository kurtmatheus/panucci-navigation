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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.alura.panucci.navigation.*
import br.com.alura.panucci.ui.components.BottomAppBarItem
import br.com.alura.panucci.ui.components.PanucciBottomAppBar
import br.com.alura.panucci.ui.components.bottomAppBarItems
import br.com.alura.panucci.ui.screens.*
import br.com.alura.panucci.ui.theme.PanucciTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PanucciTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PanucciApp()
                }
            }
        }
    }
}


@Composable
fun PanucciApp(
    navController: NavHostController = rememberNavController()
) {
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
    val orderDoneState = backStackEntryState
        ?.savedStateHandle
        ?.getStateFlow<String?>(key = "order_done", initialValue = null)
        ?.collectAsState()

    Log.i("MainActivity", "onCreate: current saved state handle - ${orderDoneState?.value}")

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()

    orderDoneState?.value?.let {
        scope.launch {
            snackbarHostState.showSnackbar(it)
        }
    }

    val currentRoute = currentDestination?.route
    val selectedItem by remember(currentDestination) {
        val item = when (currentRoute) {
            highlightsListRoute -> BottomAppBarItem.HighlightsList
            menuRoute -> BottomAppBarItem.Menu
            drinksRoute -> BottomAppBarItem.Drinks
            else -> BottomAppBarItem.HighlightsList
        }
        mutableStateOf(item)
    }
    val containsInBottomAppBarItems = when (currentRoute) {
        highlightsListRoute, menuRoute, drinksRoute -> true
        else -> false
    }
    val isShowFab = when (currentDestination?.route) {
        menuRoute,
        drinksRoute -> true

        else -> false
    }
    PanucciApp(
        snackbarHostState = snackbarHostState,
        bottomAppBarItemSelected = selectedItem,
        onBottomAppBarItemSelectedChange = { item ->
            navController.navigateSingleTopWithPopUpTo(item)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanucciApp(
    bottomAppBarItemSelected: BottomAppBarItem = bottomAppBarItems.first(),
    onBottomAppBarItemSelectedChange: (BottomAppBarItem) -> Unit = {},
    onFabClick: () -> Unit = {},
    isShowTopBar: Boolean = false,
    isShowBottomBar: Boolean = false,
    isShowFab: Boolean = false,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    content: @Composable () -> Unit
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(modifier = Modifier.padding(8.dp)) {
                    Text(text = data.visuals.message)
                }
            }
        },
        topBar = {
            if (isShowTopBar) {
                CenterAlignedTopAppBar(
                    modifier = Modifier.semantics { contentDescription = "top app bar" },
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
                    modifier = Modifier.semantics { contentDescription = "floating action button" },
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
            PanucciApp(content = {})
        }
    }
}