package com.sliidepracticaltask.network.resource

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.livinglifetechway.k4kotlin.core.err
import com.sliidepracticaltask.model.response.APIError
import com.livinglifetechway.k4kotlin_retrofit.enqueueDeferredResponse
import com.sliidepracticaltask.model.response.APIErrorFields
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call

fun <R, F> CoroutineScope.simpleNetworkCall(
    data: R? = null,
    func: suspend () -> Result<R, F>
): LiveData<Resource<R, F>> {
    val liveData = MutableLiveData<Resource<R, F>>()
    liveData.value = Resource.empty()
    launch {
        withContext(Dispatchers.Main) {
            liveData.value = Resource.loading(data)
            val result = func.invoke()
            liveData.value = when (result) {
                is Success -> {
                    Resource.success(result.data)
                }
                is Failure -> Resource.error(throwable = result.throwable, errorData = result.error)
            }
        }
    }
    return liveData
}

suspend fun <T : Any, S : T> Call<S>.getResult(): Result<T, APIError> {
    return try {
        val response = this.enqueueDeferredResponse().await()
        when {
            // response.code() == 201 -> Failure(null, APIError("201", "Something went wrong!"))
            response.isSuccessful -> //Send success body
                Success(response.body())

            response.code() == 422 -> {
                try {
                    val json = JSONObject(response.errorBody()!!.string())
                    Log.e("zxczxc", "json ------ $json")

                    var gson = Gson()
                    var apiErrorFields: APIErrorFields =
                        gson?.fromJson(json.toString(), APIErrorFields::class.java)
                    var errorString = ""
                    apiErrorFields.data!!.forEach {
                        errorString = "${
                            if (errorString.trim().isEmpty()) {
                                ""
                            } else {
                                "$errorString\n"
                            }
                        }${it!!.field} ${it.message}"
                    }
                    Failure(
                        null, APIError(
                            "0", errorString
                        )
                    )
                } catch (e: Exception) {
                    Failure(null, APIError("0", "Something went wrong!"))
                }
            }

            else -> {
                try {
                    val json = JSONObject(response.errorBody()!!.string())
                    Failure(null, APIError("0", json.getJSONObject("data").getString("message")))
                } catch (e: Exception) {
                    Failure(null, APIError("0", "Something went wrong!"))
                }
            }
        }
    } catch (throwable: Throwable) {
        Log.e(TAG, "getResult: ", throwable)
        Failure(
            throwable,
            APIError(
                "0",
                "There was a problem connecting to the server. Please try again after sometime."
            )
        )
    }
}
