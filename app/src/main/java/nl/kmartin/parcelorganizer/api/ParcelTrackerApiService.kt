package nl.kmartin.parcelorganizer.api

import nl.kmartin.parcelorganizer.api.request.ChangePasswordRequestBody
import nl.kmartin.parcelorganizer.api.request.ForgotPasswordRequestBody
import nl.kmartin.parcelorganizer.api.request.RegisterParcelRequestBody
import nl.kmartin.parcelorganizer.api.request.RegisterUserRequestBody
import nl.kmartin.parcelorganizer.api.request.ResetPasswordRequestBody
import nl.kmartin.parcelorganizer.api.request.UpdateParcelRequestBody
import nl.kmartin.parcelorganizer.api.request.UpdateUserRequestBody
import nl.kmartin.parcelorganizer.enums.ParcelStatusEnum
import nl.kmartin.parcelorganizer.model.OAuth2Credentials
import nl.kmartin.parcelorganizer.model.Parcel
import nl.kmartin.parcelorganizer.model.ParcelStatus
import nl.kmartin.parcelorganizer.model.User
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ParcelTrackerApiService {

    @POST(Endpoints.PARCELS)
    fun saveParcel(@Body registerParcelRequestBody: RegisterParcelRequestBody): Single<Parcel>

    @GET(Endpoints.PARCELS)
    fun getParcels(): Single<List<Parcel>>

    @PUT(Endpoints.PARCELS)
    fun updateParcel(@Body updateParcelRequestBody: UpdateParcelRequestBody): Single<Parcel>

    @DELETE(Endpoints.DELETE_PARCEL)
    fun deleteParcel(@Path("id") id: Long): Completable

    @GET(Endpoints.GET_PARCEL_STATUS_BY_STATUS)
    fun getParcelStatusByStatus(@Path("status") parcelStatusEnum: ParcelStatusEnum): Single<ParcelStatus>

    @POST(Endpoints.OAUTH_TOKEN)
    @FormUrlEncoded
    fun authenticateUser(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String = "password"
    ): Single<OAuth2Credentials>

    @GET(Endpoints.USERS)
    fun getUser(@Header("Authorization") accessToken: String): Single<User>

    @POST(Endpoints.USERS)
    fun registerUser(@Body registerUserRequestBody: RegisterUserRequestBody): Single<User>

    @PUT(Endpoints.USERS)
    fun updateUser(@Body updateUserRequestBody: UpdateUserRequestBody) : Single<User>

    @POST(Endpoints.CHANGE_PASSWORD)
    fun changePassword(@Body changePasswordRequestBody: ChangePasswordRequestBody): Completable

    @POST(Endpoints.FORGOT_PASSWORD)
    fun forgotPassword(@Body forgotPasswordRequestBody: ForgotPasswordRequestBody): Completable

    @POST(Endpoints.RESET_PASSWORD)
    fun resetPassword(@Body resetPasswordRequestBody: ResetPasswordRequestBody): Completable
}