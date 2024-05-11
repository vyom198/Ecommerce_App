package com.myapp.core.dataprovider

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.myapp.core.common.COLLECTION_PATH_NAME
import com.myapp.core.common.PLEASE_CHECK_INTERNET_CONNECTION
import com.myapp.core.common.ResultState
import com.myapp.core.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class RealtimeDbRepository @Inject constructor(
    private val db: FirebaseFirestore,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RealtimeRepository{

    override suspend fun insert(item: RealtimeModelResponse.RealtimeItems): ResultState<Any> {
        return try {
            withContext(ioDispatcher) {

                val addTaskTimeout = withTimeoutOrNull(10000L) {
                    db.collection(COLLECTION_PATH_NAME)
                        .add(item)
                }

                if (addTaskTimeout == null) {
                    Log.d("ERROR: ", PLEASE_CHECK_INTERNET_CONNECTION)
                    ResultState.Failure(IllegalStateException(PLEASE_CHECK_INTERNET_CONNECTION))
                }
                ResultState.Success(Unit)
            }
        } catch (exception: Exception) {
            Log.d("ERROR: ", "$exception")

            ResultState.Failure(exception = exception)
        }
    }


    override suspend fun getItems(): Flow<ResultState<List<RealtimeModelResponse>>> = callbackFlow{
        trySend(ResultState.Loading)
        db.collection(COLLECTION_PATH_NAME)
            .get()
            .addOnSuccessListener {
                val items =  it.map { data->
                    RealtimeModelResponse(
                        item = RealtimeModelResponse.RealtimeItems(
                            product = data["product"] as String?,
                            description = data["description"] as String?,
                            price = data["price"] as String ? ,
                            image = data["image"] as String?
                        ),
                        key = data.id
                    )
                }
                trySend(ResultState.Success(items))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }

    override fun delete(key: String): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        db.collection(COLLECTION_PATH_NAME)
            .document(key)
            .delete()
            .addOnCompleteListener {
                if(it.isSuccessful)
                    trySend(ResultState.Success("Deleted successfully.."))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    override fun update(item: RealtimeModelResponse): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        val map = HashMap<String,Any>()
        map["product"] = item.item?.product!!
        map["description"] = item.item.description!!
        map["price"] = item.item.price!!
        map["image"] = item.item.image!!
        db.collection(COLLECTION_PATH_NAME)
            .document(item.key!!)
            .update(map)
            .addOnCompleteListener {
                if(it.isSuccessful)
                    trySend(ResultState.Success("Update successfully..."))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        awaitClose {
            close()
        }
    }


}
