package com.sliidepracticaltask.network

import com.sliidepracticaltask.model.request.*
import com.sliidepracticaltask.model.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @POST("users")
    fun addUser(
        @Body body: AddUserRequest
    ): Call<AddUserResponse>

    @GET("users")
    fun getUsers(
    ): Call<UserListResponse>

    @DELETE("users/{id}")
    fun deleteUser(
        @Path(
            value = "id",
            encoded = true
        ) id: Int
    ): Call<Void>

}
