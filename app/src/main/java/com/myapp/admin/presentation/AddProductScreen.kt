package com.myapp.admin.presentation

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.myapp.core.dataprovider.RealtimeModelResponse
import com.myapp.shopify.R
import androidx.compose.material3.OutlinedButton as Button

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProduct(
    onBackClick : () -> Unit,
    adminViewmodel: adminViewmodel
) {
   Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 50.dp),
         horizontalAlignment = Alignment.CenterHorizontally

    ){
        var selectedImageUri by remember {
            mutableStateOf<Uri?>(null)
        }
        var selectedImageUris by remember {
            mutableStateOf<List<Uri>>(emptyList())
        }
        val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri -> selectedImageUri = uri }
        )
        val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(),
            onResult = { uris -> selectedImageUris = uris }
        )
        var name by remember {
            mutableStateOf("")
        }
        var description by remember {
            mutableStateOf("")
        }
        var price by remember {
            mutableStateOf("")
        }


            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly){

                item {
                    AsyncImage(model = selectedImageUri, contentDescription = null,modifier =
                    Modifier
                        .fillMaxWidth(0.75f)
                        .height(250.dp)
                        .clickable {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        placeholder = painterResource(id = R.drawable.img),
                        contentScale = ContentScale.Crop)
                    OutlinedTextField(value = name , onValueChange = {
                        name = it
                    }, modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .clip(RoundedCornerShape(3.dp)) )
                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedTextField(value = description , onValueChange ={
                        description= it
                    } , modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .clip(RoundedCornerShape(3.dp)))

                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedTextField(value = price , onValueChange ={
                        price = it
                    }, modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .clip(RoundedCornerShape(3.dp)) )

                }


            }
             Spacer(modifier = Modifier.height(14.dp))
           Button(onClick = {
           adminViewmodel.insert(items = RealtimeModelResponse.RealtimeItems(
               product = name , description = description, price = price , image = selectedImageUri.toString()
           ))
               Log.d("AddProductScreen", "product added")
               onBackClick.invoke()
          }
           ) {
           Text(text = "save")
             }



          }




}