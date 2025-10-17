package dev.rafiqulislam.core.base



open class BaseResponse<T>(
    val status: String,
    val statusCode: Int,
    val message: String,
    val data: T? = null,
    val error: Array<Any>? = null,
) {
    companion object {
        fun <T> success(data: T): BaseResponse<T> {
            return BaseResponse(status = "Success", 200, "Successful message", data)
        }

        fun <T> error(message: String): BaseResponse<T> {
            return BaseResponse(status = "Error", 500, message)
        }
    }
}
