package uit.ehelse.oblig3android.newPatient

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uit.ehelse.oblig3android.api.PatientModelRequest
import uit.ehelse.oblig3android.api.httpClient
import javax.inject.Inject


@HiltViewModel
class NewPatientViewModel @Inject constructor(application: android.app.Application) : AndroidViewModel(application) {

    private val _patientSsn = MutableLiveData<String>()
    val patientSsn: LiveData<String>
        get() = _patientSsn

    fun setPatientSsn(ssn: String): Boolean {
        val valid = validateSsn(ssn.trim())
        Log.d("NewPatientViewModel", "setPatientSsn: $ssn, valid: $valid")
        if (valid) {
            _patientSsn.value = ssn.trim()
        }
        return valid
    }

    private val _wardId = MutableLiveData<String>()
    val wardId: LiveData<String>
        get() = _wardId

    fun setWardId(wardId: String) {
        _wardId.value = wardId
    }

    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response



    suspend fun registerPatient() = withContext(Dispatchers.IO) {
        if (patientSsn.value != null && wardId.value != null) {
            val patient = PatientModelRequest(patientSsn.value!!, wardId.value!!)
            httpClient().registerPatient(patient).map {
                _response.postValue("Patient registered")
            }.mapLeft {
                _response.postValue("Error: Already registered") // Not always true, but for demo purposes. Could be other errors
            }
        }
    }

}

fun validateSsn(ssn: String): Boolean {
    return ssn.length == 11 && ssn.all { it.isDigit() }

    // Could also check control numbers, but it would mean that you would have to check if the ssn is valid before input
    // Not good for testing
}