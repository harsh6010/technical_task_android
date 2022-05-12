package com.sliidepracticaltask.model.request

data class AddUserRequest(
    var email: String?,
    var gender: String?,
    var name: String?,
    var status: String?
)