package com.km.parceltracker.util

import okhttp3.Request

class Endpoints {
    companion object {
        const val PARCELS = "/parcels"
        const val DELETE_PARCEL = "/parcels/{id}"
        const val OAUTH_TOKEN = "/oauth/token"
        const val USERS = "/users"
        const val GET_PARCEL_STATUS_BY_STATUS = "/parcel-statuses/status/{status}"

        fun shouldBasicAuth(request: Request): Boolean {
            return when (request.url().url().path) {
                OAUTH_TOKEN -> true
                else -> false
            }
        }

        fun shouldBearerTokenAuth(request: Request): Boolean {
            val path = request.url().url().path
            return when {
                path == PARCELS -> true
                path.startsWith(PARCELS) -> true
                else -> false
            }
        }
    }
}