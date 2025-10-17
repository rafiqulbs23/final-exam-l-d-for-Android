package dev.rafiqulislam.core.data.repository


interface TokenRepository {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String
    suspend fun clearToken()
    suspend fun saveUsername(username: String)
    suspend fun getUsername() : String
    suspend fun getEmpId() : String
    suspend fun saveEmpId(empId: String)
    suspend fun isLoggedOut() : Boolean
    suspend fun setLoggedOut(boolean: Boolean)
    suspend fun savePassword(password: String)
    suspend fun getPassword() : String
    suspend fun saveLastSyncDateTime(dateTime: String)
    suspend fun getLastSyncDateTime(): String
}