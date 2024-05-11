package com.myapp.shopify.navigation.composables
import android.annotation.SuppressLint
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.myapp.auth.presentation.GoogleAuthUiClient
import com.myapp.shopify.navigation.BottomNavScreen
import com.myapp.shopify.navigation.HomeNavGraph
import com.myapp.shopify.navigation.navigateSingleTopTo


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppContent(
    navController : NavHostController,
    googleAuthUiClient: GoogleAuthUiClient,

    ) {
    val screens = listOf(
        BottomNavScreen.Home,
        BottomNavScreen.Cart,
        BottomNavScreen.Menu,
        BottomNavScreen.Profile
    )
    var selectedItemIndex by rememberSaveable {
        mutableStateOf( 0)
    }
    Scaffold(
        bottomBar = {
            NavigationBar {
                screens.forEach{  item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == item.id,
                        onClick = {  selectedItemIndex = item.id
                            navController.navigateSingleTopTo(item.route)
                        },
                        icon = {
                            Icon(
                                imageVector = (if (item.id == selectedItemIndex) {
                                    item.selectedIcon!!
                                } else item.unselectedIcon!!),
                                contentDescription = item.name
                            )
                        })
                }
            }
        }
    ){
        HomeNavGraph(navController = navController, googleAuthUiClient = googleAuthUiClient)
    }
}





