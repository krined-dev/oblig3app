package uit.ehelse.oblig3android.registerSymptoms

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import uit.ehelse.oblig3android.api.IdResource
import uit.ehelse.oblig3android.api.Symptom
import javax.inject.Inject

@HiltViewModel
class RegisterSymptomsViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    private val _selectedPatient = MutableLiveData<Patient>()
    val selectedPatient: LiveData<Patient>
        get() = _selectedPatient

    private val _fever = MutableLiveData<Symptoms>()
    val fever: LiveData<Symptoms>
        get() = _fever

    private val _coughing = MutableLiveData<Symptoms>()
    val coughing: LiveData<Symptoms>
        get() = _coughing

    private val _tiredness = MutableLiveData<Symptoms>()
    val tiredness: LiveData<Symptoms>
        get() = _tiredness

    private val _lossOfTasteAndOrSmell = MutableLiveData<Symptoms>()
    val lossOfTasteAndOrSmell: LiveData<Symptoms>
        get() = _lossOfTasteAndOrSmell

    private val _soreThroat = MutableLiveData<Symptoms>()
    val soreThroat: LiveData<Symptoms>
        get() = _soreThroat

    private val _headache = MutableLiveData<Symptoms>()
    val headache: LiveData<Symptoms>
        get() = _headache

    private val _achesAndPain = MutableLiveData<Symptoms>()
    val achesAndPain: LiveData<Symptoms>
        get() = _achesAndPain

    private val _diarrhoa = MutableLiveData<Symptoms>()
    val diarrhoa: LiveData<Symptoms>
        get() = _diarrhoa

    private val _rashesOrDiscoloration = MutableLiveData<Symptoms>()
    val rashesOrDiscoloration: LiveData<Symptoms>
        get() = _rashesOrDiscoloration

    private val _breathingComplication = MutableLiveData<Symptoms>()
    val breathingComplication: LiveData<Symptoms>
        get() = _breathingComplication

    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String>
        get() = _error



//    suspend fun symptoms() {
//        viewModelScope.launch {
//            val symptom = Symptom() {
//                _fever.value = Symptom.FEVER
//            }
//        }
//
//    }

//    suspend fun getPatient() {
//        viewModelScope.launch {
//            val resources = httpClient().getAllRegisteredPatientsForWard().map {
//                Log.d("RegisterSymptomsViewMod", "getPatient: \n ${Json.encodeToString(it.resources)}")
//                _selectedPatient.value = it.resources.map {
//                    Patient.tryFromResource(it)
//                }
//            }.mapLeft {
//                Log.d("RegisterSymptomsViewMod", "getPatient: $it")
//            }
//        }.join()
//    }
}

class Patient(
    val id: String
)

class Symptoms(
    val fever: String,
    val couching: String

){

    companion object {
        fun tryFromResource(idResource: IdResource<String>?): Patient {
            idResource?.id.let { id ->
                return Patient(id!!)
            }
        }
    }
}

