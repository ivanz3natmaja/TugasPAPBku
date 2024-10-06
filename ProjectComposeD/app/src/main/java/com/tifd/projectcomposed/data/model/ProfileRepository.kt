package com.tifd.projectcomposed.data.model
import retrofit2.HttpException

class ProfileRepository {
    suspend fun getProfile(username: String): Profile {
        return try {
            val response = ApiConfig.getApiService().getDetailUser(username)
            response
        } catch (e: HttpException){
            throw Exception(e.response()?.errorBody()?.string())
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }
}