package dev.rafiqulislam.core.base

import com.incepta.core.network.AppException
import com.incepta.core.network.exception.NetworkExceptionMapper
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject


open class BaseRepository @Inject constructor() {

    protected suspend fun <T> safeApiCall(apiCall: suspend () -> Response<BaseResponse<T>>): Result<T?> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.statusCode == 200) {
                    Result.Success(body.data)
                } else {
                    Result.Error(AppException(body?.message ?: "Response body is null"))
                }
            } else {
                val errorBody = response.errorBody()
                val errorMessage = errorBody?.string() ?: "Unknown error"
                Result.Error(AppException(errorMessage))
            }
        } catch (e: Exception) {
            Result.Error(mapException(e))
        }
    }

    private fun mapException(e: Exception): Exception {
        return when (e) {
            is HttpException -> NetworkExceptionMapper.codeToException(e.code())
            is SocketTimeoutException -> SocketTimeoutException("No server response, request timed out")
            is IOException -> IOException("No internet connection")
            else -> AppException("Unknown error: ${e.message}")
        }
    }

    protected suspend fun <T> safeDbCall(dbCall: suspend () -> T): Result<T> {
        return try {
            Result.Success(dbCall())
        } catch (e: Exception) {

            Result.Error(AppException("Database operation failed: ${e.message}"))
        }
    }
}

inline fun <T, R> Result<T>.mapSuccess(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> Result.Error(exception)
    }
}