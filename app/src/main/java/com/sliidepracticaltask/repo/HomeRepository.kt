package com.sliidepracticaltask.repo

import com.sliidepracticaltask.model.request.*
import com.sliidepracticaltask.network.ApiClient
import com.sliidepracticaltask.network.resource.getResult
import com.sliidepracticaltask.utils.formatTo
import java.util.*

object HomeRepository {
    suspend fun getUserList()  = ApiClient.service.getUsers(
    ).getResult()

    suspend fun addUser(request: AddUserRequest) =
        ApiClient.service.addUser(request).getResult()

    suspend fun deleteUser(id: Int) =
        ApiClient.service.deleteUser(id).getResult()


}