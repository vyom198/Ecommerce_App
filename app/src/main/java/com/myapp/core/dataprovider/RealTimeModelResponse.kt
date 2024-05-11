package com.myapp.core.dataprovider

data class RealtimeModelResponse(
    val item:RealtimeItems?,
    val key:String? = ""
){
    data class RealtimeItems(
        val product:String? = "",
        val description:String? = "",
        val price: String ? = "",
        val image: String? = null
    )
}
