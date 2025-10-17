package com.incepta.core.network.exception

import com.incepta.core.network.AppException


/**
 * Created by Abdullah on 18/5/25.
 */

object NetworkExceptionMapper {
    fun mapException(exception: Exception): AppException {
        return when (exception) {
            is UnauthorizedException -> UnauthorizedException(exception.message ?: "Unauthorized")
            is NoInternetException -> NoInternetException(
                exception.message ?: "No Internet Connection"
            )

            is ServerException -> ServerException(exception.message ?: "Server Error")
            is UnknownException -> UnknownException(exception.message ?: "Unknown Error")
            is NetworkTimeoutException -> NetworkTimeoutException(
                exception.message ?: "Network Timeout"
            )

            is NetworkConnectionException -> NetworkConnectionException(
                exception.message ?: "Network Connection Error"
            )

            is NetworkResponseException -> NetworkResponseException(
                exception.message ?: "Network Response Error"
            )

            is NetworkRequestException -> NetworkRequestException(
                exception.message ?: "Network Request Error"
            )

            is NetworkFailureException -> NetworkFailureException(
                exception.message ?: "Network Failure"
            )

            is NetworkErrorException -> NetworkErrorException(exception.message ?: "Network Error")
            else -> AppException("Unknown error: ${exception.message}")
        }
    }

    fun codeToException(code: Int): AppException {
        return when (code) {
            401 -> UnauthorizedException("Unauthorized access")
            500 -> ServerException("Server error")
            503 -> ServiceUnavailableException("Service unavailable")
            408 -> NetworkTimeoutException("Request timeout")
            429 -> TooManyRequestException("Too many requests")
            403 -> NetworkErrorException("Forbidden")
            404 -> NotFoundException("Not found")
            400 -> NetworkErrorException("Bad request")
            502 -> NetworkErrorException("Bad gateway")
            504 -> NetworkErrorException("Gateway timeout")
            else -> UnknownException("Unknown error")
        }
    }
}

open class NetworkException(
    message: String
): AppException(
    message = message
)

class ServiceUnavailableException (
    message: String
): NetworkException(
    message = message
)

class UnauthorizedException(
    message: String
): AppException(
    message = message
)

class NoInternetException(
    message: String
): AppException(
    message = message
)

class ServerException(
    message: String
): AppException(
    message = message
)

class UnknownException(
    message: String
): AppException(
    message = message
)

class NetworkTimeoutException(
    message: String
): AppException(
    message = message
)

class NetworkConnectionException(
    message: String
): AppException(
    message = message
)

class NetworkResponseException(
    message: String
): AppException(
    message = message
)
class NetworkRequestException(
    message: String
): AppException(
    message = message
)

class NetworkFailureException(
    message: String
): AppException(
    message = message
)
class NetworkErrorException(
    message: String
): AppException(
    message = message
)

class GatewayException(
    message: String
): AppException(
    message = message
)

class NotFoundException (
    message: String
): AppException(
    message = message
)

class TooManyRequestException (
    message: String
): AppException(
    message = message
)