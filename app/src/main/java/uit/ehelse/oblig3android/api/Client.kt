package uit.ehelse.oblig3android.api

import android.util.Log
import arrow.core.Either
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 *  We just hardcode some credentials for now to use for demoing the app...
 */

const val USERNAME = "user"
const val PASSWORD = "password"
const val BASE_URL = "http://192.168.4.176:8080"

object TokenManager {
    var token: String? = null
    private var expiresAt: Long? = null
    var username: String? = null
    var password: String? = null

    @JvmName("getToken1")
    suspend fun getToken(): String? = withContext(Dispatchers.IO) {
        if (token == null) {
            val res = httpClient().authorize(username ?: "", password ?: "").map {
                token = it.token
                expiresAt = it.timeToLive
                token
            }.mapLeft {
                token = null
            }
        }

        if (System.currentTimeMillis() >= (expiresAt ?: 0)) {
            val res = httpClient().authorize(username ?: "", password ?: "").map {
                token = it.token
                expiresAt = it.timeToLive
                token
            }.mapLeft {
                token = null
            }
        }
        token
    }
}
interface AppHttpClient {
    suspend fun authorize(username: String, password: String): Either<ApiError, LoginResponse>

    suspend fun registerNewSymptoms(registerNewSymptomsRequest: RegisterNewSymptomsRequest): String

    suspend fun getAllPatients(): Either<ApiError, EndpointResources<IdResource<String>>>

    suspend fun registerPatient(registerPatientRequest: PatientModelRequest): Either<ApiError, Boolean>

    suspend fun getAllRegisteredSymptoms(): EndpointResources<IdResource<Long>>

    suspend fun getAllRegisteredPatientsForWard(wardId: String): EndpointResources<IdResource<String>>

    suspend fun deletePatient(url: String): Either<ApiError, Boolean>

    suspend fun updateWardIdForPatient(url: String, wardId: String): Either<ApiError, Boolean>

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
    override suspend fun authorize(username: String, password: String): Either<ApiError, LoginResponse> {
        val response = client.use { handler ->
            handler.post("$BASE_URL/login") {
                 setBody(LoginRequest(username, password)) //
            header("Content-Type", "application/json")
            }
        }

        if (response.status == HttpStatusCode.OK) {
            return Either.Right(response.body())
        } else {
            return Either.Left(ApiErrorType.Unauthorized)
        }
    }

    override suspend fun registerNewSymptoms(registerNewSymptomsRequest: RegisterNewSymptomsRequest): String =
        client.use { handler ->
            handler.post("$BASE_URL/symptoms") {
                setBody(registerNewSymptomsRequest)
                header("Content-Type", "application/json")
                header("Authorization", "Bearer ${TokenManager.getToken()}")
            }
        }.body()

    override suspend fun getAllPatients(): Either<ApiError, EndpointResources<IdResource<String>>> = client.use { handler ->
        val response = handler.get("$BASE_URL/patients") {
            header("Authorization", "Bearer ${TokenManager.getToken()}")
        }
        Log.d("TOKEN", "Token: ${TokenManager.getToken()}")
        if (response.status.isSuccess()) {
            Either.Right(response.body())
        } else {
            when (response.status)  {
                HttpStatusCode.NoContent -> Either.Left(ApiErrorType.NotFound("No patients found"))
                else -> Either.Left(ApiErrorType.Unauthorized)
            }
        }
    }

    override suspend fun registerPatient(registerPatientRequest: PatientModelRequest): Either<ApiError, Boolean> = client.use { handler ->
        val response = handler.post("$BASE_URL/patients") {
            setBody(registerPatientRequest)
            header("Content-Type", "application/json")
            header("Authorization", "Bearer ${TokenManager.getToken()}")
        }
        if (response.status.isSuccess()) {
            Either.Right(true)
        } else {
            when (response.status)  {
                HttpStatusCode.UnprocessableEntity -> Either.Left(ApiErrorType.InternalServerError)
                else -> Either.Left(ApiErrorType.Unauthorized)
            }
        }

    }

    override suspend fun getAllRegisteredSymptoms(): EndpointResources<IdResource<Long>> {
        return client.use { handler ->
            handler.get("$BASE_URL/symptoms") {
                header("Authorization", "Bearer ${TokenManager.getToken()}")
            }.body()
        }
    }

    override suspend fun getAllRegisteredPatientsForWard(wardId: String): EndpointResources<IdResource<String>> {
        return client.use { handler ->
            handler.get("$BASE_URL/patients?wardId=$wardId") {
                header("Authorization", "Bearer ${TokenManager.getToken()}")
            }.body()
        }
    }

    override suspend fun deletePatient(url: String): Either<ApiError, Boolean> {
        return client.use { handler ->
            val response = handler.delete(url) {
                header("Authorization", "Bearer ${TokenManager.getToken()}")
            }
            if (response.status.isSuccess()) {
                Either.Right(true)
            } else {
                when (response.status)  {
                    HttpStatusCode.UnprocessableEntity -> Either.Left(ApiErrorType.InternalServerError)
                    else -> Either.Left(ApiErrorType.Unauthorized)
                }
            }
        }
    }

    override suspend fun updateWardIdForPatient(url: String, wardId: String): Either<ApiError, Boolean> = client.use { handler ->
        val response = handler.put(url) {
            setBody(UpdateWardIdRequest(wardId))
            header("Content-Type", "application/json")
            header("Authorization", "Bearer ${TokenManager.getToken()}")
        }
        if (response.status.isSuccess()) {
            Either.Right(true)
        } else {
            when (response.status)  {
                HttpStatusCode.UnprocessableEntity -> Either.Left(ApiErrorType.InternalServerError)
                else -> Either.Left(ApiErrorType.Unauthorized)
            }
        }

    }
}

@Serializable
data class UpdateWardIdRequest(val wardId: String)


@Serializable
data class LoginRequest(
    val externalIdentifier: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String,
    val timeToLive: Long
)


@Serializable
data class RegisterNewSymptomsRequest(
    val externalId: String,
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


@Serializable
data class EndpointResources<T>(
    val resources: List<T?>
)

@Serializable
enum class SupportedHttpMethod(val rel: String) {
    GET("self"),
    PUT("update"),
    DELETE("delete");
    companion object {
        fun getAll(): List<SupportedHttpMethod> = listOf(GET, PUT, DELETE)
    }
}

@Serializable
data class IdResource<T>(
    val id: T,
    val name: String,
    @SerialName("_links")
    val links: List<EndpointLink>,
)
@Serializable
data class EndpointLink(
    val rel: String,
    val method: SupportedHttpMethod,
    val href: String,
)
interface ApiError

@Serializable
sealed class ApiErrorType : ApiError {
    @Serializable
    object Unauthorized : ApiErrorType()
    object Forbidden : ApiErrorType()
    @Serializable
    data class NotFound(val error: String) : ApiErrorType()
    object BadRequest : ApiErrorType()
    object InternalServerError : ApiErrorType()
    object Unknown : ApiErrorType()
}

fun main() {
    val json = """
        { "error": "NotFound(item=test not found in database)" }
    """.trimIndent()

    val jsonT: ApiErrorType.NotFound = Json.decodeFromString(json)

    println(jsonT)
}

@Serializable
data class PatientModelRequest(
    val externalId: String,
    val wardId: String,
    val internalId: String? = null,
)