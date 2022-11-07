package uit.ehelse.oblig3android.patientList

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import uit.ehelse.oblig3android.api.IdResource
import uit.ehelse.oblig3android.api.httpClient
import javax.inject.Inject


@HiltViewModel
class PatientListViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    private val _patientsResponseData = MutableLiveData<List<PatientModel>>()
    val patients: LiveData<List<PatientModel>>
        get() = _patientsResponseData

    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String>
        get() = _error

    val patientsList: List<String>
        get() = _patientsResponseData.value?.map {
            it.id
        } ?: emptyList()


    // get patients async
    suspend fun getPatientsAsync() {
        viewModelScope.launch {
            val resources = httpClient().getAllPatients().map {
                Log.d("PatientListViewModel", "getPatients: \n ${Json.encodeToString(it.resources)}")
                _patientsResponseData.value = it.resources.map {
                    PatientModel.tryFromResource(it)
                }
            }.mapLeft {
                Log.d("PatientListViewModel", "getPatients: $it")
            }
        }.join()
    }
}

class PatientModel(
    val id: String
) {

    companion object {
        fun tryFromResource(idResource: IdResource<String>?): PatientModel {
            idResource?.id.let { id ->
                return PatientModel(id!!)
            }
        }
    }
}