package com.tifd.projectcomposed.data.model;
import retrofit2.http.GET
import retrofit2.http.Path
public interface APIService
{
    @GET("users/{username}")
    suspend fun getDetailUser(@Path("username") username:String) : Profile
}
