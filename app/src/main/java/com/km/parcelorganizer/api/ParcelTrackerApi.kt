package com.km.parcelorganizer.api

import android.content.Context
import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import com.google.gson.Gson
import com.km.parcelorganizer.BuildConfig
import com.km.parcelorganizer.api.error.ApiError
import com.km.parcelorganizer.enums.ApiErrorEnum
import com.km.parcelorganizer.repository.TokenRepository
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ParcelTrackerApi {
    companion object {
        // The base url off the api.
//        private const val baseUrl = "http://10.0.2.2:8080/"
        private const val baseUrl = "https://api.parcelorganizer.kmartin.nl//"

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

        fun createRefreshTokenApi(context: Context): RefreshTokenApiService {
            // Create the Retrofit instance
            val numbersApi = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttpClientRefreshTokenApi(context))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

            // Return the Retrofit NumbersApiService
            return numbersApi.create(RefreshTokenApiService::class.java)
        }

        private fun getOkHttpClientApi(context: Context): OkHttpClient {
            val okHttpClientBuilder = OkHttpClient.Builder()
                .addInterceptor(authHeaderInterceptor(context))
                .addInterceptor(langHeaderInterceptor())
                .authenticator(getRefreshTokenAuthenticator(context))

            if (BuildConfig.DEBUG) {
                okHttpClientBuilder.addInterceptor(
                    HttpLoggingInterceptor().setLevel(
                        HttpLoggingInterceptor.Level.BODY
                    )
                )
            }

            return okHttpClientBuilder.build()
        }

        private fun getOkHttpClientRefreshTokenApi(context: Context): OkHttpClient {
            val okHttpClientBuilder = OkHttpClient.Builder()
                .addInterceptor(authHeaderInterceptor(context))
                .addInterceptor(langHeaderInterceptor())

            if (BuildConfig.DEBUG) {
                okHttpClientBuilder.addInterceptor(
                    HttpLoggingInterceptor().setLevel(
                        HttpLoggingInterceptor.Level.BODY
                    )
                )
            }

            return okHttpClientBuilder.build()
        }

        private fun authHeaderInterceptor(context: Context): Interceptor {
            return Interceptor {
                val request = it.request()
                val requestBuilder = request.newBuilder()

                if (Endpoints.shouldBasicAuth(request)) {
                    val basic = Credentials.basic(
                        BuildConfig.clientSecretUsername,
                        BuildConfig.clientSecretPassword
                    )
                    requestBuilder.addHeader("Authorization", basic)

                    return@Interceptor it.proceed(requestBuilder.build())
                }

                if (Endpoints.shouldBearerTokenAuth(request)) {
                    val tokenRepository = TokenRepository(context)
                    val accessToken = tokenRepository.getUserOAuth2Credentials()?.accessToken
                    requestBuilder.addHeader("Authorization", "Bearer $accessToken")

                    return@Interceptor it.proceed(requestBuilder.build())
                }

                it.proceed(request)
            }
        }

        private fun langHeaderInterceptor(): Interceptor {
            return Interceptor {
                val locale =
                    ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0)
                val request = it.request()

                it.proceed(
                    request.newBuilder()
                        .addHeader("Accept-Language", locale.language)
                        .build()
                )
            }
        }

        private fun getRefreshTokenAuthenticator(context: Context): Authenticator {
            return object : Authenticator {
                override fun authenticate(route: Route?, response: Response): Request? {
                    val body = response.peekBody(Long.MAX_VALUE).string()
                    val responseBody = Gson().fromJson(body, ApiError::class.java)
                    return if (responseBody.error == ApiErrorEnum.invalid_token) {
                        val tokenRepository = TokenRepository(context)
                        val refreshToken =
                            tokenRepository.getUserOAuth2Credentials()?.refreshToken ?: return null

                        val newAuth = tokenRepository.refreshAccessToken(refreshToken).blockingGet()
                        tokenRepository.setUserOAuth2Credentials(newAuth)

                        response.request().newBuilder()
                            .header("Authorization", "Bearer ${newAuth.accessToken}")
                            .build()
                    } else {
                        null
                    }
                }
            }
        }
    }
}