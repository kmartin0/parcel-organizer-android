package com.km.parcelorganizer.api

import okhttp3.Request

class Endpoints {
    companion object {
        const val PARCELS = "/parcels"
        const val DELETE_PARCEL = "/parcels/{id}"
        const val OAUTH_TOKEN = "/oauth/token"
        const val USERS = "/users"
        const val GET_PARCEL_STATUS_BY_STATUS = "/parcel-statuses/status/{status}"
        const val CHANGE_PASSWORD = "/users/change-password"

        fun shouldBasicAuth(request: Request): Boolean {
            return when (request.url().url().path) {
                OAUTH_TOKEN -> true
                else -> false
            }
        }

        fun shouldBearerTokenAuth(request: Request): Boolean {
            val path = request.url().url().path
            val method = request.method()
            return when {
                path == PARCELS -> true
                path.startsWith(PARCELS) -> true
                path == USERS && method == "PUT" -> true
                path == CHANGE_PASSWORD -> true
                else -> false
            }
        }
    }
}