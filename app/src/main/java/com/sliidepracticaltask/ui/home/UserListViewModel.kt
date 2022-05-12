package com.sliidepracticaltask.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sliidepracticaltask.model.request.AddUserRequest
import com.sliidepracticaltask.model.response.UserListResponse
import com.sliidepracticaltask.network.resource.simpleNetworkCall
import com.sliidepracticaltask.repo.HomeRepository
import com.sliidepracticaltask.utils.coroutine.CoroutineScopedViewModel

class UserListViewModel : CoroutineScopedViewModel() {

    private val mutableUsers = MutableLiveData<UserListResponse>()
    val liveDataUsers: LiveData<UserListResponse>
        get() = mutableUsers


    private val mutableUserList = MutableLiveData<ArrayList<UserListResponse.User>>()
    val liveDataUserList: LiveData<ArrayList<UserListResponse.User>>
        get() = mutableUserList

    fun getUsersListData() = simpleNetworkCall {
        HomeRepository.getUserList()
    }

    fun setUsersList(item: UserListResponse?) {
        this.mutableUsers.value = item
        this.mutableUserList.value = item!!.data!!
    }

    fun addUser(addUserRequest: AddUserRequest) = simpleNetworkCall {
        HomeRepository.addUser(addUserRequest)
    }
    fun deleteUser(id: Int) = simpleNetworkCall {
        HomeRepository.deleteUser(id)
    }

}
