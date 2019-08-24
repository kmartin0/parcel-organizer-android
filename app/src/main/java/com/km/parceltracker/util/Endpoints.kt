package com.km.parceltracker.util

import okhttp3.Request

class Endpoints {
    companion object {
        const val PARCELS = "/parcels"
        const val OAUTH_TOKEN = "/oauth/token"
        const val USERS = "/users"
        const val GET_PARCEL_STATUS_BY_STATUS = "/parcel-statuses/status/{status}"

        fun shouldBasicAuth(request: Request): Boolean {
            return when (request.url().url().path) {
                PARCELS -> false
                OAUTH_TOKEN -> true
                USERS -> false
                GET_PARCEL_STATUS_BY_STATUS -> false
                else -> false
            }
        }

        fun shouldBearerTokenAuth(request: Request): Boolean {
            return when (request.url().url().path) {
                PARCELS -> true
                OAUTH_TOKEN -> false
                USERS -> false
                GET_PARCEL_STATUS_BY_STATUS -> false
                else -> false
            }
        }
    }
}