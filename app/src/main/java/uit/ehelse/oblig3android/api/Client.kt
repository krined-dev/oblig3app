package uit.ehelse.oblig3android.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 *  We just hardcode some credentials for now to use for demoing the app...
 */

const val USERNAME = "user01"
const val PASSWORD = "password"

interface AppHttpClient {
    suspend fun authorize(loginRequest: LoginRequest): LoginResponse

    suspend fun registerNewSymptoms(registerNewSymptomsRequest: RegisterNewSymptomsRequest): String

}
fun httpClient() = object : AppHttpClient {
    val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }
    override suspend fun authorize(loginRequest: LoginRequest): LoginResponse {
        return client.use { handler ->
            handler.post("http://localhost:8080/login") {
                 setBody(LoginRequest(USERNAME, PASSWORD)) // TODO: Change to loginRequest
            header("Content-Type", "application/json")
            }
        }.body()
    }

    override suspend fun registerNewSymptoms(registerNewSymptomsRequest: RegisterNewSymptomsRequest): String {
        val token = authorize(LoginRequest(USERNAME, PASSWORD))
        return client.use { handler ->
            handler.post("http://localhost:8080/symptoms") {
                setBody(registerNewSymptomsRequest)
                header("Content-Type", "application/json")
                header("Authorization", "Bearer ${token.token}")
            }
        }.body()
    }
}

@Serializable
data class LoginRequest(
    val externalIdentifier: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String
)


@Serializable
data class RegisterNewSymptomsRequest(
    val patientId: String,
    val symptoms: List<Symptom>,
    val wardId: String
)

@Serializable
enum class Symptom(val details: Details) {
    FEVER(Details.COMMON),
    COUGHING(Details.COMMON),
    TIREDNESS(Details.COMMON),
    LOSS_OF_TASTE_AND_SMELL(Details.COMMON),
    SORE_THROAT(Details.LESS_COMMON),
    HEADACHE(Details.LESS_COMMON),
    ACHES_AND_PAINS(Details.LESS_COMMON),
    DIARRHOEA(Details.LESS_COMMON),
    RASHES_OR_DISCOLORATION(Details.LESS_COMMON),
    BREATHING_COMPLICATIONS(Details.SERIOUS),
}

/**
 * [Details] Details about the symptom that is tagged with a score
 */
enum class Details(val sore: Int){
    COMMON(3), LESS_COMMON(1), SERIOUS(2)
}

// EXAMPLE USAGE:
//fun main() {
//    val client = httpClient()
//    runBlocking {
//        val response = client.authorize(LoginRequest(USERNAME, PASSWORD))
//        println(response)
//    }
//}