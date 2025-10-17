package com.incepta.core.network


/**
 * Created by Abdullah on 18/5/25.
 */

open class AppException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {
}