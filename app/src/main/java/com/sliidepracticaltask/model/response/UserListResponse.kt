package com.sliidepracticaltask.model.response

data class UserListResponse(
    var data: ArrayList<User>?,
    var meta: Meta?
) {
    data class User(
        var email: String?,
        var gender: String?,
        var id: Int?,
        var name: String?,
        var status: String?
    )

    data class Meta(
        var pagination: Pagination?
    ) {
        data class Pagination(
            var limit: Int?,
            var links: Links?,
            var page: Int?,
            var pages: Int?,
            var total: Int?
        ) {
            data class Links(
                var current: String?,
                var next: String?,
                var previous: String?
            )
        }
    }
}