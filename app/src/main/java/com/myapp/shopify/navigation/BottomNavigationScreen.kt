package com.myapp.shopify.navigation

import HomeScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.myapp.admin.presentation.AddProduct
import com.myapp.auth.presentation.GoogleAuthUiClient

import com.myapp.profile.presentation.screen.ProfileScreen
import com.myapp.shopify.navigation.composables.CartScreen
import com.myapp.admin.presentation.AdminScreen
import com.myapp.admin.presentation.adminViewmodel

sealed class BottomNavScreen(
    val name : String  ,
    val id : Int  ,
    val route : String,
    val selectedIcon : ImageVector?,
    val unselectedIcon : ImageVector?,
 ) {
    object Home : BottomNavScreen(
        name = "Home",
        route= "home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        id = 1

    )
    object Profile : BottomNavScreen(
        name = "Profile",
        route= "profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        id = 2
    )

    object  Cart : BottomNavScreen(
        name = "Cart",
        route = "cart",
        selectedIcon = Icons.Filled.ShoppingCart,
        unselectedIcon = Icons.Outlined.ShoppingCart,
        id = 3
    )
    object  Menu: BottomNavScreen(
        name = "Menu",
        route = "menu",
        selectedIcon = Icons.Filled.Menu,
        unselectedIcon = Icons.Outlined.Menu,
        id = 4
    )

    object  AddProduct : BottomNavScreen(
        name = "addProduct",
        route = "addproduct",
        id = 5,
        selectedIcon = null,
        unselectedIcon = null
    )

}

object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val HOME = "home_graph"

}

sealed class AuthScreen(val route: String) {
    object Login : AuthScreen(route = "LOGIN")
    object SignUp : AuthScreen(route = "SIGN_UP")
    object Forgot : AuthScreen(route = "FORGOT")
}


@Composable
fun HomeNavGraph(navController: NavHostController, googleAuthUiClient: GoogleAuthUiClient) {
    val viewmodel :adminViewmodel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = BottomNavScreen.Home.route,
        route = Graph.HOME
       
    ) {
        composable(route = BottomNavScreen.Home.route) {
            HomeScreen()
        }
        composable(route = BottomNavScreen.Cart.route) {
            CartScreen()
        }
        composable(route = BottomNavScreen.Menu.route) {
            AdminScreen(viewmodel =viewmodel,
                onBackClick = {
                    navController.popBackStack()
                },
                onAddClick = {
                    navController.navigateSingleTopTo(route= BottomNavScreen.AddProduct.route)
                },
                onRefresh =  viewmodel::loadStuff
                )
        }

        composable(route = BottomNavScreen.Profile.route) {
            ProfileScreen(
                userData = googleAuthUiClient.getSignedInUser(),
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = BottomNavScreen.AddProduct.route){
            AddProduct(
                onBackClick = {
                    navController.popBackStack()
                },
                adminViewmodel = viewmodel
            )
        }
    }
}


fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
        launchSingleTop = true
    }
