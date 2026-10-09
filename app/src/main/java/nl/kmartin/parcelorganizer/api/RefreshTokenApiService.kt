package nl.kmartin.parcelorganizer.api

import nl.kmartin.parcelorganizer.model.OAuth2Credentials
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RefreshTokenApiService {

    @POST(Endpoints.OAUTH_TOKEN)
    @FormUrlEncoded
    fun refreshToken(@Field("refresh_token") refreshToken: String, @Field("grant_type") grantType: String = "refresh_token"): Single<OAuth2Credentials>

}