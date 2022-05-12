package com.sliidepracticaltask.model.response

data class APIErrorFields(
    var data: List<Data?>?,
    var meta: Any?
) {
    data class Data(
        var field: String?,
        var message: String?
    )
}