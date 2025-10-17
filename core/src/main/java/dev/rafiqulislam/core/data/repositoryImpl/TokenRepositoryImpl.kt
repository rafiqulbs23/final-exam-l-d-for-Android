package dev.rafiqulislam.core.data.repositoryImpl

import dev.rafiqulislam.core.data.repository.TokenRepository
import dev.rafiqulislam.core.utils.CoreConstant
import dev.rafiqulislam.core.utils.DataStoreManager
import javax.inject.Inject


class TokenRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : TokenRepository{

    override suspend fun saveToken(token: String) {
        dataStoreManager.saveString(CoreConstant.TOKEN,token)
    }

    override suspend fun getToken(): String {
       return dataStoreManager.getString(CoreConstant.TOKEN)
    }

    override suspend fun clearToken() {
        dataStoreManager.clear(CoreConstant.TOKEN)
    }

    override suspend fun saveUsername(username: String) {
        dataStoreManager.saveString(CoreConstant.USERNAME,username)
    }

    override suspend fun getUsername(): String {
        return dataStoreManager.getString(CoreConstant.USERNAME)
    }

    override suspend fun getEmpId(): String {
        return dataStoreManager.getString(CoreConstant.EMP_ID)
    }

    override suspend fun saveEmpId(empId: String) {
        dataStoreManager.saveString(CoreConstant.EMP_ID, empId)
    }

    override suspend fun isLoggedOut(): Boolean {
        return  dataStoreManager.getBoolean(CoreConstant.LOGGED_STATUS, true)
    }

    override suspend fun setLoggedOut(boolean: Boolean) {
       dataStoreManager.saveBoolean(CoreConstant.LOGGED_STATUS, boolean)
    }

    override suspend fun savePassword(password: String) {
        dataStoreManager.saveString(CoreConstant.PASSWORD, password)
    }

    override suspend fun getPassword(): String {
        return dataStoreManager.getString(CoreConstant.PASSWORD)
    }

    override suspend fun saveLastSyncDateTime(dateTime: String) {
        dataStoreManager.saveString(CoreConstant.LAST_SYNC_DATETIME, dateTime)
    }

    override suspend fun getLastSyncDateTime(): String {
        return dataStoreManager.getString(CoreConstant.LAST_SYNC_DATETIME)
    }
}