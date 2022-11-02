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
class PatientListViewModel @Inject constructor(application: Application) : AndroidViewModel(application)  {
    private val _patients = MutableLiveData<List<PatientModel>>()
    val patients: MutableLiveData<List<PatientModel>>
        get() = _patients

    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String>
        get() = _error


    // get patients async
    fun getPatients() {
        viewModelScope.launch {
            val resources = httpClient().getAllPatients().map {
                Log.d("PatientListViewModel", "getPatients: \n ${Json.encodeToString(it.resources)}")
                _patients.value = it.resources.map {
                    PatientModel.tryFromResource(it)
                }
            }.mapLeft {
                Log.d("PatientListViewModel", "getPatients: $it")
            }

        }
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