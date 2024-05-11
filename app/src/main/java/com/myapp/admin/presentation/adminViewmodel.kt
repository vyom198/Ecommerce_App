package com.myapp.admin.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.core.common.ResultState
import com.myapp.core.dataprovider.RealtimeModelResponse
import com.myapp.core.dataprovider.RealtimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import javax.inject.Inject
@HiltViewModel
class adminViewmodel @Inject constructor (
private  val repo : RealtimeRepository
):ViewModel() {
    private val _state = MutableStateFlow(ProductState())
    val state = _state.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    fun loadStuff() {
       viewModelScope.launch {
           _isLoading.value = true
           repo.getItems()
           delay(3000)
           _isLoading.value = false
       }
        Log.d("viewmodel","refresh")
   }
    fun insert(items: RealtimeModelResponse.RealtimeItems) {
        viewModelScope.launch {  repo.insert(items)}}

    private val _updateRes:MutableStateFlow<RealtimeModelResponse> =MutableStateFlow(
        RealtimeModelResponse(
            item = RealtimeModelResponse.RealtimeItems(),
        )
    )
    val updateRes  = _updateRes.asStateFlow()

    fun setData(data:RealtimeModelResponse){
        _updateRes.value = data
    }

    init {

        viewModelScope.launch {
            repo.getItems().collect{
                when(it){
                    is ResultState.Success -> {
                        _state.value = ProductState(
                            item = it.data
                        )
                    }
                    is ResultState.Failure->{
                        _state.value = ProductState(
                            error = it.toString()
                        )
                    }
                    ResultState.Loading->{
                        _state.value = ProductState(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    fun delete(key:String) = repo.delete(key)
    fun update(item:RealtimeModelResponse) = repo.update(item)


}


data class ProductState(
    val item: List<RealtimeModelResponse> = emptyList(),
    val error:String ? = "error",
    val isLoading:Boolean = false
)
