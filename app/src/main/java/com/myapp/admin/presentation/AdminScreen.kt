package com.myapp.admin.presentation

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.myapp.shopify.R
import org.jetbrains.annotations.Async

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AdminScreen(
    viewmodel: adminViewmodel,
    onBackClick : () -> Unit,
    onAddClick : () -> Unit,
    onRefresh : ()-> Unit
) {
    val state by viewmodel.state.collectAsState()
    val isLoading by viewmodel.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar =  {
            TopAppBar(title = {
                Text(text = "Products")
            }, navigationIcon =  {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back", modifier =
                Modifier.clickable {
                    onBackClick.invoke()
                })
            },
                actions = {
                    IconButton ( onClick = onAddClick
                    ){
                        Icon(imageVector = Icons.Default.Add, contentDescription = "addProduct")
                    }
                }

            )

        }
    ){
        SwipeRefresh(state =swipeRefreshState , onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize().padding(it),
             )  {
            if (state.isLoading){
              Box(modifier = Modifier.fillMaxSize(),
                  contentAlignment = Alignment.Center){
                  CircularProgressIndicator()
              }

          }else{
              LazyColumn{
                    items(state.item){
                        Row(
                            modifier = Modifier.fillMaxWidth(),

                            ) {
                            AsyncImage(
                                model = it.item?.image?.toUri() ?: Uri.EMPTY,
                                modifier = Modifier
                                    .height(70.dp)
                                    .width(200.dp),
                                contentDescription = "null",
                                placeholder = painterResource(id = R.drawable.img)
                            )
                            Spacer(modifier = Modifier.width(50.dp))
                            Text(text = it.item?.product.toString())

                        }

                    }
                }
            }


          }

      }


    }
