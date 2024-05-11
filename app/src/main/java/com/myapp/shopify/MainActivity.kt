package com.myapp.shopify
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.myapp.auth.presentation.GoogleAuthUiClient
import com.myapp.auth.presentation.LoginScreen
import com.myapp.auth.presentation.SignInViewmodel
import com.myapp.shopify.navigation.AuthScreen
import com.myapp.shopify.navigation.BottomNavScreen
import com.myapp.shopify.navigation.Graph
import com.myapp.shopify.navigation.composables.AppContent
import com.myapp.shopify.ui.theme.ShopifyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopifyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel = viewModel<SignInViewmodel>()
                    val state by viewModel.state.collectAsState()
                    NavHost(
                        navController = navController,
                        startDestination = Graph.AUTHENTICATION,
                        route = Graph.ROOT
                    ) {

                        navigation(
                            startDestination = AuthScreen.Login.route,
                            route = Graph.AUTHENTICATION
                        ) {
                            composable(route = AuthScreen.Login.route) {


                                LaunchedEffect(key1 = Unit) {
                                    if (googleAuthUiClient.getSignedInUser() != null) {
                                        navController.navigate(Graph.HOME)
                                    }
                                }
                                val launcher = rememberLauncherForActivityResult(
                                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                                    onResult = { result ->
                                        if (result.resultCode == RESULT_OK) {
                                            lifecycleScope.launch {
                                                val signInResult =
                                                    googleAuthUiClient.signInWithIntent(
                                                        intent = result.data ?: return@launch
                                                    )
                                                viewModel.onSignInResult(signInResult)
                                            }
                                        }
                                    }
                                )
                                LaunchedEffect(key1 = state.isSignInSuccessful) {
                                    Log.d("state of sign in ", state.isSignInSuccessful.toString())
                                    if (state.isSignInSuccessful) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Sign in successful",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        navController.navigate(Graph.HOME)
                                        viewModel.resetState()
                                    }
                                }
                                LoginScreen(
                                    state = state,
                                    onSignInClick = {
                                        lifecycleScope.launch {
                                            val signInIntentSender = googleAuthUiClient.signIn()
                                            launcher.launch(
                                                IntentSenderRequest.Builder(
                                                    signInIntentSender ?: return@launch
                                                ).build()
                                            )
                                        }

                                    }
                                )
                            }
                         composable(route = Graph.HOME){
                             AppContent(navController = rememberNavController(), googleAuthUiClient = googleAuthUiClient)
                         }

                        }


                    }


                }


            }
        }
    }
}
