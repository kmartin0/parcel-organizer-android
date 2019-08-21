package com.km.parceltracker.api

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.km.parceltracker.repository.UserRepository
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ParcelTrackerApi {
    companion object {
        // The base url off the api.
        private const val baseUrl = "http://10.0.2.2:8080/"

        /**
         * @return [ParcelTrackerApi] The service class off the retrofit client.
         */
        fun createApi(context: Context): ParcelTrackerApiService {
            // Create the Retrofit instance
            val numbersApi = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttpClientApi(context))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

            // Return the Retrofit NumbersApiService
            return numbersApi.create(ParcelTrackerApiService::class.java)
        }

        fun createRefreshTokenApi(): RefreshTokenApiService {
            // Create the Retrofit instance
            val numbersApi = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttpClientRefreshTokenApi())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

            // Return the Retrofit NumbersApiService
            return numbersApi.create(RefreshTokenApiService::class.java)
        }

        private fun getOkHttpClientApi(context: Context): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(getAccessTokenInterceptor(context))
                .authenticator(getRefreshTokenAuthenticator(context))
                .build()
        }

        private fun getOkHttpClientRefreshTokenApi(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(getBasicAuthInterceptor())
                .build()
        }

        private fun getAccessTokenInterceptor(context: Context): Interceptor {
            val userRepository = UserRepository(context)
            val accessToken = userRepository.getLoggedInUser()?.authorization?.accessToken

            // TODO: Test what happens in calls that don't need auth header (i.e. register)
            return Interceptor {
                val request = it.request().newBuilder()
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()

                it.proceed(request)
            }
        }

        private fun getBasicAuthInterceptor(): Interceptor {
            val basic = Credentials.basic("parcel-tracker-android", "secret")
            return Interceptor {
                val request = it.request().newBuilder()
                    .addHeader("Authorization", basic)
                    .build()

                it.proceed(request)
            }
        }

        private fun getRefreshTokenAuthenticator(context: Context): Authenticator {
            return object : Authenticator {
                override fun authenticate(route: Route?, response: Response): Request? {
                    val body = response.body()?.string()
                    val responseBody = Gson().fromJson(body, ApiError::class.java)
                    return if (responseBody.error == "invalid_token") {
                        val userRepository = UserRepository(context)
                        val refreshToken = userRepository.getLoggedInUser()?.authorization?.refreshToken ?: return null
                        val newAuth = userRepository.refreshAccessToken(refreshToken).blockingGet()
                        userRepository.setUserAuthentication(newAuth)

                        response.request().newBuilder()
                            .header("Authorization", "Bearer ${newAuth.accessToken}")
                            .build()
                    } else {
                        // TODO: Test with other authentication errors such as login
                        null
                    }
                }
            }
        }
    }
}