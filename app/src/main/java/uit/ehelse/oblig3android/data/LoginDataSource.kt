package uit.ehelse.oblig3android.data

import kotlinx.coroutines.runBlocking
import uit.ehelse.oblig3android.api.TokenManager
import uit.ehelse.oblig3android.api.httpClient
import uit.ehelse.oblig3android.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            TokenManager.password = password
            TokenManager.username = username
            runBlocking {   TokenManager.getToken() }
            if (TokenManager.token != null) {
                val user = LoggedInUser(username, "")
                return Result.Success(user)
            }
            TokenManager.username = null
            TokenManager.password = null
            throw Exception("Invalid credentials")
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
        TokenManager.username = null
        TokenManager.password = null
        TokenManager.token = null
    }
}